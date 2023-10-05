package hexlet.code.controllers;

import hexlet.code.component.JWTHelper;
import hexlet.code.dto.LoginDto;
import hexlet.code.model.User;
import hexlet.code.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY;

@RestController
@AllArgsConstructor
@RequestMapping("${base-url}")
public class LoginController {

    private final PasswordEncoder bCryptPasswordEncoder;
    private final JWTHelper jwtHelper;
    private final UserService userService;

    @PostMapping(path = "/login")
    public String logIn(@RequestBody LoginDto dto) {
        User existedUser = userService.findUserByEmail(dto.getEmail());
        String passwordToCheck = dto.getPassword();
        if (!bCryptPasswordEncoder.matches(passwordToCheck, existedUser.getPassword())) {
            throw new UsernameNotFoundException("Sign in failed. Incorrect password!");
        }
        return jwtHelper.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, existedUser));
    }
}
