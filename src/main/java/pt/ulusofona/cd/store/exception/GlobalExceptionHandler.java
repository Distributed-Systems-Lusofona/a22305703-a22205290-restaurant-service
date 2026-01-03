package pt.ulusofona.cd.store.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String TRACE_HEADER = "X-Trace-Id";

    @ExceptionHandler({
            RestaurantNotFoundException.class,
            MenuItemNotFoundException.class,
            AvailabilitySlotNotFoundException.class
    })
    public ResponseEntity<Map<String, Object>> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error("NOT_FOUND", ex.getMessage(), traceId(request)));
    }

    @ExceptionHandler(OverlappingAvailabilitySlotException.class)
    public ResponseEntity<Map<String, Object>> handleOverlap(OverlappingAvailabilitySlotException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error("CONFLICT", ex.getMessage(), traceId(request)));
    }

    @ExceptionHandler({DomainValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, Object>> handleUnprocessable(Exception ex, HttpServletRequest request) {
        String msg;
        if (ex instanceof MethodArgumentNotValidException manv) {
            msg = manv.getBindingResult().getFieldErrors().stream()
                    .map(err -> err.getField() + ": " + err.getDefaultMessage())
                    .collect(Collectors.joining("; "));
        } else {
            msg = ex.getMessage();
        }
        return ResponseEntity.status(422).body(error("VALIDATION_ERROR", msg, traceId(request)));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error("BAD_REQUEST", ex.getMessage(), traceId(request)));
    }

    private Map<String, Object> error(String code, String message, String traceId) {
        return Map.of(
                "error", code,
                "message", message,
                "traceId", traceId
        );
    }

    private String traceId(HttpServletRequest request) {
        String h = request.getHeader(TRACE_HEADER);
        return (h != null && !h.isBlank()) ? h : UUID.randomUUID().toString();
    }
}
