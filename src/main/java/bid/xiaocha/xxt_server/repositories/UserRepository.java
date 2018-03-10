package bid.xiaocha.xxt_server.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import bid.xiaocha.xxt_server.entities.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, String>{
	@Override
	UserEntity findOne(String id);
	double findMoneyByUserId(String id);
}
