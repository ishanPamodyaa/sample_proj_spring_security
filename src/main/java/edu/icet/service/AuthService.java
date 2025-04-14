package edu.icet.service;


import edu.icet.dto.LoginRequestDTO;
import edu.icet.dto.LoginResponseDTO;
import edu.icet.entity.UserEntity;
import edu.icet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity createUser(UserEntity userDetails) {
        UserEntity newUser = new UserEntity();
        newUser.setName(userDetails.getName());
        newUser.setUserName(userDetails.getUserName());
        newUser.setEmail(userDetails.getEmail());
        newUser.setPassword(passwordEncoder.encode(userDetails.getPassword()));

        return userRepository.save(newUser);
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        Boolean userPresent = isAvailable(loginRequestDTO.getUsername());

        if(!userPresent){
            return new LoginResponseDTO(null , null , "User Not Found","Error")
        }
    }

    private Boolean isAvailable(String userName){

        return userRepository.findByUserName(userName).isPresent();

    }
}
