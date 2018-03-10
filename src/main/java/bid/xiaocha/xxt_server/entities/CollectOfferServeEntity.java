package bid.xiaocha.xxt_server.entities;



import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="collect_offer_serve")
public class CollectOfferServeEntity {

	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int collectId;
	
	@Column(nullable=false,name="user_id", length=20)
	private String userId;
	
	@Column(nullable=false,name="serve_id")
	private String serveId;
	
	@Column(nullable=false,name="collect_date")
	private Date collectDate;
	
	public int getCollectId() {
		return collectId;
	}

	public void setCollectId(int collectId) {
		this.collectId = collectId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getServeId() {
		return serveId;
	}

	public void setServeId(String serveId) {
		this.serveId = serveId;
	}

	public Date getCollectDate() {
		return collectDate;
	}

	public void setCollectDate(Date collectDate) {
		this.collectDate = collectDate;
	}
}
