package com.Safe.Link.exception;

import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler({ IllegalArgumentException.class, RuntimeException.class })
    public ResponseEntity<Map<String, String>> handleTranslatable(RuntimeException ex) {
        Locale locale = LocaleContextHolder.getLocale();
        String key = ex.getMessage();
        String mensaje;
        try {
            mensaje = messageSource.getMessage(key, null, locale);
        } catch (NoSuchMessageException e) {
            mensaje = key;
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", mensaje));
    }
}
