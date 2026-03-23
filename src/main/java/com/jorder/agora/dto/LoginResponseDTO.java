package com.jorder.agora.dto;

public record LoginResponseDTO(String token) {

    @Override
    public String token() {
        return token;
    }

}
