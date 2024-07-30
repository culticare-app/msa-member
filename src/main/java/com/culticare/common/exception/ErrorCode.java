package com.culticare.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    //공통 예외
    BAD_REQUEST_PARAM(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    BAD_REQUEST_VALIDATION(HttpStatus.BAD_REQUEST, "검증에 실패하였습니다."),

    //회원 예외
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "해당 요청은 로그인이 필요합니다."),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 틀립니다."),
    UNAUTHORIZED_ID(HttpStatus.UNAUTHORIZED, "아이디가 틀립니다."),
    EXIST_LOGIN_ID(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다."),
    EXIST_USER_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    EXIST_USER_NICKNAME(HttpStatus.CONFLICT, "이미 존재하는 닉네임입니다."),
    EXIST_USER_PREFERRED_FACILITY(HttpStatus.CONFLICT, "이미 찜한 시설입니다."),
    SHOULD_CHANGE_PASSWORD(HttpStatus.CONFLICT, "기존 비밀번호와 다르게 입력해주세요."),
    NOT_FOUND_MEMBER(HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
    NOT_FOUND_ARTIST(HttpStatus.NOT_FOUND, "해당 작가를 찾을 수 없습니다."),
    NOT_FOUND_EMAIL(HttpStatus.NOT_FOUND, "가입되지 않은 이메일 입니다."),
    NOT_FOUND_KEYWORD(HttpStatus.NOT_FOUND, "해당 키워드를 찾을 수 없습니다."),
    OVER_PREFERRED_ART_WORK_MAXIMUM(HttpStatus.BAD_REQUEST, "관심 작품은 최대 100개까지 등록할 수 있습니다."),
    OVER_PREFERRED_ARTIST_MAXIMUM(HttpStatus.BAD_REQUEST, "픽 작가는 최대 100개까지만 등록할 수 있습니다."),
    NOT_FOUND_ASK(HttpStatus.NOT_FOUND, "해당 문의 글이 존재하지 않습니다."),
    PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "타인의 글은 수정 및 삭제할 수 없습니다"),
    NOT_FOUND_SEARCH_WORD(HttpStatus.NOT_FOUND,"해당 최근 검색어가 존재하지 않습니다."),

    //친구 예외
    NOT_FOUND_FRIENDSHIP(HttpStatus.NOT_FOUND,"해당 친구관계 정보가 존재하지 않습니다."),
    EXIST_FRIENDSHIP(HttpStatus.CONFLICT, "이미 존재하는 친구요청입니다."),


    //sms 인증 예외
    SMS_CERTIFICATION_NUMBER_MISMATCH_EXCEPTION(HttpStatus.UNAUTHORIZED, "인증번호가 일치하지 않습니다."),

    //시설 예외
    NOT_FOUND_FACILITY(HttpStatus.NOT_FOUND, "해당 시설을 찾을 수 없습니다."),

    //토큰 예외
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),


    //경로 예외
    NOT_VALID_URI(HttpStatus.BAD_REQUEST, "유효한 경로로 요청해주세요."),

    //게시글 예외
    NOT_FOUND_POST(HttpStatus.NOT_FOUND, "해당 게시글이 존재하지 않습니다."),

    // 카테고리 예외
    NOT_FOUND_CATEGORY(HttpStatus.NOT_FOUND, "해당 카테고리가 존재하지 않습니다."),

    // 댓글 예외
    NOT_FOUND_COMMENT(HttpStatus.NOT_FOUND, "해당 댓글이 존재하지 않습니다."),

    // 좋아요 예외
    EXIST_USER_LIKED_POST(HttpStatus.CONFLICT, "이미 존재하는 좋아요 정보입니다."),
    NOT_FOUND_MEMBER_LIKE_POSTS(HttpStatus.NOT_FOUND, "해당 좋아요 정보가 존재하지 않습니다.");

    private final HttpStatus httpStatus;
    private final String detail;
}
