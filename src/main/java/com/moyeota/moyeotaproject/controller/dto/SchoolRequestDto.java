package com.moyeota.moyeotaproject.controller.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SchoolRequestDto {
    private String key;
    private String email;
    private String univName;
    private boolean univ_check;
    private int code;
}
