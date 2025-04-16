package edu.icet.service;


import edu.icet.dto.LoginRequestDTO;
import edu.icet.dto.LoginResponseDTO;
import edu.icet.dto.RegisterRequestDto;
import edu.icet.dto.RegisterResponseDto;
import edu.icet.entity.UserEntity;
import edu.icet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity createUser(RegisterRequestDto userDetails) {
        UserEntity newUser = new UserEntity();
        newUser.setName(userDetails.getName());
        newUser.setUserName(userDetails.getUserName());
        newUser.setEmail(userDetails.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));

        return userRepository.save(newUser);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
//        Boolean userPresent = isAvailable(loginRequestDTO.getUsername());
//
//        if(!userPresent){
//
//        }
   try {
       authenticationManager.authenticate( new UsernamePasswordAuthenticationToken(loginRequestDTO.getUserName() , loginRequestDTO.getPassword()));
   } catch (Exception e) {
       return new LoginResponseDTO(null , null , "User Not Found","Error");
   }

        Map<String,Object> claims = new HashMap<String,Object>();
   claims.put("role","USER");
   claims.put("email","company@gmail.com");

   String token = jwtService.getJWTToken(loginRequestDTO.getUserName(),claims);
        System.out.println(jwtService.getFieldFromToken(token,"role"));
    return new LoginResponseDTO(token, LocalDateTime.now(),null,"massage token");
    }

    public RegisterResponseDto register(RegisterRequestDto registerRequestDto){
        if(isAvailable(registerRequestDto.getUserName())){
            return new RegisterResponseDto(null,"User Already exist in the system");
        }
        var userData = this.createUser(registerRequestDto);
        if(userData.getId() == null){

            return new RegisterResponseDto(null,"System Error");
        }

        return new RegisterResponseDto(String.format("user registered at ",userData.getId()),null);
    }

    private Boolean isAvailable(String userName){

        return userRepository.findByUserName(userName).isPresent();

    }
}
