package com.jorder.agora.service;

import com.jorder.agora.dto.UserRequestDTO;
import com.jorder.agora.dto.UserResponseDTO;
import com.jorder.agora.mapper.UserMapper;
import com.jorder.agora.model.User;
import com.jorder.agora.model.UserRole;
import com.jorder.agora.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO dto) {
        User user = userMapper.toEntity(dto);

        user.setPassword(passwordEncoder.encode(dto.password()));
//        user.setRole(UserRole.valueOf(dto.role().toUpperCase()));

        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public UserResponseDTO getUserById(UUID id) {
        return userRepository.findById(id)
                .map(userMapper::toResponseDTO)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

    public UserResponseDTO updateUser(UUID id, UserRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        userMapper.updateEntity(dto, user);
        return userMapper.toResponseDTO(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        if (!userRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuário não encontrado");
        }
        userRepository.deleteById(id);
    }

    public boolean existsByName(String name){
        return userRepository.existsByName(name);
    }

}
