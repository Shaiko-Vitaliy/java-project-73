package hexlet.code.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusDto {
    private static final int MIN_LENGTH = 1;

    @NotBlank(message = "Name is required")
    @Size(min = MIN_LENGTH, message = "Min length name is 1 char")
    private String name;
}
