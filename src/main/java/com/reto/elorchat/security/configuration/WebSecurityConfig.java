package com.reto.elorchat.security.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {
	

	@Autowired 
	private JwtTokenFilter jwtTokenFilter;
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// esta es la funcion que define cual es el metodo de cifrado para la contrasenia en BBDD
		// cuando hagamos login, comparara la contrasenia con 
		return new BCryptPasswordEncoder();
	}

	
	// aqui definimos principalmente cuales son las urls van a poder ser accesibles sin identificarse
	// y cuales seran obligatorias
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
			.csrf( csrf ->
				csrf.disable()
				// desactivamos el CSRF, que impediría realizar peticiones POST
			)
			.sessionManagement(session -> 
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			)
			.authorizeHttpRequests((requests) -> requests
				// vamos a autorizar las peticiones http y sigue la siguiente norma:
				// va a evaluar todas las reglas, si cumple una de las reglas, hara lo que este especificado
				// en dicha regla. Si no, pasa a evaluarse la siguiente regla.
				// asi hasta llegar al anyRequest() que todas las urls que no cumplan ninguna regla anterior
				// se someteran a dicha restriccion
					
				// vamos a permitir registro y login para todos. Al no especificar metodo
				// podran hacer todos los metodos a dichas urls...
				.requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
				
				
				
				// employees
				// permitimos el get a cualquiera
				//.requestMatchers("/api/employees/{id}").permitAll()
				// .requestMatchers(HttpMethod.GET, "/api/employees", "/api/employees/{id}").permitAll()
				
				// para todo lo demas habra que estar autenticado
				//.requestMatchers(HttpMethod.GET, "/api/employees").hasAuthority(RoleEnum.ADMIN.toString())
				.requestMatchers(HttpMethod.GET, "/index.html", "/prueba1.html").permitAll()
				
				//.anyRequest().authenticated()
				.anyRequest().permitAll()
			)
			// el siguiente bloque de codigo ajusta los codigos de error, para devolver 401 en caso de que tenga que estar autenticado
			.exceptionHandling((exceptionHandling) ->
				exceptionHandling.authenticationEntryPoint((request, response, ex) -> {
					if (response.getStatus() < 400) {
						response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
					}
				})
//				.accessDeniedHandler((request, response, ex) -> {
//					if (response.getStatus() < 400) {
//						response.setStatus(HttpServletResponse.SC_FORBIDDEN);
//					}
//				})
 			)
			.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
			

		return http.build();
	}
}
