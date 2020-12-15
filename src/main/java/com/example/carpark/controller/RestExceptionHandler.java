package com.example.carpark.controller;

import com.example.carpark.dto.ErrorResponse;
import com.example.carpark.exception.ChargingPointNotFoundException;
import com.example.carpark.exception.ChargingPointNotOccupiedException;
import com.example.carpark.exception.InvalidIdException;
import com.example.carpark.exception.OccupiedChargingPointException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ChargingPointNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handle(ChargingPointNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(), HttpStatus.NOT_FOUND.value(), HttpStatus.NOT_FOUND.getReasonPhrase());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ChargingPointNotOccupiedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handle(ChargingPointNotOccupiedException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(OccupiedChargingPointException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handle(OccupiedChargingPointException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(), HttpStatus.CONFLICT.value(), HttpStatus.CONFLICT.getReasonPhrase());
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InvalidIdException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handle(InvalidIdException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getMessage(), HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
