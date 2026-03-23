package com.jorder.agora.controller;

import com.jorder.agora.dto.AuthDTO;
import com.jorder.agora.dto.LoginResponseDTO;
import com.jorder.agora.dto.UserRequestDTO;
import com.jorder.agora.dto.UserResponseDTO;
import com.jorder.agora.exceptions.BusinessException;
import com.jorder.agora.model.User;
import com.jorder.agora.service.AuthService;
import com.jorder.agora.service.TokenService;
import com.jorder.agora.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthDTO authDTO) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(authDTO.name(), authDTO.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);
        var token = tokenService.generateToken((User) auth.getPrincipal());
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@RequestBody UserRequestDTO data) {
        if(this.userService.existsByName(data.name())){
            throw new BusinessException("Este nome de usuário já está em uso.");
        } else {
            var userResult = this.userService.createUser(data);
            return ResponseEntity.ok(userResult);
        }
    }

}
