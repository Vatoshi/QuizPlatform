package kg.attractor.quizplatform.controler;

import kg.attractor.quizplatform.exeptions.*;
import kg.attractor.quizplatform.servise.ErrorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalControllerAdvice {
    private final ErrorService errorService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    private ResponseEntity<ErrorResponseBody> validHandler(MethodArgumentNotValidException e) {
        return new ResponseEntity<>(errorService.makeResponse(e.getBindingResult()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailExist.class)
    private ResponseEntity<ErrorResponseBody> EmailExist(EmailExist e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorService.makeResponse(e, "email already exist", HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ErrorResponseBody> illegalArgument(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorService.makeResponse(e, "illegal argument", HttpStatus.BAD_REQUEST));
    }
}
