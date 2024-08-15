package com.moyeota.moyeotaproject.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moyeota.moyeotaproject.domain.account.AccountRepository;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.dto.UsersDto.AccountDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Transactional
@Service
@Slf4j
public class AccountService {

	private final AccountRepository accountRepository;
	private final UsersService usersService;

	@Transactional(readOnly = true)
	public List<AccountDto> findAllByUser(String accessToken) {
		Users user = usersService.getUserByToken(accessToken);
		List<AccountDto> accounts = accountRepository.findAccountsByUser(user);
		return accounts;
	}

	public String delete(String accessToken, Long accountId) {
		Users user = usersService.getUserByToken(accessToken);
		accountRepository.deleteById(accountId);
		return accessToken + "번 계좌 삭제 완료";
	}

}
