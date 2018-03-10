package bid.xiaocha.xxt_server.model;

import java.util.List;

import bid.xiaocha.xxt_server.entities.NeedServeEntity;
import bid.xiaocha.xxt_server.entities.OfferServeEntity;

public class GetResultByPage<T> {
	private List<T> dataList;
	private boolean isHaveMore;
	public List<T> getDataList() {
		return dataList;
	}
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	public boolean isHaveMore() {
		return isHaveMore;
	}
	public void setHaveMore(boolean isHaveMore) {
		this.isHaveMore = isHaveMore;
	}
	
}
