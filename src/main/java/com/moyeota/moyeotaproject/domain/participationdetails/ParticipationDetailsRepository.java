package com.moyeota.moyeotaproject.domain.participationdetails;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.users.Users;

public interface ParticipationDetailsRepository extends JpaRepository<ParticipationDetails, Long> {

	List<ParticipationDetails> findByUserOrderByIdDesc(Users user);

	@Query("select p from ParticipationDetails p where p.user = :user and p.status = 'JOIN' order by p.createdDate" )
	List<ParticipationDetails> findMyParticipationDetails(@Param("user") Users users);

	@Query("select p from ParticipationDetails p where p.post.id = :postId and p.status = 'JOIN' order by p.createdDate")
	List<ParticipationDetails> findParticipationDetailsByPostsId(@Param("postId") Long postId);

	@Query("select p from ParticipationDetails p where p.post = :post and p.user = :user and p.status = 'JOIN'")
	Optional<ParticipationDetails> findParticipationDetailsByUserAndPost(@Param("user") Users user,
		@Param("post") Posts post);

}
