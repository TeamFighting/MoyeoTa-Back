package com.moyeota.moyeotaproject.config.response;

import com.moyeota.moyeotaproject.config.response.ResponseDto;
import com.moyeota.moyeotaproject.config.response.ResponseStatus;
import com.moyeota.moyeotaproject.controller.dto.reviewDto.ReviewResponseDto;
import org.springframework.data.domain.Slice;

public class ResponseUtil {

    public static <T> ResponseDto<T> SUCCESS (String message, T data) {
        return new ResponseDto(ResponseStatus.SUCCESS, message, data);
    }

    public static <T>ResponseDto<T> FAILURE (String message, T data) {
        return new ResponseDto(ResponseStatus.FAILURE, message, data);
    }

    public static <T>ResponseDto<T> ERROR (String message, T data) {
        return new ResponseDto(ResponseStatus.ERROR, message, data);
    }

}
