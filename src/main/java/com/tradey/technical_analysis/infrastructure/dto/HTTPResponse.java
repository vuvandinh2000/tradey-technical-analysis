package com.tradey.technical_analysis.infrastructure.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class HTTPResponse {
    private final int status_code;
    private final String message;
    private final Map<String, Object> data;
    private final String timestamp;
}
