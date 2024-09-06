package wms.order.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import wms.order.dto.CommonResponseDto;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponseDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation error: ", ex);
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()))
                .findFirst()
                .orElse("필수값 오류");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(CommonResponseDto.error("01", errorMessage));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<CommonResponseDto> handleOrderNotFound(OrderNotFoundException ex) {
        String errorMessage = messageSource.getMessage("order.not.found", null, LocaleContextHolder.getLocale()) + " + " + ex.getMessage();
        logger.error("Order not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(CommonResponseDto.error("02", errorMessage));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponseDto> handleIllegalArgument(IllegalArgumentException ex) {
        logger.error("Illegal argument error: ", ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CommonResponseDto.error("03", "잘못된 입력 값입니다: " + ex.getMessage()));
    }

    @ExceptionHandler(FileProcessingException.class)
    public ResponseEntity<CommonResponseDto> handleFileProcessingException(FileProcessingException ex) {
        logger.error("File processing error: ", ex);
        String errorMessage = "파일 처리 중 오류가 발생했습니다. " + ex.getMessage();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(CommonResponseDto.error("99", errorMessage));
    }

    public static class FileProcessingException extends RuntimeException {
        public FileProcessingException(String message) {
            super(message);
        }

        public FileProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static class OrderNotFoundException extends RuntimeException {
        public OrderNotFoundException(String message) {
            super(message);
        }
    }
}
