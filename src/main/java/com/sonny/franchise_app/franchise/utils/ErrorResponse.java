package com.sonny.franchise_app.franchise.utils;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ErrorResponse {

    private String timestamp;
    private int status;
    private String error;
    private List<String> messages;


}