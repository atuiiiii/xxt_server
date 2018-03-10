package bid.xiaocha.xxt_server.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import bid.xiaocha.xxt_server.entities.ActiveOrderEntity;
import bid.xiaocha.xxt_server.entities.FinishedOrderEntity;
import bid.xiaocha.xxt_server.entities.UserEntity;

public interface ActiveOrderRepository extends JpaRepository<ActiveOrderEntity, String>{
	
	
	Page<ActiveOrderEntity> findByServePublisherIdOrServeReceiverId(String servePublishedId,String serveReceiverId,Pageable pageable);
	default Page<ActiveOrderEntity> findMyActiveOrder(String userId,Pageable pageable){
		return findByServePublisherIdOrServeReceiverId(userId,userId,pageable);
	}
	
	ActiveOrderEntity findByServeId(String serveId);
}
