package bid.xiaocha.xxt_server.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import bid.xiaocha.xxt_server.entities.CommentEntity;

public interface CommentRepository extends JpaRepository<CommentEntity, Integer>{

}
