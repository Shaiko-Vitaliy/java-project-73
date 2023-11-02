package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LabelDto {

    @NotBlank(message = "Name is required")
    @Size(min = 1, max = 1000, message = "Min length name is 1 char")
    private String name;
}
