package com.jorder.agora.service;

import com.jorder.agora.dto.UserRequestDTO;
import com.jorder.agora.dto.UserResponseDTO;
import com.jorder.agora.mapper.UserMapper;
import com.jorder.agora.model.User;
import com.jorder.agora.model.UserRole;
import com.jorder.agora.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Deve criar um usuário com sucesso")
    void createUserSuccess() {
        UserRequestDTO dto = new UserRequestDTO("William", "william@email.com", "123456", UserRole.USER);
        User user = new User();
        UserResponseDTO expected = new UserResponseDTO(UUID.randomUUID(), "William", "william@email.com");

        when(userMapper.toEntity(dto)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponseDTO(user)).thenReturn(expected);

        UserResponseDTO result = userService.createUser(dto);

        assertNotNull(result);
        assertEquals(expected.name(), result.name());
        verify(userRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("Deve buscar um usuário por ID com sucesso")
    void getUserByIdSuccess() {
        UUID id = UUID.randomUUID();
        User user = new User();
        UserResponseDTO expected = new UserResponseDTO(id, "William", "william@email.com");

        when(userRepository.findById(id)).thenReturn(Optional.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(expected);

        UserResponseDTO result = userService.getUserById(id);

        assertNotNull(result);
        assertEquals(id, result.id());
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar ID inexistente")
    void getUserByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> userService.getUserById(id));
        verify(userRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar lista de todos os usuários")
    void getAllUsersSuccess() {
        User user = new User();
        UserResponseDTO dto = new UserResponseDTO(UUID.randomUUID(), "William", "william@email.com");

        when(userRepository.findAll()).thenReturn(List.of(user));
        when(userMapper.toResponseDTO(user)).thenReturn(dto);

        List<UserResponseDTO> result = userService.getAllUsers();

        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(userRepository).findAll();
    }

//    @Test
//    @DisplayName("Deve atualizar usuário com sucesso")
//    void updateUserSuccess() {
//        UUID id = UUID.randomUUID();
//        UserRequestDTO dto = new UserRequestDTO("Novo Nome", "novo@email.com", "654321");
//        User user = new User();
//        UserResponseDTO expected = new UserResponseDTO(id, "Novo Nome", "novo@email.com");
//
//        when(userRepository.findById(id)).thenReturn(Optional.of(user));
//        when(userRepository.save(user)).thenReturn(user);
//        when(userMapper.toResponseDTO(user)).thenReturn(expected);
//
//        UserResponseDTO result = userService.updateUser(id, dto);
//
//        assertNotNull(result);
//        assertEquals("Novo Nome", result.name());
//        // Verifica se o mapper foi chamado para atualizar a entidade antes de salvar
//        verify(userMapper).updateEntity(dto, user);
//        verify(userRepository).save(user);
//    }
//
//    @Test
//    @DisplayName("Deve deletar usuário com sucesso")
//    void deleteUserSuccess() {
//        UUID id = UUID.randomUUID();
//        when(userRepository.existsById(id)).thenReturn(true);
//
//        assertDoesNotThrow(() -> userService.deleteUser(id));
//
//        verify(userRepository).deleteById(id);
//    }

//    @Test
//    @DisplayName("Deve lançar exceção ao tentar deletar usuário inexistente")
//    void deleteUserNotFound() {
//        UUID id = UUID.randomUUID();
//        when(userRepository.existsById(id)).thenReturn(false);
//
//        assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(id));
//
//        // Garante que o deleteById NUNCA foi chamado se o ID não existe
//        verify(userRepository, never()).deleteById(id);
//    }

}