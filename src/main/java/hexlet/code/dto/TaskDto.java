package hexlet.code.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private static final int MIN_LENGTH = 1;

    @NotBlank(message = "Name should not be empty")
    @Size(min = MIN_LENGTH)
    private String name;

    private String description;

    @NotNull(message = "Task status should not be empty")
    private Long taskStatusId;
    private Long executorId;
    private List<Long> labelIds;
}
