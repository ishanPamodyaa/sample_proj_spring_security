package edu.icet.filter;

import edu.icet.entity.UserEntity;
import edu.icet.repository.UserRepository;
import edu.icet.service.JWTService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserRepository userRepository;


    @Override
    protected void doFilterInternal( HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if(!authorization.startsWith("Bearer ")) { filterChain.doFilter(request , response);}

        String jwt_token = authorization.split(" ")[1];

        String username = jwtService.getUserName(jwt_token);
        if(username==null){ filterChain.doFilter(request , response);}

        UserEntity userData = userRepository.findByUserName(username).orElse(null);
        if(userData == null){filterChain.doFilter(request,response);}
        if(SecurityContextHolder.getContext().getAuthentication()!=null){filterChain.doFilter(request,response);}

        UserDetails userDetails = User.builder()
                .username(userData.getUserName())
                .password(userData.getPassword())
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(token);

        System.out.println(jwt_token);
        filterChain.doFilter(request , response);
    }
}
