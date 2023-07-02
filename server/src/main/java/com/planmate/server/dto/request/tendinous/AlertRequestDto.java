package com.planmate.server.dto.request.tendinous;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
public class AlertRequestDto {
    private String title;
    private String body;
    private String tag;

    @Override
    public String toString() {
        LocalDateTime now = LocalDateTime.now();

        return new String(
                now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")) +
                "\n{\n" + "\ttag: " + tag + "\n\ttitle: " + title + "\n"
                        + "\tbody: " + body + "\n}");
    }
}
