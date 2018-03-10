package bid.xiaocha.xxt_server.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import bid.xiaocha.xxt_server.entities.CollectOfferServeEntity;




public interface CollectOfferServeRepository extends JpaRepository<CollectOfferServeEntity, String>{
	
	Page<CollectOfferServeEntity> findByUserIdByPage(String userId ,Pageable pageable );
	CollectOfferServeEntity findByUserIdAndServeId(String userId, String serveId);
	
	
}
