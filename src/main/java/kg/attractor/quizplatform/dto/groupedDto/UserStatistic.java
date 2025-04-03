package kg.attractor.quizplatform.dto.groupedDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserStatistic {
    private String yourName;
    private Integer quizPassedCount;
    private Double averageScore;
}
