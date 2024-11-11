package edu.mv.mv.models;

import lombok.Getter;
import lombok.Setter;

public class RocketResponse {



    @Setter
    @Getter
    private int httpStatusCode;
    private String message;

}
