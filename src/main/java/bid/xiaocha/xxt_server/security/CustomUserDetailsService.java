package bid.xiaocha.xxt_server.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import bid.xiaocha.xxt_server.entities.UserEntity;
import bid.xiaocha.xxt_server.repositories.UserRepository;


@Component
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		
		UserEntity userEntity = userRepository.findOne(username);
		
		if(userEntity == null)
			throw new UsernameNotFoundException("user not exist");
		
		return userEntity;
	}

}
