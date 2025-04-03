package kg.attractor.quizplatform.dto.groupedDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeaderBoardDto {
    private String name;
    private Double score;
}
