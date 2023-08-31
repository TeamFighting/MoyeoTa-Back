package com.moyeota.moyeotaproject.domain.participationDetails;

import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationDetailsRepository extends JpaRepository<ParticipationDetails, Long> {

    ParticipationDetails findByUserAndPost(Users user, Posts post);

//    @Query("select p from ParticipationDetails p order by p.id desc ")
//    List<ParticipationDetails> findAllDesc();

    List<ParticipationDetails> findByUserOrderByIdDesc(Users user);

}
