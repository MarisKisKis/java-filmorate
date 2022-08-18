package filmorate.ru.yandex.practicum.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Mpa {
    private int id;
    private String mpaName;

    public Mpa(int id, String mpaName) {
        this.id = id;
        this.mpaName = mpaName;
    }
}
