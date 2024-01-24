package zet.kedzieri.bank.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import zet.kedzieri.bank.api.v1.dto.ErrorDTO;
import zet.kedzieri.bank.common.AcceptableToRespondException;

@RestControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    public final ResponseEntity<Object> handleRuntimeException(RuntimeException exception) {
        if (exception instanceof AcceptableToRespondException) {
            //todo preserve response status
            return new ResponseEntity<>(ErrorDTO.from(exception), HttpStatus.BAD_REQUEST);
        }
        if (!isInternal(exception.getClass())) {
            System.err.println("Błąd serwera");
            exception.printStackTrace(System.err);
        }
        return new ResponseEntity<>(new ErrorDTO("BadRequest", "BadRequest"), HttpStatus.BAD_REQUEST);
    }

    private static boolean isInternal(Class<?> clazz) {
        return clazz.getName().startsWith("zet.kedzieri.");
    }

}
