package com.omega.hogwatsartifactsonline.hogwatsuser;

import com.omega.hogwatsartifactsonline.dto.UserDto;
import com.omega.hogwatsartifactsonline.dtomapper.UserDtoMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.endpoint.base-url}/users")
public class UserController {
    private final UserService userService;
    private final UserDtoMapper userDtoMapper;

    @GetMapping
    public List<UserDto> findAllUsers() {
        List<HogwartsUser> foundHogwartsUsers = userService.findAll();

        // Convert foundUsers to a list of UserDtos.
        return foundHogwartsUsers.stream()
                .map(userDtoMapper)
                .collect(Collectors.toList());
        // Note that UserDto does not contain password field
    }

    @GetMapping("/{userId}")
    public UserDto findUserById(@PathVariable Integer userId) {
        HogwartsUser foundHogwartsUser = userService.findById(userId);
        return userDtoMapper.apply(foundHogwartsUser);
    }

    /**
     * We are not using UserDto, but User, since we require password.
     *
     * @param newHogwartsUser
     * @return
     */
    @PostMapping("/new")
    public UserDto addUser(@Valid
                           @RequestBody HogwartsUser newHogwartsUser) {
        HogwartsUser savedUser = userService.save(newHogwartsUser);
        return userDtoMapper.apply(savedUser);
    }

    // We are not using this to update password, need another changePassword method in this class.
    @PutMapping("/{userId}")
    public UserDto updateUser(@PathVariable Integer userId,
                              @Valid
                              @RequestBody UserDto userDto) {
        HogwartsUser update = userDtoMapper.apply(userDto);
        HogwartsUser updatedHogwartsUser = userService.update(userId, update);
        return userDtoMapper.apply(updatedHogwartsUser);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Integer userId) {
        userService.delete(userId);
    }

}