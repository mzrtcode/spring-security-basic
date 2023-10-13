package com.security.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration // Indicamos que esta clase es de configuracion
@EnableWebSecurity // Con esto especificamos que esta clase es de configuracion de Spring Security
public class SecurityConfig {




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                //.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests((authorizedRequests) ->
                        authorizedRequests
                                .requestMatchers("v1/index2").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin -> formLogin
                        .successHandler(successHandler())) //URL donde redirige despues de iniciar sesion
                        .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)//ALWAYS IF-REQUIRED NEVER STATELESS
                                .invalidSessionUrl("/login")
                                .maximumSessions(1)
                                .expiredUrl("/login")
                                .sessionRegistry(sessionRegistry())
                        )
                .sessionManagement(session -> session
                        .sessionFixation().migrateSession() //Prevenir vulnerabilidad de fijacion de sesion
                ).httpBasic(Customizer.withDefaults()) // Nos permite enviar credenciales por el Header de la peticion
                .build();
    }

    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }
    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication) -> {
           response.sendRedirect("/v1/session");
        });
    }


}
