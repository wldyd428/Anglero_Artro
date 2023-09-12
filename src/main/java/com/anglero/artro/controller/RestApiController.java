package com.anglero.artro.controller;

import com.anglero.artro.config.auth.PrincipalDetails;
import com.anglero.artro.model.Role;
import com.anglero.artro.model.User;
import com.anglero.artro.model.UserDTO;
import com.anglero.artro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class RestApiController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("join")
    public String join(@RequestBody User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRole(Role.ROLE_USER);
        userRepository.save(user);
        return "회원가입완료";
    }

    @GetMapping("/api/v1/user")
    public UserDTO user(
            @AuthenticationPrincipal PrincipalDetails principalDetails) {

        User user = principalDetails.getUser();

        UserDTO userDTO = new UserDTO(user);

        return userDTO;
    }

    @GetMapping("/api/v1/manager")
    public String manager() {
        return "manager";
    }

    @GetMapping("/api/v1/admin")
    public String admin() {
        return "admin";
    }
}
