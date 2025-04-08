package kg.attractor.quizplatform.dto.groupedDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetScoreDto {
    private Integer yourCorrectAnswerCount;
    private Integer totalAnswers;
    private Integer score;
    private Integer ratingFromUser;
}
