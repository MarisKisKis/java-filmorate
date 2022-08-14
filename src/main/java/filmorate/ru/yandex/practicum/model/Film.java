package filmorate.ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    private long id;
    @NotBlank
    private String name;
    private String description;
    private Date releaseDate;
    private int duration;
    private String genre;
    private String rating;
    @JsonIgnore
    private Set<Long> likes = new HashSet<>();

    public Film(long film_id, String name, String description, Date releaseDate) {
        this.id = film_id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
    }
}
