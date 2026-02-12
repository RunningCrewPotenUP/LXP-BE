package com.lxpbe.common.response;

import com.lxpbe.common.exception.ErrorBody;

public record ApiResponse<T>(
        T data,
        ErrorBody error
) {

    public ApiResponse {
        boolean isBothNull = (data == null && error == null);
        boolean isBothNotNull = (data != null && error != null);

        if (isBothNull || isBothNotNull) {
            throw new IllegalArgumentException("data 와 error 는 동시에 null 이거나 not null 일 수 없습니다.");
        }
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null);
    }
}
