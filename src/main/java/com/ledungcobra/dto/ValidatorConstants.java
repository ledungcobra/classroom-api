package com.ledungcobra.dto;

public class ValidatorConstants {
    public static final String USER_NAME_NULL_MSG = "Tên tài khoản không được null";
    public static final String USERNAME_EMPTY_MSG = "Tên tài khoản không được rỗng";
    public static final String PASSWORD_NULL_MSG = "Mật khẩu không được null";
    public static final String PASSWORD_EMPTY_MSG = "Mật khẩu không được rỗng";
    public static final String USERNAME_CANNOT_EMPTY = "Tài khoản không thể trống";
    public static final String EMAIL_VALIDATION_MESSAGE = "Email không hợp lệ";
    public static final String PASSWORD_VALIDATION_MESSAGE = "Mật khẩu phải có ít nhất một kí tự viết hoa một chữ số và một kí tự đặc biệt";
    public static final String PHONE_NUMBER_VALIDATION_MESSAGE = "Định dạng số điện thoại không hợp lệ";
    public static final String PHONE_NUMBER_PATTERN = "(84|0[3|5|7|8|9])+([0-9]{8})\\b";
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
}
