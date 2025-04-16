package edu.icet.controller;


import edu.icet.dto.LoginRequestDTO;
import edu.icet.dto.LoginResponseDTO;
import edu.icet.dto.RegisterRequestDto;
import edu.icet.dto.RegisterResponseDto;
import edu.icet.entity.UserEntity;
import edu.icet.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @GetMapping
    public List<UserEntity> getAllUsers(){

        return authService.getAllUsers();
    }

    @PostMapping
    public UserEntity createUser(@RequestBody RegisterRequestDto userDetail){
        return authService.createUser(userDetail);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        System.out.println(loginRequestDTO);
        LoginResponseDTO responseDTO = authService.login(loginRequestDTO);
        if(responseDTO.getError() != null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@RequestBody RegisterRequestDto registerRequestDto){
        System.out.println(registerRequestDto);
        RegisterResponseDto responseDTO = authService.register(registerRequestDto);
        if(responseDTO.getError() != null) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDTO);
        }
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }


}
