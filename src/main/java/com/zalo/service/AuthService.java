package com.zalo.service;

import com.zalo.dto.LoginRequest;
import com.zalo.dto.LoginResponse;
import com.zalo.dto.RegisterRequest;
import com.zalo.model.User;
import com.zalo.repository.UserRepository;
import com.zalo.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    public LoginResponse login(LoginRequest loginRequest) {

        String phoneNumber = formatPhoneNumber(loginRequest.getCountryCode(), loginRequest.getPhoneNumber());

        Optional<User> userOptional = userRepository.findByPhoneNumber(phoneNumber);

        if (userOptional.isEmpty()) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Tài khoản không tồn tại")
                    .build();
        }

        User user = userOptional.get();

        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Mật khẩu không chính xác")
                    .build();
        }

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return LoginResponse.builder()
                .success(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .profileName(user.getProfileName())
                .avatarUrl(user.getAvatarUrl())
                .message("Đăng nhập thành công")
                .build();
    }

    public String generateQRLoginCode(String userId) {
        String qrToken = jwtTokenProvider.generateQRToken(userId);
        return qrToken;
    }

    public LoginResponse verifyQRLogin(String qrToken) {

        if (!jwtTokenProvider.validateQRToken(qrToken)) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Mã QR không hợp lệ hoặc đã hết hạn")
                    .build();
        }

        String userId = jwtTokenProvider.getUserIdFromQRToken(qrToken);
        Optional<User> userOptional = userRepository.findById(Long.parseLong(userId));

        if (userOptional.isEmpty()) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Không tìm thấy thông tin người dùng")
                    .build();
        }

        User user = userOptional.get();

        String accessToken = jwtTokenProvider.generateAccessToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        return LoginResponse.builder()
                .success(true)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .profileName(user.getProfileName())
                .avatarUrl(user.getAvatarUrl())
                .message("Đăng nhập thành công")
                .build();
    }

    private String formatPhoneNumber(String countryCode, String phoneNumber) {
        if (phoneNumber.startsWith(countryCode.replace("+", ""))) {
            return phoneNumber;
        }
        if (phoneNumber.startsWith("0")) {
            phoneNumber = phoneNumber.substring(1);
        }
        return phoneNumber;
    }

    public LoginResponse register(@Valid RegisterRequest registerRequest) {
        String phoneNumber = formatPhoneNumber(registerRequest.getCountryCode(), registerRequest.getPhoneNumber());

        if (userRepository.existsByPhoneNumber(phoneNumber)) {
            return LoginResponse.builder()
                    .success(false)
                    .message("Số điện thoại đã được đăng ký")
                    .build();
        }

        User newUser = new User();
        newUser.setPhoneNumber(phoneNumber);
        newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        newUser.setProfileName(registerRequest.getProfileName());
        newUser.setActive(true);

        userRepository.save(newUser);

        return LoginResponse.builder()
                .success(true)
                .message("Đăng ký thành công")
                .build();
    }
}