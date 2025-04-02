package kg.attractor.quizplatform.model;

import lombok.Data;

@Data
public class QuizResults {
    Long userId;
    Long quizId;
    Integer score;
    Long id;
}
