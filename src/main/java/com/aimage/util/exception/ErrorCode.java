package com.aimage.util.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {

    // User
    CONFIRM_PASSWORD("confirmPassword", "비밀번호를 다시 확인해주세요."),
    LOGIN_ERROR("loginError", "이메일 또는 비밀번호가 일치하지 않습니다."),
    PASSWORD_INQUIRE_FAILED("pwInquiry", "계정을 찾을 수 없습니다."),
    USERNAME_UPDATE_FAILED("usernameUpdate", "닉네임이 이전과 같습니다."),
    USER_ALREADY_NOT_EXIST("userNotExist", "이미 존재하지 않는 사용자 입니다."),

    // Image
    IMAGE_SAVE_FAILED("imageSave", "이미지 저장에 실패했습니다."),
    IMAGE_NOT_FOUND("imageNotFound", "이미지를 찾을 수 없습니다."),
    IMAGE_DELETE_FAILED("imageDelete", "이미지를 삭제할 수 없습니다."),
    IMAGE_ALREADY_NOT_EXIST("imageNotExist", "이미 존재하지 않는 이미지입니다.");


    private String field;
    private String message;

    ErrorCode(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
