package com.moyeota.moyeotaproject.dto.accountdto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@ApiModel(value = "계좌 응답")
public class AccountDto {

	@ApiModelProperty(value = "은행")
	private String bankName;
	@ApiModelProperty(value = "계좌 번호")
	private String accountNumber;

	@Builder
	public AccountDto(String bankName, String accountNumber) {
		this.bankName = bankName;
		this.accountNumber = accountNumber;
	}
}
