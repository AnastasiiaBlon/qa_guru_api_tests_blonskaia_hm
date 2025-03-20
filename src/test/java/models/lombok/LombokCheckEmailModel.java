package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LombokCheckEmailModel {
    public List<UserData> getData() {
        return data;
    }

    private List<UserData> data;
}
