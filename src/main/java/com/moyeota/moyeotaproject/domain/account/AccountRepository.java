package com.moyeota.moyeotaproject.domain.account;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moyeota.moyeotaproject.domain.users.Users;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select a from Account a where a.user = :user")
	Optional<Account> findByUser(@Param("user")Users user);

}
