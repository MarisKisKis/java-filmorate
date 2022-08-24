package filmorate.ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Genre {
    private int id;
    private String name;

    public Genre(int genreId, String name) {
        this.id = genreId;
        this.name = name;
    }

    public int getId()
    {
        return this.id;
    }
}
