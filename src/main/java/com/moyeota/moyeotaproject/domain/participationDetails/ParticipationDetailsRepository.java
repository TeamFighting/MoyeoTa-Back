package com.moyeota.moyeotaproject.domain.participationDetails;

import com.moyeota.moyeotaproject.domain.posts.Posts;
import com.moyeota.moyeotaproject.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationDetailsRepository extends JpaRepository<ParticipationDetails, Long> {

    ParticipationDetails findByUserAndPost(Users user, Posts post);

}
