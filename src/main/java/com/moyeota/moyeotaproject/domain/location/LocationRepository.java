package com.moyeota.moyeotaproject.domain.location;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LocationRepository extends JpaRepository<Location, Long> {

	@Query("select l from Location l where l.sessionId = :sessionId order by l.id desc")
	List<Location> findAllBySessionId(@Param("sessionId") String sessionId);

}
