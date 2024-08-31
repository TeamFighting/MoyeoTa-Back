package com.moyeota.moyeotaproject.domain.posts;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moyeota.moyeotaproject.domain.users.Users;

public interface PostsRepository extends JpaRepository<Posts, Long> {

	List<Posts> findByUser(Users user);

	Slice<Posts> findAllByCategoryAndStatus(Category category, PostsStatus status, Pageable pageable);

	List<Posts> findAllByStatusAndDepartureTimeAfterOrderByCreatedDateDesc(PostsStatus status,
		LocalDateTime departureTime);

	@Modifying
	@Query("update Posts p set p.view = p.view + 1 where p.id = :id")
	int updateView(@Param("id") Long id);

}
