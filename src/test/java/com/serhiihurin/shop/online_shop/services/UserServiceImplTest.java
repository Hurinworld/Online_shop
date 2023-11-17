package com.serhiihurin.shop.online_shop.services;

import com.serhiihurin.shop.online_shop.dao.UserRepository;
import com.serhiihurin.shop.online_shop.dto.PasswordUpdateRequestDTO;
import com.serhiihurin.shop.online_shop.dto.RegisterRequestDTO;
import com.serhiihurin.shop.online_shop.dto.UserRequestDTO;
import com.serhiihurin.shop.online_shop.entity.User;
import com.serhiihurin.shop.online_shop.enums.Role;
import com.serhiihurin.shop.online_shop.exception.ApiRequestException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private User testUser;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, new BCryptPasswordEncoder());
        modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        testUser = User.builder()
                .id(1L)
                .firstName("User")
                .lastName("For test")
                .email("testUser@gmail.com")
                .password("password")
                .cash(26665.0)
                .role(Role.CLIENT)
                .build();
    }

    @Test
    void getAllUsers() {
        //when
        userService.getAllUsers();
        //then
        Mockito.verify(userRepository).findAll();
    }

    @Test
    void getUser() {
        //when
        Mockito.when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        User capturedValue = userService.getUser(testUser.getId());
        //then
        assertThat(capturedValue)
                .usingRecursiveComparison()
                .ignoringFields("password")
                .isEqualTo(testUser);
    }

    @Test
    void getUserByEmail() {
        //when
        Mockito.when(userRepository.getByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));
        User capturedValue = userService.getUserByEmail(testUser.getEmail());
        //then
        assertThat(capturedValue)
                .usingRecursiveComparison()
                .ignoringFields("password")
                .isEqualTo(testUser);
    }

    @Test
    void createUser() {
        testUser.setId(null);
        RegisterRequestDTO registerRequestDTO = modelMapper.map(testUser, RegisterRequestDTO.class);
        //when
        userService.createUser(registerRequestDTO);
        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User capturedValue = userArgumentCaptor.getValue();
        assertThat(capturedValue)
                .usingRecursiveComparison()
                .ignoringFields("password")
                .isEqualTo(testUser);
    }

    @Test
    void saveUser() {
        //when
        userService.saveUser(testUser);
        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User capturedValue = userArgumentCaptor.getValue();
        assertThat(capturedValue).isEqualTo(testUser);
    }

    @Test
    void updateUser() {
        //given
        String newFirstName = "Amogus";
        String newLastName = "Abobus";
        Double newCash = 30000.0;
        UserRequestDTO userRequestDTO = UserRequestDTO.builder()
                .firstName(newFirstName)
                .lastName(newLastName)
                .email("testUser@gmail.com")
                .cash(newCash)
                .build();
        //when
        userService.updateUser(testUser, userRequestDTO);
        //then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User capturedValue = userArgumentCaptor.getValue();
        assertThat(capturedValue.getFirstName()).isEqualTo(newFirstName);
        assertThat(capturedValue.getLastName()).isEqualTo(newLastName);
        assertThat(capturedValue.getCash()).isEqualTo(newCash);
        assertThat(capturedValue.getEmail()).isEqualTo(testUser.getEmail());
    }

    @Test
    void updateUsername() {
        // given
        String newEmail = "newEmail@gmail.com";
        // when
        userService.updateUsername(testUser, newEmail);
        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User capturedValue = userArgumentCaptor.getValue();
        assertThat(capturedValue.getEmail()).isEqualTo(newEmail);
    }

    @Test
    @Disabled
    void updatePassword() {
        // given
        PasswordUpdateRequestDTO passwordUpdateRequestDTO = new PasswordUpdateRequestDTO();
        passwordUpdateRequestDTO.setPassword("newPassword");
        // when
        userService.updatePassword(testUser, passwordUpdateRequestDTO);
        // then
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User capturedValue = userArgumentCaptor.getValue();
        assertThat(capturedValue.getPassword()).isNotNull();
        assertThat(capturedValue.getPassword()).isNotEqualTo(passwordUpdateRequestDTO.getPassword());
    }

    @Test
    public void updatePasswordWithEmptyNewPassword() {
        // given
        PasswordUpdateRequestDTO requestDTO = new PasswordUpdateRequestDTO();

        // when
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userService.updatePassword(testUser, requestDTO));

        // then
        assertThat(exception.getMessage()).isEqualTo("New password cannot be empty");
        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    public void testUpdatePasswordWithSameOldAndNewPassword() {
        // given
        PasswordUpdateRequestDTO requestDTO = new PasswordUpdateRequestDTO();
        requestDTO.setPassword(testUser.getPassword());

        // when
        ApiRequestException exception = assertThrows(ApiRequestException.class,
                () -> userService.updatePassword(testUser, requestDTO));

        // then
        assertThat(exception.getMessage()).isEqualTo("New password must differ from the old one");
        Mockito.verifyNoInteractions(userRepository);
    }

    @Test
    void deleteUser() {
        //when
        userService.deleteUser(testUser.getId());
        //then
        Mockito.verify(userRepository).deleteById(testUser.getId());
    }
}