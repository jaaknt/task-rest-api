package com.multimarketing.taskrestapi.exception;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ErrorResponse {
    private String error;
}
