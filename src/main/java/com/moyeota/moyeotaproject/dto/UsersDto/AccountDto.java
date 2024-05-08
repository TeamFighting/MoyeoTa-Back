package com.moyeota.moyeotaproject.dto.UsersDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class AccountDto {

    private String bankName;
    private String accountNumber;
}
