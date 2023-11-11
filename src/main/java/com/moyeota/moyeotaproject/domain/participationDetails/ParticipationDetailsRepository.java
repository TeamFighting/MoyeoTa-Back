package com.moyeota.moyeotaproject.domain.participationDetails;

import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParticipationDetailsRepository extends JpaRepository<ParticipationDetails, Long> {

    List<ParticipationDetails> findByUserOrderByIdDesc(Users user);

    @Query("select p from ParticipationDetails p where p.post.id = :postId and p.status = 'JOIN' order by p.createdDate")
    List<ParticipationDetails> findParticipationDetailsByPostsId(@Param("postId") Long postId);

    @Query("select p from ParticipationDetails p where p.post = :post and p.user = :user and p.status = 'JOIN'")
    ParticipationDetails findParticipationDetailsByUserAndPost(@Param("user") Users user, @Param("post") Posts post);

}
