package antifraud.config;

import antifraud.dao.userDao.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


    @Configuration
    @EnableWebSecurity
    public class SecurityConfiguration  {

        @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }

    private final UserDetailsServiceImpl userDetailsService;
        @Autowired
        public SecurityConfiguration(UserDetailsServiceImpl userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(HttpMethod.POST, "/api/auth/user").permitAll()
                            .requestMatchers("/actuator/shutdown").permitAll()
                            .requestMatchers(HttpMethod.GET,"/api/auth/list").hasAnyRole("ADMINISTRATOR", "SUPPORT")
                            .requestMatchers(HttpMethod.DELETE,"/api/auth/user/{username}").hasRole("ADMINISTRATOR")
                            .requestMatchers(HttpMethod.PUT,"/api/auth/role").hasRole("ADMINISTRATOR")
                            .requestMatchers(HttpMethod.PUT,"/api/auth/access").hasRole("ADMINISTRATOR")
                            .requestMatchers(HttpMethod.POST,"/api/antifraud/transaction").hasRole("MERCHANT")
                            .requestMatchers(HttpMethod.PUT,"/api/antifraud/transaction").hasRole("SUPPORT")
                            .requestMatchers(HttpMethod.GET,"/api/antifraud/history").hasRole("SUPPORT")
                            .requestMatchers("/api/antifraud/history/{number}").hasRole("SUPPORT")
                            .requestMatchers("/api/antifraud/suspicious-ip").hasRole("SUPPORT")
                            .requestMatchers("/api/antifraud/stolencard/{number}").hasRole("SUPPORT")
                            .requestMatchers("/api/antifraud/stolencard").hasRole("SUPPORT")
                            .requestMatchers("/api/antifraud/suspicious-ip/{ip}").hasRole("SUPPORT")
                    )
                    .sessionManagement(sessionManagement -> sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    )
                    .userDetailsService(userDetailsService)
                    .csrf(AbstractHttpConfigurer::disable)
                    .headers(headers -> headers.frameOptions().disable())
                    .httpBasic(Customizer.withDefaults());

            return http.build();
        }

    }

