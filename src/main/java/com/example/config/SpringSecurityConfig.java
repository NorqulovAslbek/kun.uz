package com.example.config;

import com.example.util.MDUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity()
public class SpringSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtTokenFilter jwtTokenFilter;

    public static final String[] AUTH_WHITELIST={
            "/v2/api-docs",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-resources",
            "/swagger-resources/**",

            "/auth/*",
            "/auth/verification/email/*",
            "/init/admin",
            "/init/**",
            "/region/byLang",
            "/auth/*/*",
            "/attach/open/*",
            "/attach/open_general/*",
            "attach/upload",
            "article/typeId/id",
            "article/articles",
            "article/articleId",
            "article/mostReadArticles",
            "article/articleListByRegionId",
            "article/last5ArticleCategoryKeys",
            "article/articlesByCategory",
            "article/IncreaseArticle/{id}",
            "article/IncreaseShare/{id}",
            "/articleType/lang",
           " /article/typeId/id",
            "/article/articles",
            "/article/articleId",
            "/article/mostReadArticles",
            "/article/articleListByRegionId",
            "/article/last5ArticleCategoryKeys",
            "/article/articlesByCategory",
            "/article/IncreaseArticle/*",
            "/article/IncreaseShare/*",
            "/comment/open/*",
            "/comment/{id}"

    };
    @Bean
    public AuthenticationProvider authenticationProvider() {
        // authentication
//        String password = UUID.randomUUID().toString();
//        System.out.println("User Pasword mazgi: " + password);
//
//        UserDetails user = User.builder()
//                .username("user")
//                .password("{noop}" + password)
//                .roles("ADMIN")
//                .build();

        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // authorization
//        http.authorizeHttpRequests()
//                .anyRequest()
//                .authenticated()
//                .and().formLogin();
//
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
            authorizationManagerRequestMatcherRegistry
                    .requestMatchers(AUTH_WHITELIST).permitAll()
                    .requestMatchers("/region/adm/**").hasRole("ADMIN")
                    .requestMatchers("/region/adm").hasRole("ADMIN")
                    .requestMatchers("/profile/adm","/profile/adm/*").hasRole("ADMIN")
                    .requestMatchers("/comment/adm","/comment/adm/*").hasRole("ADMIN")
                    .anyRequest()
                    .authenticated();
        });
//        http.httpBasic(Customizer.withDefaults());
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(AbstractHttpConfigurer::disable);


        return http.build();
    }
    public PasswordEncoder passwordEncoder() {
        return new PasswordEncoder() {
            @Override
            public String encode(CharSequence rawPassword) {
                return rawPassword.toString();
            }

            @Override
            public boolean matches(CharSequence rawPassword, String encodedPassword) {
                return MDUtil.encode(rawPassword.toString()).equals(encodedPassword);
            }
        };
    }



}
