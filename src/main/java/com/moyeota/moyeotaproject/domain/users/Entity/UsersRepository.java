package com.moyeota.moyeotaproject.domain.users.Entity;

import com.moyeota.moyeotaproject.domain.users.OAuth.OAuthProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByLoginId(String loginId);
    Optional<Users> findByEmail(String email);
}
