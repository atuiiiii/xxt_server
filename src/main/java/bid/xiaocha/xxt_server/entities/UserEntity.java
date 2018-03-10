package bid.xiaocha.xxt_server.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;



@Entity
@Table(name="user")
public class UserEntity implements UserDetails{
	@Id	
	@Column(name="user_id", length=20)
	private String userId;
	
	@Column(name="nick_name", length=20)
	private String nickName;
	
	
	public static final short SEX_UNDEFINED = 0;
	public static final short SEX_MAN = 1;
	public static final short SEX_WOMAN = 2;
	@Column(name="sex")
	private short sex;
	
	
	
    @ManyToMany(cascade = {CascadeType.REFRESH},fetch = FetchType.EAGER)
    private List<RoleEntity> roles;
	
	@Column(name="money", length=20, nullable=false)
	private double money;
	
	
	//userId + _0_ + 文件后缀
	@Column(name="pic_path", length=200, unique=true)
	private String picPath;
	
	@Column(name="token", length=200, nullable=false, unique=true)
	private String token;
	
	@Column(nullable=false)
    private int beHelpedNumber;//被服务时，评论人数
	@Column(nullable=false)
	private int helpNumber;//服务他人时，评论人数
	@Column(nullable=false)
    private double beHelpedMark;//被服务时，评论分数
	@Column(nullable=false)
    private double helpMark;//服务他人时，评论分数
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}


	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public String getPicPath() {
		return picPath;
	}

	public short getSex() {
		return sex;
	}

	public void setSex(short sex) {
		this.sex = sex;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}



	public double getBeHelpedMark() {
		return beHelpedMark;
	}

	public void setBeHelpedMark(double beHelpedMark) {
		this.beHelpedMark = beHelpedMark;
	}

	public int getBeHelpedNumber() {
		return beHelpedNumber;
	}

	public void setBeHelpedNumber(int beHelpedNumber) {
		this.beHelpedNumber = beHelpedNumber;
	}

	public int getHelpNumber() {
		return helpNumber;
	}

	public void setHelpNumber(int helpNumber) {
		this.helpNumber = helpNumber;
	}

	public double getHelpMark() {
		return helpMark;
	}

	public void setHelpMark(double helpMark) {
		this.helpMark = helpMark;
	}

	
	
	
	
	
	
	public void setRoles(List<RoleEntity> roles) {
		this.roles = roles;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> auths = new ArrayList<GrantedAuthority>();
        for (RoleEntity role : roles) {
            auths.add(new SimpleGrantedAuthority(role.getRoleName()));
        }
        
        return auths;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return userId;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
}
