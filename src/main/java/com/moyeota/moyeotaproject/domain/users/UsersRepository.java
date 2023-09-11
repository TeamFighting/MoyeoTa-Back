package com.moyeota.moyeotaproject.domain.users;

import com.moyeota.moyeotaproject.domain.users.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByLoginId(String loginId);
    Optional<Users> findByEmail(String email);

    @Query("select u.name from Users u where u.id = :userId")
    String findNameByUserId(@Param("userId") Long userId);
}
