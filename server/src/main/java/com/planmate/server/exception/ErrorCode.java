package com.planmate.server.exception;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;

@Generated
@AllArgsConstructor
@Getter
public enum ErrorCode {
    OK(200, "COMMON_SUCCESS-200", "Successful"),
    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
    EMAIL_DUPLICATION(400,"MEMBER-ERR-400","EMAIL DUPLICATED"),
    NO_MATCHING_CONTENTS(204, "NO-CONTENT_ERR-204", "NO CONTENT");

    private int status;
    private String errorCode;
    private String message;
}
