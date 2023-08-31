package com.moyeota.moyeotaproject.config.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "com.moyeota.moyeotaproject.controller")
public class ApiExceptionController {

//    @ExceptionHandler(RuntimeException.class)
//    protected Response<ErrorResponse> handleRuntimeException(ApiException apiException) {
//        return Response.of(
//                apiException.getStatus(),
//                ErrorResponse.of(apiException.getErrorCode()));
//    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> runtimeExHandler(ApiException e){
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getErrorCode().getCode())
                .status(e.getErrorCode().getStatus())
                .message(e.getErrorCode().getMessage()).build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResponse illegalExHandler(IllegalArgumentException e) {
        return new ErrorResponse(e.getMessage(), 500, 500);
    }

}
