package hexlet.code.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
@Table(name = "tasks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private long id;

    @NotBlank
    @Size(min = 3, max = 1000)
    @EqualsAndHashCode.Include
    private String name;

    @EqualsAndHashCode.Include
    private String description;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    private TaskStatus taskStatus;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    private User author;

    @ManyToOne
    @EqualsAndHashCode.Exclude
    private User executor;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @EqualsAndHashCode.Exclude
    private Instant createdAt;

    @ManyToMany
    @Fetch(JOIN)
    @EqualsAndHashCode.Exclude
    private Set<Label> labels;
}
