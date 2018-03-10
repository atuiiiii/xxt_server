package bid.xiaocha.xxt_server.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import bid.xiaocha.xxt_server.entities.NeedServeEntity;
import bid.xiaocha.xxt_server.entities.OfferServeEntity;

public interface NeedServeRepository extends JpaRepository<NeedServeEntity, String>{
	Page<NeedServeEntity> findByPublishUserId(String publishUserId ,Pageable pageable);
	Page<NeedServeEntity> findByState(int state ,Pageable pageable);
}
