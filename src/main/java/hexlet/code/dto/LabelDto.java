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
    private static final int MIN = 1;

    @NotBlank(message = "Name is required")
    @Size(min = MIN, message = "Min length name is 1 char")
    private String name;
}
