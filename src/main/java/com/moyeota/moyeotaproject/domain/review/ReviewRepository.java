package com.moyeota.moyeotaproject.domain.review;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.moyeota.moyeotaproject.domain.users.Users;

public interface ReviewRepository extends JpaRepository<Review, Long> {

	Slice<Review> findByUser(Users user, Pageable pageable);
}
