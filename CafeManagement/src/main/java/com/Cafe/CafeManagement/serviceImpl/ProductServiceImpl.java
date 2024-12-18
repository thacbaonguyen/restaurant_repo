package com.Cafe.CafeManagement.serviceImpl;

import com.Cafe.CafeManagement.DAO.CategoryRepository;
import com.Cafe.CafeManagement.DAO.ProductRepository;
import com.Cafe.CafeManagement.JWT.JwtFilter;
import com.Cafe.CafeManagement.POJO.Category;
import com.Cafe.CafeManagement.POJO.Product;
import com.Cafe.CafeManagement.constants.CafeConstants;
import com.Cafe.CafeManagement.service.ProductService;
import com.Cafe.CafeManagement.utils.CafeResponse;
import com.Cafe.CafeManagement.wrapper.ProductWrapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    JwtFilter jwtFilter;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    ModelMapper modelMapper;
    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> request) {
        try {
            if(jwtFilter.isAdmin()){
                if(validateProductMap(request)){
                    Product product = getProductFromMap(request);
                    product.setStatus("true");
                    productRepository.save(product);
                    return CafeResponse.getResponseEntity(CafeConstants.CREATE_SUCCESSFULLY, HttpStatus.OK);
                }
                return CafeResponse.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeResponse.getResponseEntity(CafeConstants.UN_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
        try {
            List<Product> products = productRepository.findAll();
            ArrayList<ProductWrapper> productWrappers = new ArrayList<>();
            for(Product item : products){
                productWrappers.add(modelMapper.map(item, ProductWrapper.class));
            }
            return new ResponseEntity<>(productWrappers, HttpStatus.OK);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateProduct(Map<String, String> request) {
        try {
            if(jwtFilter.isAdmin()){
                if (validateProductMap(request) && request.containsKey("id")){
                    Optional<Product> product = productRepository.findById(Integer.parseInt(request.get("id")));
                    if(!product.isEmpty()){
                        productRepository.updateProduct(Integer.parseInt(request.get("id")), request.get("name"),
                                request.get("description")
                        , Long.parseLong(request.get("price")), Integer.parseInt(request.get("categoryId")));
                        return CafeResponse.getResponseEntity("Update product successfully!", HttpStatus.OK);
                    }
                    return CafeResponse.getResponseEntity("This product does not existing!", HttpStatus.NOT_FOUND);
                }
                return CafeResponse.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
            return CafeResponse.getResponseEntity(CafeConstants.UN_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> deleteProduct(Integer id) {
        try {
            if(jwtFilter.isAdmin()){
                Optional<Product> product = productRepository.findById(id);
                if(!product.isEmpty()){
                    productRepository.deleteById(id);
                    return CafeResponse.getResponseEntity("Delete product successfully!", HttpStatus.OK);
                }
                return CafeResponse.getResponseEntity("This product does not exist!", HttpStatus.NOT_FOUND);
            }
            return CafeResponse.getResponseEntity(CafeConstants.UN_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getByCategory(Integer id) {
        try {
            Optional<Category> category = categoryRepository.findById(id);
            if(!category.isEmpty()){
                List<Product> products = productRepository.findByCategoryId(id);
                ArrayList<ProductWrapper> productWrappers = new ArrayList<>();
                for(Product item : products){
                    if(item.getStatus().equalsIgnoreCase("true")) {
                        productWrappers.add(modelMapper.map(item, ProductWrapper.class));
                    }
                }
                return new ResponseEntity<>(productWrappers, HttpStatus.OK);
            }
            return new ResponseEntity<>("This category does not exist!", HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> getById(Integer id) {
        try {
            Optional<Product> product = productRepository.findById(id);
            if(!product.isEmpty()){
             ProductWrapper productWrapper = modelMapper.map(product.get(), ProductWrapper.class);
             return new ResponseEntity<>(productWrapper, HttpStatus.OK);
            }
            return CafeResponse.getResponseEntity("This product does not exist!", HttpStatus.NOT_FOUND);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateStatus(Map<String, String> request) {
        try {
            if(jwtFilter.isAdmin()){
                Optional<Product> product = productRepository.findById(Integer.parseInt(request.get("id")));
                if(!Objects.isNull(product.get())){
                    productRepository.updateStatus(Integer.parseInt(request.get("id")), request.get("status"));
                    return CafeResponse.getResponseEntity("Update successfully", HttpStatus.OK);
                }
                return CafeResponse.getResponseEntity("This product does not exist", HttpStatus.CONFLICT);
            }
            return CafeResponse.getResponseEntity(CafeConstants.UN_AUTHORIZED, HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return CafeResponse.getResponseEntity(CafeConstants.SOME_THING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateProductMap(Map<String, String> request){
        return request.containsKey("name") && request.containsKey("price");
    }
    private Product getProductFromMap(Map<String, String> request){
        Product product = Product.builder()
                .name(request.get("name"))
                .price(Long.parseLong(request.get("price")))
//                .status("true")
                .build();
        if(request.containsKey("description")) product.setDescription(request.get("description"));
        if(request.containsKey("categoryId")){
            Optional<Category> category = categoryRepository.findById(Integer.parseInt(request.get("categoryId")));
            if(!Objects.isNull(category.get())){
                product.setCategory(category.get());
            }
        }
        return product;
    }
}
