package tests.api.lombok;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserUpdateJobResponse {
    private String name;
    private String job;
    private String updatedAt;
}
