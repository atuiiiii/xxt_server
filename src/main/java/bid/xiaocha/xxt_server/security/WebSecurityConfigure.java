package bid.xiaocha.xxt_server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true) //启用全局方法安全。
public class WebSecurityConfigure extends WebSecurityConfigurerAdapter {

	@Autowired
	private DaoAuthenticationProvider authenticationProvider;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(customUserService);
    	auth.authenticationProvider(authenticationProvider);
    }

    
    /*
     * 配置自定义的authenticationProvider,因为默认的实现会隐藏UsernameNotFoundException
     * 从这个类中根据注入的userDeatilsService获取user
     * 使用passwordEncoder编码后校检密码并返回结果*/
    @Bean
	public DaoAuthenticationProvider authenticationProvider(@Autowired UserDetailsService userDetailsService, @Autowired PasswordEncoder encoder) {
	    DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
	    provider.setHideUserNotFoundExceptions(false);
	    provider.setUserDetailsService(userDetailsService);
	    provider.setPasswordEncoder(encoder);
	    return provider;
	}
    
   
    
    @Bean
    public PasswordEncoder getPasswordEncoder(){
    	return new StandardPasswordEncoder();
    }
    
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        // 由于使用的是JWT，我们这里不需要csrf
        .csrf().disable()

        // 基于token，所以不需要session
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests()
        .antMatchers("/login","/getNeedServesByPage","/getOfferServesByPage").permitAll()
        .anyRequest().authenticated()         
        .and()
        .addFilterBefore(new AuthFilter(authenticationManager()), UsernamePasswordAuthenticationFilter.class);
        //对spring security的filter，有默认的过滤链顺序，并且对于同一种Filter， 只有第一个生效
    }
}
