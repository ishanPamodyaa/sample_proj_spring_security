package edu.icet.controller;

import edu.icet.service.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HomeController {

    private final JWTService jwtService;

    @GetMapping
    public String getHello(){
        return "hi bro";
    }

    @PostMapping("/login")
    public String login(){
        return jwtService.getJWTToken();
    }

    @GetMapping("/username")
    public String getUserName(@RequestParam String token){
        return jwtService.getUserName(token);
    }
}
