package kg.attractor.quizplatform.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    @NotNull(message = "напишите вопрос")
    @Size(min = 5, max = 50, message = "вопрос должен быть от 5 до 50")
    private String questionText;
    @NotNull(message = "не может быть нулем")
    @Valid
    @Size(min = 2, message = "минимум два варианта ответа")
    List<OptionDto> options;
}
