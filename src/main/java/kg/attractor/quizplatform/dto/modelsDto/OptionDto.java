package kg.attractor.quizplatform.dto.modelsDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class OptionDto {
    @NotNull(message = "напишите ответ")
    @Size(max = 50)
    private String optionText;
    @NotNull(message = "обязательно укажите правильность ответа")
    private Boolean isCorrect;
}
