package com.omega.hogwatsartifactsonline.security;

import com.omega.hogwatsartifactsonline.hogwatsuser.HogwartsUser;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.endpoint.base-url}/users")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> getLoginInfo(Authentication authentication) {
        Map<String, Object> loginInfo = authenticationService.createLoginInfo(authentication);
        return new ResponseEntity<>(loginInfo, HttpStatus.OK);
    }
}
