package com.moyeota.moyeotaproject.domain.account;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.dto.UsersDto.AccountDto;

public interface AccountRepository extends JpaRepository<Account, Long> {

	@Query("select a from Account a where a.user = :user")
	Optional<Account> findByUser(@Param("user")Users user);

	@Query("select new com.moyeota.moyeotaproject.dto.UsersDto.AccountDto(a.bankName, a.accountNumber) from Account a where a.user = :user")
	List<AccountDto> findAccountsByUser(@Param("user") Users user);

	List<Account> findAllByUser(Users users);

}
