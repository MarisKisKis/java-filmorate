package filmorate.ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Genre {
    private int id;
    private String genreName;

    public Genre(int genreId, String genreName) {
        this.id = genreId;
        this.genreName = genreName;
    }

    public int getId()
    {
        return this.id;
    }
}
