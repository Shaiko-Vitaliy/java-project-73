package hexlet.code.dto;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
    private static final int MIN_LENGTH_PAS = 3;

    @NotBlank(message = "FirstName can't be empty")
    private String firstName;

    @NotBlank(message = "LastName can't be empty")
    private String lastName;

    @NotBlank
    @Email(message = "Incorrect format email")
    private String email;

    @Size(min = MIN_LENGTH_PAS, message = "The password must be more than 3 characters")
    private String password;
}
