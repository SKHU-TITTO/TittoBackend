package com.example.titto_backend.matchingBoard.converter;


import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

@Slf4j
public class CategoryToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source) {
        return switch (source) {
            case "STUDY" -> 1;
            case "MENTOR" -> 2;
            case "MENTEE" -> 3;
            case "UHWOOLLEAM" -> 4;
            default -> 0;
        };
    }
}
