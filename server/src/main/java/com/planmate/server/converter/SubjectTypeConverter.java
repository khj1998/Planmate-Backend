package com.planmate.server.converter;

import com.planmate.server.exception.converter.InvalidSubjectTypeException;
import lombok.Getter;

@Getter
public enum SubjectTypeConverter {
    과목(0L),
    운동(1L),
    기타(2L);

    private final Long value;

    SubjectTypeConverter(Long value) {
        this.value = value;
    }

    public static Long getTypeValue(String inputType) {
        for (SubjectTypeConverter type : SubjectTypeConverter.values()) {
            if (type.name().equals(inputType)) {
                return type.value;
            }
        }
        throw new InvalidSubjectTypeException(inputType);
    }

    public static String getTypeName(Long inputValue) {
        for (SubjectTypeConverter type : SubjectTypeConverter.values()) {
            if (type.getValue().equals(inputValue)) {
                return type.name();
            }
        }
        throw new InvalidSubjectTypeException(inputValue.toString());
    }
}
