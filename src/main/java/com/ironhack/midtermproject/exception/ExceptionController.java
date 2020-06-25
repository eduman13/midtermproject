package com.ironhack.midtermproject.exception;

import com.ironhack.midtermproject.model.Checking;
import com.ironhack.midtermproject.model.enums.Status;
import com.ironhack.midtermproject.repository.CheckingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Optional;

@ControllerAdvice
public class ExceptionController {

    @Autowired
    CheckingRepository checkingRepository;

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Error> handleMethodArgumentNotValid(Exception e) {
        String errorMessage = e.toString().substring(e.toString().length() - 50);
        Error error = new Error();
            error.setMessage(errorMessage.substring(errorMessage.indexOf("[") + 1, errorMessage.indexOf("]")));
            error.setStatus(HttpStatus.BAD_REQUEST.toString());
            error.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(IdNotFoundException.class)
//    public ResponseEntity<Error> handleObjectNotFound(IdNotFoundException e) {
//        Error error = new Error();
//            error.setMessage(e.getMessage());
//            error.setStatus(HttpStatus.NOT_FOUND.toString());
//            error.setLocalDateTime(LocalDateTime.now());
//        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
//    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Error> handleRunTimeException(RuntimeException e) {
        if (e.getMessage().contains("Fraud")) {
            Optional<Checking> checkingOptional = checkingRepository.findById(Integer.parseInt(e.getMessage().substring(e.getMessage().lastIndexOf(" ") + 1)));
            checkingOptional.get().setStatus(Status.FROZEN);
            checkingRepository.save(checkingOptional.get());
        }
        Error error = new Error();
            error.setMessage(e.getMessage());
            error.setStatus((e.getMessage().contains("does not") ? HttpStatus.NOT_FOUND.toString() :
                    e.getMessage().contains("Access denied") ? HttpStatus.FORBIDDEN.toString() : HttpStatus.BAD_REQUEST.toString()));
            error.setLocalDateTime(LocalDateTime.now());
        return new ResponseEntity<>(error, e.getMessage().contains("does not") ? HttpStatus.NOT_FOUND :
                e.getMessage().contains("Access denied") ? HttpStatus.FORBIDDEN : HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(InsufficientQuantityException.class)
//    public ResponseEntity<Error> handleFraudDetectionException(InsufficientQuantityException e) {
//        Error error = new Error();
//            error.setMessage(e.getMessage());
//            error.setStatus(HttpStatus.BAD_REQUEST.toString());
//            error.setLocalDateTime(LocalDateTime.now());
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//    }
}
