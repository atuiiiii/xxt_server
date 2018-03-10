package bid.xiaocha.xxt_server.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "offer_serve")
public class OfferServeEntity {
	@Id
    private String serveId;//服务号
	@Column(length=20,nullable=false)
    private String publishUserId;//发行者id
	@Column(length=140,nullable=false)
    private String content;//服务详情详细信息
	@Column(length=25,nullable=false)
    private String title;//服务标题
	@Column(length=20,nullable=false)
	private String userName;//“收货人”姓名
	@Column(length=50)
    private String place;//地点
	@Column(length=20,nullable=false)
    private String phone;//电话
	@Column(length=20,nullable=false)
    private Double price;//价格
	@Column(nullable=false)
    private Date publishdate;//发布时间
	@Column(nullable=false)
    private int type;//类型 0.生活类型 1.技术类型 2.其他类型
	@Column(nullable=false)
    private int state;//状态
	@Column(nullable=false)
    private int scoreNum;
	@Column(nullable=false)
    private double score;
	@Column(nullable=false)
    private double longitude;//经度
	@Column(nullable=false)
    private double latitude;//纬度
    public static final int TYPE_TECHNOLOGY = 0;
    public static final int TYPE_LIFE = 1;
    public static final int TYPE_OTHERS = 2;
	public static final int STOP_SERVE = 0;
	public static final int START_SERVE = 1;
	public static final int DELECT_SERVE = 2;
	public String getServeId() {
		return serveId;
	}
	public void setServeId(String serveId) {
		this.serveId = serveId;
	}
	public String getPublishUserId() {
		return publishUserId;
	}
	public void setPublishUserId(String publishUserId) {
		this.publishUserId = publishUserId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPlace() {
		return place;
	}
	public void setPlace(String place) {
		this.place = place;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Date getPublishdate() {
		return publishdate;
	}
	public void setPublishdate(Date publishdate) {
		this.publishdate = publishdate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getScoreNum() {
		return scoreNum;
	}
	public void setScoreNum(int scoreNum) {
		this.scoreNum = scoreNum;
	}
	public double getScore() {
		return score;
	}
	public void setScore(double score) {
		this.score = score;
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
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
}
