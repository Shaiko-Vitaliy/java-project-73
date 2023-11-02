package hexlet.code.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDto {
    @NotBlank
    @Email(message = "Incorrect format email")
    private String email;
    @NotBlank
    @Size(min = 3, max = 100, message = "The password must be more than 3 characters")
    private String password;

}
