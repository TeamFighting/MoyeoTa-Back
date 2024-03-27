package com.moyeota.moyeotaproject.domain.schoolEmailRedis;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;


@RedisHash
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SchoolEmailRedis {
    @Id
    private Long id;

    @Indexed
    private String email;
    private String code;
}
