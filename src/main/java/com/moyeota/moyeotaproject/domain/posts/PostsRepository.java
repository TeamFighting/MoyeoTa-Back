package com.moyeota.moyeotaproject.domain.posts;

import com.moyeota.moyeotaproject.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("select p from Posts p order by p.createdDate desc")
    List<Posts> findAllDesc();

    List<Posts> findByUserOrderByCreatedDateDesc(Users user);

    @Query("select p from Posts p where p.category = :category order by p.createdDate desc")
    List<Posts> findByCategoryOrderByIdDesc(@Param("category") Category category);

}
