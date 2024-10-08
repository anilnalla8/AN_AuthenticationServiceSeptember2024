package org.example.userauthenticationservice_sept2024.controller;

import org.example.userauthenticationservice_sept2024.config.RequestStatus;
import org.example.userauthenticationservice_sept2024.dto.LoginRequestDto;
import org.example.userauthenticationservice_sept2024.dto.LoginResponseDto;
import org.example.userauthenticationservice_sept2024.dto.SignUpRequestDto;
import org.example.userauthenticationservice_sept2024.dto.SignUpResponseDto;
import org.example.userauthenticationservice_sept2024.exception.UserAlreadyExistsException;
import org.example.userauthenticationservice_sept2024.exception.UserNotFoundException;
import org.example.userauthenticationservice_sept2024.exception.WrongPasswordException;
import org.example.userauthenticationservice_sept2024.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @PostMapping("/sign_up")
    public ResponseEntity<SignUpResponseDto> signUp(@RequestBody SignUpRequestDto request ) throws UserAlreadyExistsException {
        SignUpResponseDto response = new SignUpResponseDto();

       try {
            if (authService.signUp(request.getEmail(), request.getPassword())) {
                response.setRequestStatus(RequestStatus.SUCCESS);
            } else {
                response.setRequestStatus(RequestStatus.FAILURE);
            }
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (
                Exception e) {
            response.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto request) throws UserNotFoundException {
        LoginResponseDto response = new LoginResponseDto();
        try {
            try {
                authService.login(request.getEmail(), request.getPassword());
            } catch (WrongPasswordException e) {
                throw new RuntimeException(e);
            }
            response.setRequestStatus(RequestStatus.SUCCESS);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (
                Exception e) {
            response.setRequestStatus(RequestStatus.FAILURE);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
}
