package com.jorder.agora.infra;

import com.jorder.agora.exceptions.BusinessException;
import com.jorder.agora.exceptions.ForbiddenActionException;
import com.jorder.agora.exceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    private ResponseEntity<RestErrorMessage> handleNotFound(ResourceNotFoundException e) {
        RestErrorMessage error = new RestErrorMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(BusinessException.class)
    private ResponseEntity<RestErrorMessage> handleBusinessError(BusinessException e) {
        RestErrorMessage error = new RestErrorMessage(HttpStatus.CONFLICT.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    @ExceptionHandler(ForbiddenActionException.class)
    private ResponseEntity<RestErrorMessage> handleForbidden(ForbiddenActionException e) {
        RestErrorMessage error = new RestErrorMessage(HttpStatus.FORBIDDEN.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
    }

    @ExceptionHandler(jakarta.persistence.EntityNotFoundException.class)
    private ResponseEntity<RestErrorMessage> handleEntityNotFound(jakarta.persistence.EntityNotFoundException e) {
        RestErrorMessage error = new RestErrorMessage(HttpStatus.NOT_FOUND.value(), e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

}
