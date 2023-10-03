package com.moyeota.moyeotaproject.domain.posts;

import com.moyeota.moyeotaproject.domain.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    Slice<Posts> findByUser(Users user, Pageable pageable);

    @Query("select p from Posts p where p.category = :category and p.status = :status")
    Slice<Posts> findByCategory(@Param("category") Category category, @Param("status") PostsStatus status, Pageable pageable);


    @Query("select p from Posts p where p.status = :status")
    Slice<Posts> findAllByStatus(Pageable pageable, @Param("status") PostsStatus recruiting);
    
}
