package hexlet.code.controllers;

import hexlet.code.component.JWTHelper;
import hexlet.code.dto.LoginDto;
//import hexlet.code.service.impl.LogInServiceImpl;
import hexlet.code.model.User;
import hexlet.code.repository.UserRepository;
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

    //    private final LogInServiceImpl logInService;
    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JWTHelper jwtHelper;

    @PostMapping(path = "/login")
    public String logIn(@RequestBody LoginDto dto) {
        User existedUser = userRepository.findUserByEmailIgnoreCase(dto.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Sign in failed. User not found!"));

        String passwordToCheck = dto.getPassword();
        if (!bCryptPasswordEncoder.matches(passwordToCheck, existedUser.getPassword())) {
            throw new UsernameNotFoundException("Sign in failed. Incorrect password!");
        }
        return jwtHelper.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, existedUser));
    }
}
