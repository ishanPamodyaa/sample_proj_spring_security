package edu.icet.controller;


import edu.icet.dto.LoginRequestDTO;
import edu.icet.dto.LoginResponseDTO;
import edu.icet.entity.UserEntity;
import edu.icet.service.AuthService;
import lombok.RequiredArgsConstructor;
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
    public UserEntity createUser(@RequestBody UserEntity userDetail){

        return authService.createUser(userDetail);

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){


    }


}
