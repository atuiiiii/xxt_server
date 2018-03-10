package bid.xiaocha.xxt_server.model;

import java.util.Date;

import bid.xiaocha.xxt_server.entities.OfferServeEntity;

public class CollectResult {

	private Date date;
	private OfferServeEntity offerServeEntity;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public OfferServeEntity getOfferServeEntity() {
		return offerServeEntity;
	}
	public void setOfferServeEntity(OfferServeEntity offerServeEntity) {
		this.offerServeEntity = offerServeEntity;
	}
}
