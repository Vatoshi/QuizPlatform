package kg.attractor.quizplatform.dto.groupedDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeaderWithQuesAndAnswer {
    private String mark;
    private List<QuesAndAnswerDto> quesAndAnswerDtos;
    private LocalDateTime dateTime;
}
