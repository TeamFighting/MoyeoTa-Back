package com.moyeota.moyeotaproject.dto.UsersDto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountDto {

    private String bankName;
    private String accountNumber;
}
