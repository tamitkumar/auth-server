package com.org.auth.exception;

import static com.org.auth.utils.AuthConstant.HTTP_CODE_500;
import static com.org.auth.utils.AuthConstant.HYPHEN;

public class AuthException extends RuntimeException {
    private static final long serialVersionUID = -4711404730678356597L;

    private String code;
    private String message;

    public AuthException(Exception exception) {
        super(exception);
        StringBuilder builder = new StringBuilder(ErrorCode.HTTP_CODE_500.getErrorCode()).append(HYPHEN)
                .append(ErrorSeverity.FATAL).append(HYPHEN).append(ErrorCode.HTTP_CODE_500.getErrorMessage());
        this.code = HTTP_CODE_500;
        this.message = builder.toString();
    }

    public AuthException(String code, String message) {
        super(message);
        StringBuilder builder = new StringBuilder(code).append(HYPHEN).append(message);
        this.code = code;
        this.message = builder.toString();
    }

    public AuthException(String code, ErrorSeverity severity, String message) {
        super(message);
        StringBuilder builder = new StringBuilder(code).append(HYPHEN).append(severity).append(HYPHEN).append(message);
        this.code = code;
        this.message = builder.toString();
    }

    public AuthException(String code, ErrorSeverity severity, String message, Exception exception) {
        super(exception);
        StringBuilder builder = new StringBuilder(code).append(HYPHEN).append(severity).append(HYPHEN).append(message);
        this.code = code;
        this.message = builder.toString();
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
