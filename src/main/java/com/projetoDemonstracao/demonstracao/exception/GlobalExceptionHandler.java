package com.projetoDemonstracao.demonstracao.exception;

import com.projetoDemonstracao.demonstracao.dto.ResponseDto;
import org.hibernate.PropertyValueException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PropertyValueException.class)
    public ResponseEntity<ResponseDto<Void>> handlePropertyValueException(PropertyValueException ex) {
        ResponseDto<Void> responseDto = new ResponseDto<>();
        String entityName = ex.getEntityName();
        String simpleEntityName = entityName.substring(entityName.lastIndexOf('.') + 1); // Obtém apenas o nome simples
        String propertyName = ex.getPropertyName();
        responseDto.setMensagem("Erro: A propriedade '" + propertyName + "' da entidade '" + simpleEntityName + "' não pode ser nula.");
        responseDto.setSucesso(false);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseDto<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        Throwable cause = ex.getCause();

        if (cause instanceof ConstraintViolationException) {
            return handleConstraintViolationException((ConstraintViolationException) cause);
        } else if (cause instanceof PropertyValueException) {
            return handlePropertyValueException((PropertyValueException) cause);
        }

        ResponseDto<Void> responseDto = new ResponseDto<>();
        responseDto.setMensagem("Erro de integridade no banco de dados: " + ex.getMessage());
        responseDto.setSucesso(false);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    private ResponseEntity<ResponseDto<Void>> handleConstraintViolationException(ConstraintViolationException ex) {
        ResponseDto<Void> responseDto = new ResponseDto<>();
        String constraintName = ex.getConstraintName();
        responseDto.setMensagem("Erro de integridade no banco de dados: Violação de restrição de integridade: " + constraintName);
        responseDto.setSucesso(false);
        return new ResponseEntity<>(responseDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto<Void>> handleException(Exception ex) {
        ResponseDto<Void> response = new ResponseDto<>(false, "Erro: " + ex.getMessage(), null);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
