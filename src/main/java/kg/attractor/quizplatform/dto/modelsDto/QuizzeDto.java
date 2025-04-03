package kg.attractor.quizplatform.dto.modelsDto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class QuizzeDto {
    @NotNull(message = "обязательный ввод")
    @Size(min = 3, max = 50, message = "название должно содержать минимум 3 символа, максимум 50")
    private String title;
    @NotNull
    @Size(min = 3, max = 100, message = "описание должно содержать минимум 3 символа")
    private String description;
    @NotNull
    private List<@Valid QuestionDto> question;
}
