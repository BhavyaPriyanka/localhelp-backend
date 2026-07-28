package com.localhelp.backend.security;


import com.localhelp.backend.model.User;
import com.localhelp.backend.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import org.springframework.stereotype.Component;

import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;
import java.util.List;



@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {



    private final JwtService jwtService;

    private final UserRepository userRepository;



    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserRepository userRepository
    ) {

        this.jwtService = jwtService;
        this.userRepository = userRepository;

    }





    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain

    ) throws ServletException, IOException {



        System.out.println("JWT FILTER HIT");



        String authHeader =
                request.getHeader("Authorization");



        String username = null;

        String token = null;




        if(authHeader != null &&
                authHeader.startsWith("Bearer ")) {


            token = authHeader.substring(7);



            try {

                username =
                        jwtService.extractUsername(token);


                System.out.println(
                        "TOKEN USER = " + username
                );


            }
            catch(Exception e) {


                response.setStatus(
                        HttpServletResponse.SC_UNAUTHORIZED
                );


                response.setContentType(
                        "application/json"
                );


                response.getWriter().write("""
                {
                  "status":401,
                  "error":"Unauthorized",
                  "message":"Invalid or expired JWT token"
                }
                """);


                return;

            }

        }





        if(username != null &&
                SecurityContextHolder
                        .getContext()
                        .getAuthentication() == null) {



            User user =
                    userRepository
                    .findByUsername(username)
                    .orElse(null);




            if(user != null) {



                List<SimpleGrantedAuthority> authorities =
                        List.of(
                            new SimpleGrantedAuthority(
                                "ROLE_" +
                                user.getRole().name()
                            )
                        );




                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                user.getUsername(),
                                null,
                                authorities
                        );




                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                        .buildDetails(request)
                );




                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authentication);



                System.out.println(
                        "AUTH SET = "
                        + username
                );

            }

        }



        filterChain.doFilter(
                request,
                response
        );

    }

}