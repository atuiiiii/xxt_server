package bid.xiaocha.xxt_server.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="active_order")
public class ActiveOrderEntity {

	@Id
    private String orderId;//订单号
	@Column(length=20)
	private String serveId;
	@Column(length=20,nullable=false)
    private String servePublisherId;//发行者id
	@Column(length=20,nullable=false)
	private String serveReceiverId;
	@Column(length=140,nullable=false)
    private String content;//服务详情详细信息
	@Column(length=25,nullable=false)
    private String title;//服务标题
	@Column(length=50)
    private String place;//地点
	@Column(length=20,nullable=false)
    private String phone;//电话
	@Column(length=20,nullable=false)
    private Double price;//价格
	@Column(nullable=false)
    private Date buildDate;//订单生成时间
	@Column
    private Date startDate;//订单开始时间
	@Column
    private Date finishDate;//订单结束时间
	@Column(nullable=false)
    private int type;//类型 0.生活类型 1.技术类型 2.其他类型
	@Column(nullable=false)
    private short state;//状态
	@Column(nullable=false)
    private short cancelState;//状态
	@Column(length=140)
	private String refundResult;//退款理由
	@Column(nullable=false)
	private short serveType;//服务对象类型 例如发起者是服务还是被服务的
	
	public static final short STATE_WAIT_AGREE_CREATE = 0;//待接单
	public static final short STATE_BEGIN_SERVE = 1;//已接单，服务可以开始了
	public static final short STATE_WAIT_FOR_CONFIRM = 2;//已完成，待确认完成
	public static final short CANCEL_STATE_NOT_CANCEL = 0;//未取消
	public static final short CANCEL_STATE_PUBLISHER_HAS_CANCEL = 1;//服务发布者已取消
	public static final short CANCEL_STATE_RECEIVER_HAS_CANCEL = 2;//服务接单者已取消
	
	
	
    public static final int TYPE_TECHNOLOGY = 0;
    public static final int TYPE_LIFE = 1;
    public static final int TYPE_OTHERS = 2;
    public static final short NEED_SERVE = 0;
    public static final short OFFER_SERVE = 1;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getServeId() {
		return serveId;
	}
	public void setServeId(String serveId) {
		this.serveId = serveId;
	}
	public String getServePublisherId() {
		return servePublisherId;
	}
	public void setServePublisherId(String servePublisherId) {
		this.servePublisherId = servePublisherId;
	}
	public String getServeReceiverId() {
		return serveReceiverId;
	}
	public void setServeReceiverId(String serveReceiverId) {
		this.serveReceiverId = serveReceiverId;
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
	public Date getBuildDate() {
		return buildDate;
	}
	public void setBuildDate(Date buildDate) {
		this.buildDate = buildDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getFinishDate() {
		return finishDate;
	}
	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public short getState() {
		return state;
	}
	public void setState(short state) {
		this.state = state;
	}
	public String getRefundResult() {
		return refundResult;
	}
	public void setRefundResult(String refundResult) {
		this.refundResult = refundResult;
	}
	public short getServeType() {
		return serveType;
	}
	public void setServeType(short serveType) {
		this.serveType = serveType;
	}
	public short getCancelState() {
		return cancelState;
	}
	public void setCancelState(short cancelState) {
		this.cancelState = cancelState;
	}
    
    
}