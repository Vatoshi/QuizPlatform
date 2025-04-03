package kg.attractor.quizplatform.dto.groupedDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllQuizDto {
    private Long id;
    private String quizName;
    private Integer questionCount;
}
