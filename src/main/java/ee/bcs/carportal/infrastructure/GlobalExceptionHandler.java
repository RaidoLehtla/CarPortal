package ee.bcs.carportal.infrastructure;

import ee.bcs.carportal.infrastructure.exception.DatabaseConflictException;
import ee.bcs.carportal.infrastructure.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<ApiError> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                request.getRequestURI()

        );
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(DatabaseConflictException.class)
    protected ResponseEntity<ApiError> handleDatabaseNameConflict(DatabaseConflictException ex, HttpServletRequest request) {
        ApiError apiError = new ApiError(
                HttpStatus.CONFLICT,
                HttpStatus.CONFLICT.value(),
                ex.getMessage(),
                request.getRequestURI()
        );
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ApiError> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        ApiError apiError = new ApiError();
        apiError.setStatus(HttpStatus.BAD_REQUEST);
        apiError.setErrorCode(HttpStatus.BAD_REQUEST.value());
        apiError.setMessage(errorMessage);
        apiError.setPath(request.getRequestURI());
        return new ResponseEntity<>(apiError, apiError.getStatus());
    }

}