package com.moyeota.moyeotaproject.domain.users.Entity;

import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByLoginId(String loginId);
    Optional<Users> findByEmail(String email);

    @Query("select u.name from Users u where u.id = :userId")
    String findNameByUserId(@Param("userId") Long userId);
}
