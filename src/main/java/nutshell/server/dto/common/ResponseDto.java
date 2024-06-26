package nutshell.server.dto.common;

import nutshell.server.exception.code.DefaultErrorCode;

public record ResponseDto<T> (
        String code,
        T data,
        String message
) {
    public static <T> ResponseDto<T> success(final T data) {
        return new ResponseDto<>("success", data, null);
    }

    public static <T> ResponseDto<T> fail(DefaultErrorCode code) {
        return new ResponseDto<>(code.getCode(), null, code.getMessage());
    }
}