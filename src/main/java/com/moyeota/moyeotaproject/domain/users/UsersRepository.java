package com.moyeota.moyeotaproject.domain.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsersRepository extends JpaRepository<Users, Long> {
	Boolean existsByLoginId(String loginId);

	Optional<Users> findByEmail(String email);

	Optional<Users> findByProfileImage(String imageUrl);
}
