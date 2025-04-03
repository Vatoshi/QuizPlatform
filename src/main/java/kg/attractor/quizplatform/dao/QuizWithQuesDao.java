package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.dto.groupedDto.HeaderWithQuesAndAnswer;
import kg.attractor.quizplatform.dto.modelsDto.OptionsDto;
import kg.attractor.quizplatform.dto.groupedDto.QuesAndAnswerDto;
import kg.attractor.quizplatform.dto.groupedDto.QuizWithQuesDto;
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
        return jdbcTemplate.queryForObject(sql, String.class, quizId);
    }

    public HeaderWithQuesAndAnswer getSolveQuiz(Long quizId, List<String> answers) {
        String mark = "";
        List<QuizWithQuesDto> questions = getQuizToAnswer(quizId);
        List<QuesAndAnswerDto> solves = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            String ques = questions.get(i).getQuestion();
            String correctOption = questions.get(i).getAnswers().stream()
                    .filter(OptionsDto::getIsCorrect)
                    .map(OptionsDto::getOption)
                    .findFirst()
                    .orElse(null);
            QuesAndAnswerDto opa = new QuesAndAnswerDto(ques,correctOption,answers.get(i));
            solves.add(opa);
        }
        int count = questions.size() - answers.size();
        if (count == 0) {
            mark = "вы ответили на все вопросы";
        } else {
            mark = "вы не ответили на " + count + " количество вопросов (автоматически засчитываются за неправильный ответ)";
        }
        HeaderWithQuesAndAnswer header = new HeaderWithQuesAndAnswer(mark,solves);
        return header;
    }

    private Long getQuesId (String question) {
        String sql = "select id from questions where question_text = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, question);
    }
}

