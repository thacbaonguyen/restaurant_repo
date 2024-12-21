export class GlobalConstant {
  //message
  public static genericError: string = 'Đã xảy ra lỗi, vui lòng thử lại sau!';

  // regex
  // public static nameRegex: string = '[a-zA-Z0-9 ]*';
  public static nameRegex: string = '[a-zA-ZÀÁÂÃÈÉÊÌÍÒÓÔÕÙÚĂĐĨŨƠàáâãèéêìíòóôõùúăđĩũơƯĂẠẢẤẦẨẪẬẮẰẲẴẶẸẺẼỀỀỂưăạảấầẩẫậắằẳẵặẹẻẽềềểễệỈỊỌỎỐỒỔỖỘỚỜỞỠỢỤỦỨỪửữứựỳỵỷỹý ]*';

  public static productExist:string = 'Product already exist!';

  public static productSuccess:string = 'Add product successfully!';


  public static emailRegex: string =
    '[A-Za-z0-9._%-]+@[A-Za-z0-9._%-]+\\.[a-z]{2,3}';

  public static contactNumberRegex: string = '^[0-9]{10,10}$';

  // variable
  public static error: string = 'error';

  public static unauthorized: string = "Bạn không có quyền truy cập mục này!";
}
