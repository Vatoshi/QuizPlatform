package kg.attractor.quizplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    String username;
    String password;
    String email;
    Long id;
    Boolean enabled;
    Long roleId;
}
