package com.tradey.technical_analysis.infrastructure.dto;

import lombok.RequiredArgsConstructor;
import java.util.Map;

@RequiredArgsConstructor
public class HTTPResponse {
    private final int status_code;
    private final String message;
    private final Map<String, Object> data;
    private final String timestamp;
}
