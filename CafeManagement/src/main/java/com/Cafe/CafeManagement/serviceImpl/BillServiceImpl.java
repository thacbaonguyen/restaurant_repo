package com.Cafe.CafeManagement.serviceImpl;

import com.Cafe.CafeManagement.DAO.BillRepository;
import com.Cafe.CafeManagement.JWT.JwtFilter;
import com.Cafe.CafeManagement.POJO.Bill;
import com.Cafe.CafeManagement.constants.CafeConstants;
import com.Cafe.CafeManagement.service.BillService;
import com.Cafe.CafeManagement.utils.CafeResponse;
import com.Cafe.CafeManagement.utils.CafeUtils;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.pdfbox.io.IOUtils;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class BillServiceImpl implements BillService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    JwtFilter jwtFilter;
    @Override
    public ResponseEntity<String> generateReport(Map<String, Object> request) {
        try {
            String fileName;
            if(validateRequestMap(request)){
                if(request.containsKey("isGenerate") && !(Boolean)request.get("isGenerate")){
                    fileName = (String) request.get("uuid");
                }
                else {
                    fileName = CafeUtils.generateUUID() + jwtFilter.getCurrentUser();
                    request.put("uuid", fileName);
                    insertBill(request);
                }
                String data = "Ho ten: " + request.get("name") + "\n"
                        + "So dien thoai: " + request.get("contactNumber") + "\n"
                        + "Email: " + request.get("email") + "\n"
                        + "Phuong thuc thanh toan: " + request.get("paymentMethod");

                // Create a document
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(CafeConstants.LOCATION + "\\" + fileName + ".pdf"));
                document.open();
                setRectangle(document); // goi ham setRectangle add rectangle to document

                Paragraph title = new Paragraph("Cafe Choi` Ha Noi", getFont("Header")); // tieu de hoa don
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title); // add title to document

                 Paragraph dataParagraph = new Paragraph(data + "\n \n", getFont("Data"));
                 document.add(dataParagraph); // add data to document

                PdfPTable pdfPTable = new PdfPTable(5); // create a table have 5 column
                pdfPTable.setWidthPercentage(100);
                addTable(pdfPTable); // add cac o trong ham addTable vao pdfPTable

                JSONArray jsonArray = new JSONArray((String)request.get("productDetails"));
                for(int i = 0; i < jsonArray.length(); i++){
                    addRows(pdfPTable, CafeUtils.getMapFromJson(jsonArray.getString(i)));
                }
                document.add(pdfPTable); // add table to document

                Paragraph footerParagraph = new Paragraph("Tong hoa don: "  + request.get("totalAmount") +"\n"
                        + "Cafe choi cam on quy khach, see u later!");
                document.add(footerParagraph);
                document.close();
                return new ResponseEntity<>("{\"uuid\":\"" + fileName + "\"}", HttpStatus.OK);
            }
            return CafeResponse.getResponseEntity("Some field is required!", HttpStatus.BAD_REQUEST);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Bill>> getBills() {
        try {
            if(jwtFilter.isAdmin()){
                return new ResponseEntity<>(billRepository.findAll(), HttpStatus.OK);
            } else if (jwtFilter.isUser()) {
                String userName = jwtFilter.getCurrentUser();
                return new ResponseEntity<>(billRepository.findByCreatedBy(userName), HttpStatus.OK);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<byte[]> getPdf(Map<String, Object> request) {
        try {
            byte[] byteArray = new byte[0];
            if(!request.containsKey("uuid") && validateRequestMap(request)){
                return new ResponseEntity<>(byteArray, HttpStatus.BAD_REQUEST);
            }
            String filePath = CafeConstants.LOCATION + "\\" + request.get("uuid") + ".pdf";
            if (CafeUtils.isExistFile(filePath)){
                byteArray = getByArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
            else {
                request.put("isGenerate", false);
                generateReport(request);
                byteArray = getByArray(filePath);
                return new ResponseEntity<>(byteArray, HttpStatus.OK);
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<String> deleteBill(Integer id) {
        try {
            Optional<Bill> bill = billRepository.findById(id);
            if(!bill.isEmpty()){
                billRepository.deleteById(id);
                return CafeResponse.getResponseEntity("Delete bill successfully", HttpStatus.OK);
            }
            return CafeResponse.getResponseEntity("This bill does not exist!", HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] getByArray(String filePath) throws IOException {
        File file = new File(filePath);
        InputStream inputStream = new FileInputStream(file);
        byte[] byteArray = IOUtils.toByteArray(inputStream);
        inputStream.close();
        return byteArray;
    }

    private void addRows(PdfPTable pdfPTable, Map<String, Object> mapFromJson) {
        pdfPTable.addCell((String) mapFromJson.get("name"));
        pdfPTable.addCell((String) mapFromJson.get("category"));
        pdfPTable.addCell((String) mapFromJson.get("quantity"));
        pdfPTable.addCell(Double.toString((Double) mapFromJson.get("price")));
        pdfPTable.addCell(Double.toString((Double) mapFromJson.get("total")));
    }

    private void addTable(PdfPTable pdfPTable) {
        Stream.of("Ten", "Danh muc", "So luong", "Gia", "Thanh tien")
                .forEach(title ->{
                    PdfPCell pdfPCell = new PdfPCell(); // tao 1 o moi
                    pdfPCell.setBackgroundColor(BaseColor.LIGHT_GRAY); // mau nen cua o
                    pdfPCell.setBorderWidth(1); // do day cua vien o
                    pdfPCell.setPhrase(new Phrase(title)); // noi dung cua o
                    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER); // can noi dung theo chieu ngang
                    pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER); // theo chieu doc
                    pdfPTable.addCell(pdfPCell); // them o vao bang
                });
    }

    private Font getFont(String type) {
        if(type.equals("Header")){
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLDOBLIQUE, 18, BaseColor.BLACK);
            headerFont.setStyle(Font.BOLD);
            return headerFont;
        } else if (type.equals("Data")) {
            Font dataFont = FontFactory.getFont(FontFactory.TIMES_ROMAN, 15 , BaseColor.BLACK);
            dataFont.setStyle(Font.BOLD);
            return dataFont;
        }
        return new Font();
    }

    private void setRectangle(Document document) throws DocumentException {
        Rectangle rectangle =  new Rectangle(577, 825, 18, 15); // x y width height
        rectangle.enableBorderSide(1); // kich hoat duong vien canh top
        rectangle.enableBorderSide(2); // bottom
        rectangle.enableBorderSide(4); // left
        rectangle.enableBorderSide(8); // right

        rectangle.setBackgroundColor(BaseColor.WHITE);
        rectangle.setBorderWidth(1);
        document.add(rectangle); // add rectangle in document
    }

    private void insertBill(Map<String, Object> request) {
        try {
            Bill bill = Bill.builder()
                    .name((String) request.get("name"))
                    .contactNumber((String) request.get("contactNumber"))
                    .email((String) request.get("email"))
                    .paymentMethod((String) request.get("paymentMethod"))
                    .productDetail((String) request.get("productDetails"))
                    .uuid((String) request.get("uuid"))
                    .total(Long.parseLong((String) request.get("totalAmount")))
                    .createdBy(jwtFilter.getCurrentUser())
                    .build();
            billRepository.save(bill);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean validateRequestMap(Map<String, Object> request) {
        return request.containsKey("name") &&
                request.containsKey("email") &&
                request.containsKey("contactNumber")&&
                request.containsKey("paymentMethod") &&
                request.containsKey("totalAmount") &&
                request.containsKey("productDetails");
    }
}
