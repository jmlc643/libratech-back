package com.upao.pe.libratech.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {
    private Integer status;
    private String message;
    private List<String> errors;
}
