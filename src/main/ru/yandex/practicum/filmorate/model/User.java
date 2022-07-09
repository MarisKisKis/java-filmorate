package yandex.practicum.filmorate.model;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDate;

@Data
public class User {
    private int id;
    //@Email
    private String email;
  //  @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
}
