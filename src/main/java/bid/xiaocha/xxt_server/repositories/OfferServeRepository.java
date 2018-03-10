package bid.xiaocha.xxt_server.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import bid.xiaocha.xxt_server.entities.OfferServeEntity;

public interface OfferServeRepository extends JpaRepository<OfferServeEntity, String>{
	
	List<OfferServeEntity> findByServeIdIn(List<String> serveIdList);

	Page<OfferServeEntity> findByPublishUserIdAndStateNot(String publishUserId ,Pageable pageable , int state);
	Page<OfferServeEntity> findByState(int state ,Pageable pageable);
}
