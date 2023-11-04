package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;

import java.time.Instant;
import java.util.Set;

import static jakarta.persistence.TemporalType.TIMESTAMP;
import static org.hibernate.annotations.FetchMode.JOIN;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "statuses")
@EqualsAndHashCode
public class TaskStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank
    @Size(min = 1, max = 1000)
    @EqualsAndHashCode.Include
    private String name;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @EqualsAndHashCode.Exclude
    private Instant createdAt;

    @JsonIgnore
    @Fetch(JOIN)
    @OneToMany
    @EqualsAndHashCode.Exclude
    private Set<Task> tasks;

    public TaskStatus(final Long id) {
        this.id = id;
    }
}
