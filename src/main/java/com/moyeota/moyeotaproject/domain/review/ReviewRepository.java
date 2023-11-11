package com.moyeota.moyeotaproject.domain.review;

import com.moyeota.moyeotaproject.domain.users.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Slice<Review> findByUser(Users user, Pageable pageable);
}
