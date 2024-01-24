package zet.kedzieri.bank.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserAgentAuthenticationDetailsSourceImpl authenticationDetailsSource;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity sec) throws Exception {
        return sec
                .authorizeHttpRequests(ahr -> ahr
                        .requestMatchers("/api/v1/auth/get_pattern_self").authenticated()
                        .requestMatchers("/api/v1/auth/**").permitAll()
                        .requestMatchers("/api/v1/debug/**").permitAll()
                        .requestMatchers("/api/v1/password/change_password_email_generate").permitAll()
                        .requestMatchers("/api/v1/password/change_password_email").permitAll()
                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().denyAll()
                )
                .exceptionHandling(eh -> eh
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(401, "Please, login first.");
                        })
                )
                .cors(cors -> cors
                        .configurationSource(corsConfigurationSource())
                )
                .headers(headers -> headers
                        .xssProtection(Customizer.withDefaults())
                        .contentSecurityPolicy(csp -> csp
                                .policyDirectives("form-action 'self'; img-src 'self'; child-src 'none'; script-src 'self'")
                        )
                )
                .formLogin(fl -> fl
                        .loginPage("/login")
                        .loginProcessingUrl("/api/v1/auth/perform_login")
                        .defaultSuccessUrl("/api/v1/auth/login_success")
                        .failureUrl("/api/v1/auth/login_error")
                        .authenticationDetailsSource(authenticationDetailsSource)
                )
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/perform_logout")
                        .deleteCookies("JSESSIONID")
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/")
                )
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return new UrlBasedCorsConfigurationSource() {{
            registerCorsConfiguration("/**", new CorsConfiguration() {{
                setAllowedOrigins(List.of("https://localhost", "https://bank.verysecure"));
                addAllowedMethod("*");
                addAllowedHeader("*");
            }});
        }};
    }

}
