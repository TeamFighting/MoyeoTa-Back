package com.moyeota.moyeotaproject.domain.oAuth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OAuthRepository extends JpaRepository<OAuth, Long> {

	Optional<OAuth> findByEmailAndName(String email, String name);

	Optional<OAuth> findByUserId(Long userId);
}
