package com.planmate.server.exception.planner;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(HttpStatus.NOT_FOUND)
public class PlannerNotFoundException extends RuntimeException {
    private String message;

    public PlannerNotFoundException(Long id) {
        super(id.toString());
        this.message = id.toString();
    }
}
