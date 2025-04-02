package kg.attractor.quizplatform.model;

import lombok.Data;

@Data
public class Option {
    Long questionId;
    String optionText;
    Boolean isCorrect;
}
