package edu.icet.service;

import edu.icet.entity.UserEntity;
import edu.icet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


//@Service
@RequiredArgsConstructor
public class MyUserDetailService implements UserDetailsService {

//    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity userData = userRepository.findByUserName(username).orElse(null);
        if(userData==null)throw new UsernameNotFoundException("user not found");

        UserDetails user = User
                .builder()
                .username(userData.getUserName())
                .password(userData.getPassword())
                .build();
        return user;
    }
}
