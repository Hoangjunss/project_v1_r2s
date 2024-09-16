package com.r2s.project_v1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
@Getter
public class CustomException extends RuntimeException{
    private  final Error error;

    public CustomException(Error error){
        super(error.getMessage());
        this.error = error;
    }



}
