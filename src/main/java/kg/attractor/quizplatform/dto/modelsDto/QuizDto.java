package kg.attractor.quizplatform.dto.modelsDto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizDto {
    private Long id;
    private String title;
    private Long categoryId;
    private String description;
}
