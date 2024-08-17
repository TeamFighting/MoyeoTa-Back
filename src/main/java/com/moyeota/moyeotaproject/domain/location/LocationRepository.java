package com.moyeota.moyeotaproject.domain.location;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moyeota.moyeotaproject.dto.locationdto.LocationDto;

public interface LocationRepository extends JpaRepository<Location, Long> {

	@Query("select l.position from Location l where l.userId = :userId and l.postId = :postId order by l.id desc")
	List<LocationDto> findAllByUserAndPost(@Param("userId") Long userId, @Param("postId") Long postId);

	Optional<Location> findTopByUserIdAndPostIdOrderByIdDesc(String userId, String postId);

}
