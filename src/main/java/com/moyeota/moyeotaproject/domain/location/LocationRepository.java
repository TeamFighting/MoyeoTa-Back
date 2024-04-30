package com.moyeota.moyeotaproject.domain.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

	@Query("select l from Location l where l.sessionId = :sessionId order by l.id desc")
	List<Location> findAllBySessionId(@Param("sessionId") String sessionId);

	@Query("select l from Location l where l.userId = :userId")
	List<Location> findAllByUserId(@Param("userId") String postId);

	@Query("select l from Location l where l.postId = :postId and l.userId = :userId order by l.id desc")
	Optional<Location> findLocation(@Param("postId") String postId, @Param("userId") String userId);

	Optional<Location> findTopByUserIdAndPostIdOrderByIdDesc(String userId, String postId);

}
