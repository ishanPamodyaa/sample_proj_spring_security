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
        
//        final String path = request.getServletPath();
    
    // Skip JWT filter for login endpoint
//    if (path.equals("/login")) {
//        filterChain.doFilter(request, response);
//        return;
//    }
        
        
        String authorization = request.getHeader("Authorization");
        if(authorization==null){
            filterChain.doFilter(request , response);
            return;
        
        }


        System.out.println(authorization);
        if(!authorization.startsWith("Bearer ")) {
            filterChain.doFilter(request , response);
            return;
        }
        String jwt_token = authorization.split(" ")[1];

        String username = jwtService.getUserName(jwt_token);
        if(username==null){ 
            filterChain.doFilter(request , response);
            return;
        }

        UserEntity userData = userRepository.findByUserName(username).orElse(null);
        if(userData == null){
            filterChain.doFilter(request,response);
            return;
        }
        if(SecurityContextHolder.getContext().getAuthentication()!=null){
            filterChain.doFilter(request,response);
            return;
        }

        UserDetails userDetails = User.builder()
                .username(userData.getUserName())
                .password(userData.getPassword())
                .build();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(token);


        filterChain.doFilter(request , response);
    }
}
