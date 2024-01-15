package com.reto.elorchat.security.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.reto.elorchat.security.persistance.User;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component
public class JwtTokenFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtUtil;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		// esta funcion comprueba si tiene el token y en tal caso, carga el usuario
		// si no lo tiene, no carga el usuario
		// en cualquier caso pasa al siguiente filtro con filterChain.doFilter

		if (!hasAuthorizationBearer(request)) {
			filterChain.doFilter(request, response);
			return;
		}
		String token = getAccessToken(request);
		if (!jwtUtil.validateAccessToken(token)) {
			filterChain.doFilter(request, response);
			return;
		}
		setAuthenticationContext(token, request); // en el caso de ser valido el token se carga el usuario
		filterChain.doFilter(request, response);
	}


	private boolean hasAuthorizationBearer(HttpServletRequest request) {
		// comprueba sin en el header existe el header de authorization y si esta bien formado
		String header = request.getHeader("Authorization");
		if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer ")) {
			return false;
		}

		return true;
	}

	// obtiene el jwt de la cabecera de la peticion
	private String getAccessToken(HttpServletRequest request) {
		String header = request.getHeader("Authorization");
		String token = header.split(" ")[1].trim();
		return token;
	}


	private void setAuthenticationContext(String token, HttpServletRequest request) {
		// pone el usuario en el contexto con los datos del jwt
		UserDetails userDetails = getUserDetails(token);
		// el ultimo campo es la lista de los roles


		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
				userDetails, 
				null, 
				userDetails.getAuthorities()
				);
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		//		
		SecurityContextHolder.getContext().setAuthentication(authentication); // con esta linea se pone el usuario en el contexto
	}


	private UserDetails getUserDetails(String token) {
		// genera los detalles del usuario a partir del jwt
		User userDetails = new User();
		userDetails.setId(jwtUtil.getUserId(token));
		userDetails.setEmail(jwtUtil.getSubject(token));

		return userDetails;
	}

}
