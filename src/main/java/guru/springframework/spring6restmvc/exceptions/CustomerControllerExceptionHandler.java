package guru.springframework.spring6restmvc.exceptions;

import guru.springframework.spring6restmvc.controller.CustomerController;
import guru.springframework.spring6restmvc.model.ExceptionResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = CustomerController.class)
public class CustomerControllerExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity handleNotFoundException(NotFoundException nfe){
        ExceptionResponseDTO exceptionResponseDTO = new ExceptionResponseDTO(
                404,
                nfe.getMessage()
        );

        return new ResponseEntity<>(exceptionResponseDTO, HttpStatus.NOT_FOUND);
    }
}
