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

    @NotBlank(message = "FirstName can't be empty")
    private String firstName;

    @NotBlank(message = "LastName can't be empty")
    private String lastName;

    @NotBlank
    @Email(message = "Incorrect format email")
    private String email;

    @Size(min = 3, max = 100, message = "The password must be more than 3 characters")
    private String password;
}
