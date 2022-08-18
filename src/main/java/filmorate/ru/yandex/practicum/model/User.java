package filmorate.ru.yandex.practicum.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    private Date birthday;

    @JsonIgnore
    private Set<Long> friendIds = new HashSet<>();

    public User(long user_id, String login, String name, String email, Date birthday) {
        this.id = user_id;
        this.login = login;
        this.name = name;
        this.email = email;
        this.birthday = birthday;
    }
}
