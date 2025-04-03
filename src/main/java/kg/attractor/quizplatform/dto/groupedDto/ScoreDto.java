package kg.attractor.quizplatform.dto.groupedDto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class ScoreDto {
    @Min(value = 1, message = "от 1 до 5")
    @Max(value = 5, message = "от 1 до 5")
    private Integer score;
}
