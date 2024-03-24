package com.moyeota.moyeotaproject.domain.schoolEmail;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolEmailRepository extends JpaRepository<SchoolEmail, Long> {
	Optional<SchoolEmail> findByEmail(String schoolEmail);
}
