package com.moyeota.moyeotaproject.domain.review;

import com.moyeota.moyeotaproject.domain.users.Users;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("select r from Review r order by r.id desc")
    List<Review> findAllDesc();

    Slice<Review> findByUser(Users user, Pageable pageable);
}
