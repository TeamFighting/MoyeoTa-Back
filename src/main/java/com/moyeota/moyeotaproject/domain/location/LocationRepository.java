package com.moyeota.moyeotaproject.domain.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

	@Query("select l from Location l where l.userId = :userId")
	List<Location> findAllByUserId(@Param("userId") String postId);

	Optional<Location> findTopByUserIdAndPostIdOrderByIdDesc(String userId, String postId);

}
