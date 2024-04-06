package com.omega.hogwatsartifactsonline.hogwatsuser;

import com.omega.hogwatsartifactsonline.Exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserService userService;

    List<HogwartsUser> users = new ArrayList<>();

    @BeforeEach
    void setUp() {
        HogwartsUser user = new HogwartsUser(
                1,
                "osteenomega",
                "1234",
                true,
                "user");
        users.add(user);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
        //given
        given(userRepository.findAll()).willReturn(users);

        //when
        List<HogwartsUser> actual = userService.findAll();
        assertThat(users.size()).isEqualTo(actual.size());

        //then
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void findByIdSuccessful() {
        HogwartsUser user = new HogwartsUser(
                1,
                "osteenomega",
                "1234",
                true,
                "user");

        //given
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        HogwartsUser returnedUser = userService.findById(1);

        //when
        assertThat(user).isEqualTo(returnedUser);
        //then
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void findByIdUnsuccessful() {
        //given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        //when
        Throwable thrown = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.findById(1)
        );

       assertThat(thrown)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user not found");
        //then
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void addUserSuccessful() {
        HogwartsUser user = new HogwartsUser(
                1,
                "osteenomega",
                "1234",
                true,
                "user");

        //given
        given(userRepository.save(user)).willReturn(user);
        given(passwordEncoder.encode(user.getPassword())).willReturn("encoded");
        HogwartsUser savedUser = userService.save(user);

        //when
        assertThat(savedUser).isEqualTo(user);

        //then
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserSuccessful() {
        HogwartsUser user = new HogwartsUser(
                1,
                "osteen",
                "1234",
                true,
                "user");

        HogwartsUser newUser = new HogwartsUser(
                1,
                "omega",
                "1234",
                true,
                "user");

        //given
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        HogwartsUser updatedUser = userService.update(1, newUser);

        //when
        assertThat(user.getUserId()).isEqualTo(updatedUser.getUserId());
        assertThat(user.getUsername()).isEqualTo(updatedUser.getUsername());
        assertThat(user.getRoles()).isEqualTo(updatedUser.getRoles());
        assertThat(user.isEnabled()).isEqualTo(updatedUser.isEnabled());

        //then
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserUnsuccessfulWithNonExistentId() {
        HogwartsUser user = new HogwartsUser(
                1,
                "osteenomega",
                "1234",
                true,
                "user");

        //given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        //when
        Throwable throwable = catchThrowable(() -> userService.update(1, user));

        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user not found");

        //then
        verify(userRepository, times(1)).findById(1);
    }

    @Test
    void deleteByIdSuccessful() {
        HogwartsUser user = new HogwartsUser(
                1,
                "osteenomega",
                "1234",
                true,
                "user");

        //given
        given(userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById(1);

        //when
        userService.delete(1);

        //then
        verify(userRepository, times(1)).deleteById(1);
    }

    @Test
    void deleteByIdUnsuccessfulUserWithNonExistentId() {
        //given
        given(userRepository.findById(1)).willReturn(Optional.empty());

        //when
        Throwable throwable = catchThrowable(() -> userService.findById(1));

        assertThat(throwable)
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("user not found");

        //then
        verify(userRepository, times(1)).findById(1);

    }
}