package bid.xiaocha.xxt_server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import bid.xiaocha.xxt_server.entities.AddressEntity;

public interface AddressRepository extends JpaRepository<AddressEntity, Long>{
	List<AddressEntity> findByUserId(String userId);
	
}
