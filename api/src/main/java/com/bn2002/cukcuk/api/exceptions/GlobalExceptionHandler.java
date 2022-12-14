package com.bn2002.cukcuk.api.exceptions;

import com.bn2002.cukcuk.api.models.ResponseObject;
import com.bn2002.cukcuk.api.models.ValidationObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityExistsException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({ NoSuchElementException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ResponseObject> handleNoSuchElementException(
            NoSuchElementException exception
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseObject("error", exception.getMessage(), ""));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseObject> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        List<ValidationObject> validationObjectList = new ArrayList<ValidationObject>();
        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            validationObjectList.add(new ValidationObject(fieldError.getField(), fieldError.getDefaultMessage()));
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject("error", "Lỗi dữ liệu đầu vào", validationObjectList));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseObject> handleUnwantedException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new ResponseObject("error", "Unknow error", ""));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity<ResponseObject> handleEntityExistsException(EntityExistsException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseObject("error", e.getMessage(), ""));
    }

}
