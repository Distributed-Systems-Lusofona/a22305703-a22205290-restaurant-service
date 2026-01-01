package pt.ulusofona.cd.store.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RestaurantNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleRestaurantNotFoundException(
            RestaurantNotFoundException ex,
            HttpServletRequest request
    ) {
        Map<String, Object> error = baseError(HttpStatus.NOT_FOUND, "Restaurant Not Found", ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgumentException(
            IllegalArgumentException ex,
            HttpServletRequest request
    ) {
        Map<String, Object> error = baseError(HttpStatus.BAD_REQUEST, "Bad Request", ex.getMessage(), request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining("; "));

        Map<String, Object> error = baseError(HttpStatus.BAD_REQUEST, "Validation Failed", details, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    private Map<String, Object> baseError(HttpStatus status, String errorName, String message, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("error", errorName);
        error.put("message", message);
        error.put("timestamp", Instant.now());
        error.put("status", status.value());
        error.put("path", request.getRequestURI());
        return error;
    }
}
