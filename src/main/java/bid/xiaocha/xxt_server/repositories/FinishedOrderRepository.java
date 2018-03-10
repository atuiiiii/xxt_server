package bid.xiaocha.xxt_server.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import bid.xiaocha.xxt_server.entities.ActiveOrderEntity;
import bid.xiaocha.xxt_server.entities.FinishedOrderEntity;
import bid.xiaocha.xxt_server.entities.OfferServeEntity;

public interface FinishedOrderRepository extends JpaRepository<FinishedOrderEntity, String>{
	Page<FinishedOrderEntity> findByServePublisherIdOrServeReceiverId(String servePublishedId,String serveReceiverId,Pageable pageable);
	default Page<FinishedOrderEntity> findMyFinishedOrder(String userId,Pageable pageable){
		return findByServePublisherIdOrServeReceiverId(userId,userId,pageable);
	}
	

	FinishedOrderEntity findByServeId(String serveId);
}
