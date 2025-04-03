package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.dto.groupedDto.HeaderWithQuesAndAnswer;
import kg.attractor.quizplatform.dto.modelsDto.OptionsDto;
import kg.attractor.quizplatform.dto.groupedDto.QuesAndAnswerDto;
import kg.attractor.quizplatform.dto.groupedDto.QuizWithQuesDto;
import kg.attractor.quizplatform.exeptions.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class QuizWithQuesDao {
    private final JdbcTemplate jdbcTemplate;

    public List<QuizWithQuesDto> getQuizToAnswer(Long quizId) {
        String getQuesId = "select id from questions where quiz_id = ?";
        List<Long> questions = jdbcTemplate.queryForList(getQuesId, Long.class, quizId);
        List<QuizWithQuesDto> curentQuizWithQuesDtos = new ArrayList<>();
        for (Long questionId : questions) {
            String getOptions = "select option_text, is_correct from options where question_id = ?";
            List<OptionsDto> answers = jdbcTemplate.query(getOptions, new Object[]{questionId}, (rs, rowNum) ->
                    new OptionsDto(rs.getString("option_text"), rs.getBoolean("is_correct")));
            String questionText = "select question_text from questions where id = ?";
            String questxt = jdbcTemplate.queryForObject(questionText, String.class, questionId);
            QuizWithQuesDto curentQuiz = new QuizWithQuesDto(questxt,answers);
            curentQuizWithQuesDtos.add(curentQuiz);
        }
        return curentQuizWithQuesDtos;
    }

    public String getTitle(Long quizId) {
        String sql = "select title from quizzes where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, quizId);
        } catch (Exception e) {
            throw new NotFound("quiz not found");
        }
    }
}

