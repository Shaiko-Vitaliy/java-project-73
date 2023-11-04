package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;


@Entity
@Getter
@Setter
@Builder
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Exclude
    private Long id;

    @NotBlank
    @EqualsAndHashCode.Include
    private String firstName;

    @NotBlank
    @EqualsAndHashCode.Include
    private String lastName;

    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String email;

    @NotBlank
    @JsonIgnore
    @Size(min = 3, max = 100)
    @EqualsAndHashCode.Exclude
    private String password;

    @CreationTimestamp
    @Temporal(TIMESTAMP)
    @EqualsAndHashCode.Exclude
    private Instant createdAt;
    public User(final Long id) {
        this.id = id;
    }
}
