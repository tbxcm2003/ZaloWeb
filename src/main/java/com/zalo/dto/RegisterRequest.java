package com.zalo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{9,12}$", message = "Số điện thoại không hợp lệ")
    private String phoneNumber;

    @NotBlank(message = "Mật khẩu không được để trống")
    private String password;

    @NotBlank(message = "Tên hiển thị không được để trống")
    private String profileName;

    private String countryCode = "+84";
}