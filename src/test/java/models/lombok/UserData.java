package models.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserData {
    public String getEmail() {
        return email;
    }

    private int id;
    private String email;
    private String first_name;
    private String last_name;
}
