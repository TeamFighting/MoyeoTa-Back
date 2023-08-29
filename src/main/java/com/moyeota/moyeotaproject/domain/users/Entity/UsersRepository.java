package com.moyeota.moyeotaproject.domain.users.Entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Boolean existsByLoginId(String loginId);
    Optional<Users> findByLoginId(String loginId);
    Optional<Users> findByEmailAndProvider(String email, String provider);

    Optional<Users> findByEmail(String email);
}
