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

import java.util.Set;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {

    @NotBlank(message = "Name should not be empty")
    @Size(min = 3, max = 1000)
    private String name;
    private String description;
    @NotNull(message = "Task status should not be empty")
    private Long taskStatusId;
    private Long executorId;
    private Set<Long> labelIds;
}
