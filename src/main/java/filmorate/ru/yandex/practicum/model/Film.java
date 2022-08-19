package filmorate.ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.Date;
import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film {
    private long id;
    @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private Set <Genre> genres;
    @JsonIgnore
    private Set<Long> likes = new LinkedHashSet<>();

    public Film(long film_id, String name, String description, LocalDate releaseDate, int duration,
                Mpa mpa, LinkedHashSet <Genre> genres) {
        this.id = film_id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

}
