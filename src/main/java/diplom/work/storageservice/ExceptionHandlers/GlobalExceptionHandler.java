package diplom.work.storageservice.ExceptionHandlers;

import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.common.protocol.Errors;
import org.apache.kafka.common.requests.ApiError;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@ControllerAdvice
//public class GlobalExceptionHandler {
//    @ExceptionHandler(EntityNotFoundException.class)
//    public ResponseEntity<ApiError> onNotFound(EntityNotFoundException ex) {
//        return ResponseEntity.status(404)
//                .body(new ApiError(Errors.valueOf("Not found"), ex.getMessage()));
//    }
//}
