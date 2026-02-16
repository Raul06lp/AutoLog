package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.firewall.StrictHttpFirewall;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Cadena principal de filtros de seguridad.
     * Define qué endpoints son públicos y cuáles requieren autenticación.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar CSRF: nuestra API es REST (sin formularios HTML)
            .csrf(csrf -> csrf.disable())

            // Política de sesiones: STATELESS → no se crean sesiones en servidor
            // El cliente envía credenciales en cada petición (Basic Auth)
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Reglas de autorización por endpoint
            .authorizeHttpRequests(auth -> auth

                // ── Endpoints PÚBLICOS (no requieren autenticación) ──────────────
                .requestMatchers(HttpMethod.POST, "/api/clientes/registro").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/clientes/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/mecanicos/registro").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/mecanicos/login").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/auth/registro").permitAll()
                .requestMatchers(HttpMethod.GET,  "/health").permitAll()

                // ── El resto requiere autenticación ──────────────────────────────
                .anyRequest().authenticated()
            )

            // Activar Basic Authentication (envío de usuario:password en cabecera)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    /**
     * Algoritmo de cifrado de contraseñas: BCrypt (estándar actual).
     * BCrypt incluye "salt" automáticamente, por lo que es resistente a
     * ataques de diccionario y rainbow tables.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Gestor de autenticación que usa DaoAuthenticationProvider.
     * Conecta Spring Security con nuestra BD a través de CustomUserDetailsService.
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return new ProviderManager(provider);
    }

    /**
     * Firewall HTTP que rechaza URLs con caracteres peligrosos (ej: ";").
     * Ayuda a prevenir ataques de path traversal y similares.
     */
    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowSemicolon(false);         // bloquear ";"
        firewall.setAllowBackSlash(false);         // bloquear "\"
        firewall.setAllowUrlEncodedPercent(false); // bloquear "%25"
        return firewall;
    }
}
