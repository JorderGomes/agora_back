package com.jorder.agora.infra;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RestErrorMessage {
    private int status;
    private String message;
}
