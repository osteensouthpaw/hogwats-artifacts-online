package com.omega.hogwatsartifactsonline.dtomapper;

import com.omega.hogwatsartifactsonline.dto.UserDto;
import com.omega.hogwatsartifactsonline.hogwatsuser.HogwartsUser;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@RequiredArgsConstructor
@Component
public class UserDtoMapper implements Function<HogwartsUser, UserDto> {
    @Override
    public UserDto apply(HogwartsUser user) {
        return new UserDto(
                user.getUserId(),
                user.getUsername(),
                user.isEnabled(),
                user.getRoles()
        );
    }

    public HogwartsUser apply(UserDto userDto) {
        HogwartsUser user = new HogwartsUser();
        user.setUsername(userDto.username());
        user.setEnabled(userDto.enabled());
        user.setRoles(userDto.roles());
        return user;
    }
}
