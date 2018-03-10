package bid.xiaocha.xxt_server.entities;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="address")
public class AddressEntity {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long addressId;
	@Column(nullable=false)
	private String userId;
	@Column(nullable=false)
	private String userName;//“收货人”姓名
	@Column(length=20,nullable=false)
	private String phone;
	@Column(length=50)
	private String place;
	@Column(nullable=false)
    private double longitude;//经度
	@Column(nullable=false)
    private double latitude;//纬度
	
	
	public long getAddressId() {
		return addressId;
	}
	public void setAddressId(long addressId) {
		this.addressId = addressId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
}
