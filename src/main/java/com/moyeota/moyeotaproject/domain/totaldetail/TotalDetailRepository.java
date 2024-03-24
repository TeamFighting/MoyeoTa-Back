package com.moyeota.moyeotaproject.domain.totaldetail;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TotalDetailRepository extends JpaRepository<TotalDetail, Long> {

	@Query("select t from TotalDetail t where t.post.id = :postId")
	Optional<TotalDetail> findByPostId(@Param("postId") Long postId);

}
