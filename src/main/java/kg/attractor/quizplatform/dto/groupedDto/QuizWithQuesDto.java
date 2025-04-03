package kg.attractor.quizplatform.dto.groupedDto;

import kg.attractor.quizplatform.dto.modelsDto.OptionsDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuizWithQuesDto {
    private String question;
    List<OptionsDto> answers;
}
