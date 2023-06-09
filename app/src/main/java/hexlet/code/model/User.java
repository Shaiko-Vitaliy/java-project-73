package hexlet.code.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.yaml.snakeyaml.events.Event;

import java.util.Date;

import static jakarta.persistence.TemporalType.TIMESTAMP;


@Entity
@Getter
@Setter
@Table(name = "users")

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Column(unique = true)
    private String email;
    @NotBlank
    @JsonIgnore
    private String password;
    @CreationTimestamp
    @Temporal(TIMESTAMP)
    private Date createdAt;




//    id – уникальный идентификатор пользователя, генерируется автоматически
//    firstName - имя пользователя
//    lastName - фамилия пользователя
//    email - адрес электронной почты
//    password - пароль
//    createdAt - дата создания (регистрации) пользователя
}
