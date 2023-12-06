package net.greeta.authorbook.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtAuthConverter jwtAuthConverter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests

                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs", "/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.GET,"/api/authors", "/api/authors/**").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/books", "/api/books/**").permitAll()

                        .requestMatchers("/api/authors", "/api/authors/**").hasRole(BOOK_MANAGER)
                        .requestMatchers("/api/books", "/api/books/**").hasRole(BOOK_MANAGER)

                        .requestMatchers("/graphql", "/graphql/**").permitAll()
                        .requestMatchers("/graphiql", "/graphiql/**").permitAll()

                        .anyRequest().authenticated())
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer.jwt(
                        jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)))
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(csrf -> csrf.disable())
                .build();
    }

    public static final String BOOK_MANAGER = "BOOK_MANAGER";
    public static final String BOOK_USER = "BOOK_USER";
}