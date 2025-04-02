package kg.attractor.quizplatform.model;

import lombok.Data;

@Data
public class User {
    String username;
    String password;
    String email;
    Long id;
}
