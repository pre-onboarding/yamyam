package com.wanted.yamyam.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    SERVER_INTERNAL_ERROR(HttpStatus.BAD_REQUEST, "서버 내부적인 에러"),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "중복된 이메일이 있습니다."),
    DUPLICATE_USERNAME(HttpStatus.CONFLICT, "중복된 유저네임이 있습니다."),
    DUPLICATE_REVIEW(HttpStatus.CONFLICT, "이미 작성한 리뷰가 있습니다."),
    NON_EXISTENT_MEMBER(HttpStatus.BAD_REQUEST, "존재하지 않는 멤버입니다."),
    NON_EXISTENT_STORE(HttpStatus.BAD_REQUEST, "존재하지 않는 맛집입니다."),
    WRONG_PASSWORD(HttpStatus.BAD_REQUEST, "올바르지 않는 비밀번호입니다."),
    WRONG_USERNAME(HttpStatus.BAD_REQUEST, "올바르지 않는 유저네임입니다."),
    LAT_LON_NO_VALUE(HttpStatus.BAD_REQUEST, "lat과lon값을 입력해주세요."),
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료 되었습니다."),
    INVALID_JWT(HttpStatus.UNAUTHORIZED, "잘못된 JWT 입니다."),
    MISSING_JWT(HttpStatus.NOT_FOUND, "JWT가 없습니다."),
    INVALID_FILE_LIST(HttpStatus.BAD_REQUEST,"유효하지 않는 JSON 파일입니다."),
    OPENAPI_ERROR(HttpStatus.BAD_REQUEST,"오픈 api 데이터에서 오류가 발생했습니다");

    private final HttpStatus httpStatus;
    private final String message;

}
