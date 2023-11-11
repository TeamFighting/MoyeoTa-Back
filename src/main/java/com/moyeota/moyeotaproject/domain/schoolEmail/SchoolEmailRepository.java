package com.moyeota.moyeotaproject.domain.schoolEmail;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolEmailRepository extends JpaRepository<SchoolEmail, Long> {
    Optional<SchoolEmail> findByEmail(String schoolEmail);
}
