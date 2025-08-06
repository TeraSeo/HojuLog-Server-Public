package com.hojunara.web.handler;

import com.hojunara.web.dto.response.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleGeneralException(Exception ex) {
        ex.printStackTrace();

        ApiError error = new ApiError("INTERNAL_SERVER_ERROR", "An unexpected error occurred.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllExceptions(Exception ex) {
        StackTraceElement origin = ex.getStackTrace()[0];
        String className = origin.getClassName();
        String methodName = origin.getMethodName();
        int lineNumber = origin.getLineNumber();

        log.error("Exception occurred in {}.{} (line {}): {}", className, methodName, lineNumber, ex.getMessage(), ex);

        ApiError error = new ApiError("INTERNAL_ERROR", "Something went wrong.");
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}