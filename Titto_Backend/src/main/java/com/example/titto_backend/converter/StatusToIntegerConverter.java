package com.example.titto_backend.converter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class StatusToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        return switch (source) {
            case "RECRUITING" -> 1;
            case "RECRUITMENT_COMPLETED" -> 2;
            default -> 0;
        };
    }
}