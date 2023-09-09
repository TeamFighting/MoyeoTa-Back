package com.moyeota.moyeotaproject.domain.posts;

import com.moyeota.moyeotaproject.domain.users.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostsRepository extends JpaRepository<Posts, Long> {

    @Query("select p from Posts p order by p.id desc")
    List<Posts> findAllDesc();

    List<Posts> findByUserOrderByIdDesc(Users user);

    @Query("select p from Posts p where p.category = :category order by p.id desc")
    List<Posts> findByCategoryOrderByIdDesc(@Param("category") Category category);

}
