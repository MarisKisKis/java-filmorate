package filmorate.ru.yandex.practicum.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class User {
    private long id;
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    private Date birthday;
    private boolean isMutionalFriend;
    private boolean isNotMutionalFriend;
    @JsonIgnore
    private Set<Long> friendIds = new HashSet<>();

    public User(long user_id, String login, String name, Date birthday) {
        this.id = user_id;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }
}
