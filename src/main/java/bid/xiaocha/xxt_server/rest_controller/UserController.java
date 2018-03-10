package bid.xiaocha.xxt_server.rest_controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import javax.jws.soap.SOAPBinding.Use;
import javax.management.relation.Role;
import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.apache.catalina.connector.Request;
import org.apache.http.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring4.requestdata.RequestDataValueProcessorUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import antlr.collections.List;
import bid.xiaocha.xxt_server.entities.RoleEntity;
import bid.xiaocha.xxt_server.entities.UserEntity;
import bid.xiaocha.xxt_server.model.LoginState;
import bid.xiaocha.xxt_server.model.LoginState.State;
import bid.xiaocha.xxt_server.repositories.UserRepository;
import bid.xiaocha.xxt_server.utils.CommonUtils;
import bid.xiaocha.xxt_server.utils.RongUtil;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class UserController {
	public static final String HEAD_PATH="";
			//ClassLoader.getSystemResource("").toString().replace("\\", "/").replace("%20", " ").substring(6)+"static/headImg/";  
	@Autowired
	private UserRepository userRepository;
	
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public LoginState login(@RequestParam String userId){

		LoginState loginState = new LoginState();
		
		
//		//执行验证码校检
//		if(checkoutVerificationCode(userId, verificationCode) == false){
//			loginState.setState(State.checkoutError);
//			return loginState;
//		}
		
		System.out.println(userId);
		
		UserEntity userEntity = userRepository.findOne(userId);
		System.out.println(userEntity);
		if(userEntity != null){
			loginState.setRongCloudToken(userEntity.getToken());
			loginState.setState(State.success);
		}else{
			String tokenResult = RongUtil.getToken(userId+"", "nickName", "balalaPicPath");
			System.out.println(tokenResult);
			JSONObject tokenJson = JSON.parseObject(tokenResult);
			if (tokenJson.getInteger("code").equals(200)) {
				userEntity = new UserEntity();
				userEntity.setUserId(userId);
				userEntity.setNickName("nickName");
				userEntity.setPicPath("default_head.png");
				userEntity.setMoney(0);
				userEntity.setSex(UserEntity.SEX_UNDEFINED);
				userEntity.setToken(tokenJson.getString("token"));
				userEntity.setBeHelpedMark(0);
				userEntity.setBeHelpedNumber(0);
				userEntity.setHelpMark(0);
				userEntity.setHelpNumber(0);
				ArrayList<RoleEntity> roles = new ArrayList<RoleEntity>();
				RoleEntity role = new RoleEntity();
				role.setId((long)1);
				role.setRoleName("ROLE_USER");
				roles.add(role);
				userEntity.setRoles(roles);
				userRepository.save(userEntity);
				loginState.setRongCloudToken(userEntity.getToken());
				loginState.setState(State.success);
			}else {
				loginState.setState(State.serverError);
				return loginState;
			}
		}
		
		
		
		JSONObject data = new JSONObject();
		data.put("userId", userId);
		
		JSONArray authArray = new JSONArray();
		for(GrantedAuthority auth : userEntity.getAuthorities()){
			authArray.add(auth.getAuthority());
		}
		
		data.put("auth", authArray);
		
		String jwt = Jwts.builder()
				.setSubject(data.toJSONString())
				.setExpiration(new Date(System.currentTimeMillis() + 60*60 *24 * 1000))
				.signWith(SignatureAlgorithm.HS512, "balala")
				.compact();
		
		
		loginState.setJwt(jwt);
		
		return loginState;
	}
	
	
//	private boolean balala = true;
	
//	@RequestMapping(value="/verificate/{userId}/{verificationCode}", method=RequestMethod.GET)
//	public boolean checkoutVerificationCode(@PathVariable("userId") String userId, 
//			@PathVariable("verificationCode") String verificationCode){
//		balala = !balala;
//		return balala;
//	}
	
	
	@RequestMapping(value="/updateUserInfo", method=RequestMethod.POST)
	public boolean updateUserInfo(@RequestBody UserEntity userEntity){
		try {
			userRepository.saveAndFlush(userEntity);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping(value="/getUserByUserId/{userId}",method=RequestMethod.GET)
	public UserEntity getUserByUserId(@PathVariable("userId") String userId) {
		return userRepository.findOne(userId);
	}
	
	@RequestMapping(value="/getMoneyByUserId",method=RequestMethod.POST)
	public double getMoneyByUserId(@RequestBody String userId) {
	    return userRepository.findMoneyByUserId(userId);
	}
	@RequestMapping(value="/uploadUserPic",method=RequestMethod.POST)
	public String uploadUserPic(@RequestParam String userId,@RequestParam("headPic") MultipartFile headPic) {
		String result = "";
		UserEntity user = userRepository.getOne(userId);
		if (user!=null&&user.getPicPath()!=null) {
			int newVersion = 0;
			String picPath = user.getPicPath();
			if (!picPath.equals("default_head.png")) {
				//TODO：如有必要可在此删除旧图片
				newVersion = Integer.parseInt(picPath.split("_")[1])+1;
			}
			picPath = userId + "_" + newVersion + "_.png";
			user.setPicPath(picPath);
			File pic = new File(HEAD_PATH + picPath);
			try {
				CommonUtils.copyMultipartFile(headPic, pic);
				userRepository.saveAndFlush(user);
				result = newVersion + "";
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return result;
	}
	
}
