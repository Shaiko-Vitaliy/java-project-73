package hexlet.code.controllers;

import hexlet.code.component.JWTHelper;
import hexlet.code.dto.LoginDto;
import hexlet.code.service.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
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

    private final JWTHelper jwtHelper;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping(path = "/login")
    public ResponseEntity<String> logIn(@RequestBody LoginDto dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getEmail(),
                        dto.getPassword())
        );
        final UserDetails userDetails = userDetailsService.loadUserByUsername(dto.getEmail());
        if (userDetails == null) {
            return ResponseEntity
                    .status(HttpServletResponse.SC_BAD_REQUEST)
                    .body("User with username " + dto.getEmail() + " not found");
        }
        return ResponseEntity.ok(jwtHelper.expiring(Map.of(SPRING_SECURITY_FORM_USERNAME_KEY, userDetails)));
    }
}
