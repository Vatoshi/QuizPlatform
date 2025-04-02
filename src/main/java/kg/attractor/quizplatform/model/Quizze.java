package kg.attractor.quizplatform.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Quizze {
    String title;
    String description;
    Long creatorId;
    Long id;
}
