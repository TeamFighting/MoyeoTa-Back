package com.moyeota.moyeotaproject.domain.schoolEmailRedis;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface SchoolEmailRedisRepository extends CrudRepository<SchoolEmailRedis, String> {
    Optional<SchoolEmailRedis> findByEmail(String email);
}
