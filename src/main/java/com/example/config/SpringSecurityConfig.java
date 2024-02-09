//package com.example.config;
//
//import com.example.util.MDUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationProvider;
//import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.Customizer;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.InMemoryUserDetailsManager;
//import org.springframework.security.web.SecurityFilterChain;
//
//import java.util.UUID;
//
//@Configuration
//public class SpringSecurityConfig {
//    @Autowired
//    private UserDetailsService userDetailsService;
//
//    @Bean
//    public AuthenticationProvider authenticationProvider() {
//        // authentication
////        String password = UUID.randomUUID().toString();
////        System.out.println("User Pasword mazgi: " + password);
////
////        UserDetails user = User.builder()
////                .username("user")
////                .password("{noop}" + password)
////                .roles("ADMIN")
////                .build();
//
//        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//        authenticationProvider.setUserDetailsService(userDetailsService);
//        authenticationProvider.setPasswordEncoder(passwordEncoder());
//        return authenticationProvider;
//    }
//
//
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // authorization
////        http.authorizeHttpRequests()
////                .anyRequest()
////                .authenticated()
////                .and().formLogin();
////
//        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
//            authorizationManagerRequestMatcherRegistry
//                    .requestMatchers("/auth/**").permitAll()
//                    .requestMatchers("/init/admin").permitAll()
//                    .requestMatchers("/init/**").permitAll()
//                    .requestMatchers("/region/byLang").permitAll()
//                    .requestMatchers("/region/adm/**").hasRole("ADMIN")
//                    .requestMatchers("/profile/adm","/profile/adm/*").hasRole("ADMIN")
//                    .anyRequest()
//                    .authenticated();
//        });
//        http.httpBasic(Customizer.withDefaults());
//        http.csrf(AbstractHttpConfigurer::disable);
//        http.cors(AbstractHttpConfigurer::disable);
//
//
//        return http.build();
//    }
//    public PasswordEncoder passwordEncoder() {
//        return new PasswordEncoder() {
//            @Override
//            public String encode(CharSequence rawPassword) {
//                return rawPassword.toString();
//            }
//
//            @Override
//            public boolean matches(CharSequence rawPassword, String encodedPassword) {
//                return MDUtil.encode(rawPassword.toString()).equals(encodedPassword);
//            }
//        };
//    }
//
//
//
//}
