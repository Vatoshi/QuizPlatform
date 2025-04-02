package kg.attractor.quizplatform.model;

import lombok.Data;

@Data
public class Quizze {
    String title;
    String description;
    Long creatorId;
    Long id;
}
