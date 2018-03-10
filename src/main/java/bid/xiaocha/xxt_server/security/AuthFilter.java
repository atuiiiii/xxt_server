package bid.xiaocha.xxt_server.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import bid.xiaocha.xxt_server.entities.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;



public class AuthFilter extends BasicAuthenticationFilter{

	
	public AuthFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
	}
	
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		String header = request.getHeader("jwt");
		
		if(header == null || header.equals("")){
			//请求没有携带token
			filterChain.doFilter(request, response);
			return;
		}
		
		//header中携带有token信息
		
		UsernamePasswordAuthenticationToken token = getAuthentication(request);
		
		if(SecurityContextHolder.getContext().getAuthentication() == null){
			SecurityContextHolder.getContext().setAuthentication(token);
		}
		
		filterChain.doFilter(request, response);
	}
	
	

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request){
		//从header中解析用户信息
		String token = request.getHeader("jwt");
		if(token != null){
			Claims claims = Jwts.parser()
					.setSigningKey("balala")
	                .parseClaimsJws(token)
	                .getBody();
			
			JSONObject data = JSON.parseObject(claims.getSubject());
			String userId = data.getString("userId");
			List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
			

	        for (Object auth : data.getJSONArray("auth")) {
	            auths.add(new SimpleGrantedAuthority((String)auth));
	        }
			
			if(userId != null){
				return new UsernamePasswordAuthenticationToken(userId, null, auths);
			}
			
		}
		
		return null;
	}
	
}