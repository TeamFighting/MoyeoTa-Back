package com.moyeota.moyeotaproject.domain.oAuth;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuthRepository extends JpaRepository<OAuth, Long> {

    Optional<OAuth> findByEmailAndName(String email, String name);
    // Optional<OAuth> findByName(String name);
}
