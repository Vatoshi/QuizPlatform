package kg.attractor.quizplatform.dto.modelsDto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OptionsDto {
    private String option;
    @JsonIgnore
    private Boolean isCorrect;
}
