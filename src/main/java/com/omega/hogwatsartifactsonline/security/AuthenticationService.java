package com.omega.hogwatsartifactsonline.security;

import com.omega.hogwatsartifactsonline.dto.UserDto;
import com.omega.hogwatsartifactsonline.dtomapper.UserDtoMapper;
import com.omega.hogwatsartifactsonline.hogwatsuser.AppUser;
import com.omega.hogwatsartifactsonline.hogwatsuser.HogwartsUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtProvider jwtProvider;
    private final UserDtoMapper userDtoMapper;

    public Map<String, Object> createLoginInfo(Authentication authentication) {
        AppUser principal = (AppUser) authentication.getPrincipal();
        HogwartsUser hogwartsUser = principal.getHogwartsUser();

        UserDto userDto = userDtoMapper.apply(hogwartsUser);

        Map<String, Object> loginInfo = new HashMap<>();
        String token = jwtProvider.createToken(authentication);

        loginInfo.put("token", token);
        loginInfo.put("user", userDto);

        return loginInfo;
    }
}
