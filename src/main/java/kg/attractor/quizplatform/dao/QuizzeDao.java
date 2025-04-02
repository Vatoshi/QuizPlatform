package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.dto.OptionDto;
import kg.attractor.quizplatform.dto.QuestionDto;
import kg.attractor.quizplatform.dto.QuizzeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import java.sql.PreparedStatement;
import java.sql.Statement;

@Component
@RequiredArgsConstructor
public class QuizzeDao {
    private final JdbcTemplate jdbcTemplate;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public void createQuiz(QuizzeDto quizzeDto, Long userId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into quizzes(title, description, creator_id) values(?, ?, ?)"
                            ,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, quizzeDto.getTitle());
            ps.setString(2, quizzeDto.getDescription());
            ps.setLong(3, userId);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        Long id = key.longValue();

        String sqlQuestions = "insert into questions(quiz_id, question_text) values (?, ?)";
        for (QuestionDto ques : quizzeDto.getQuestion()) {
            KeyHolder questionKeyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(
                        sqlQuestions, new String[]{"ID"}
                );
                ps.setLong(1, id);
                ps.setString(2, ques.getQuestionText());
                return ps;
            }, questionKeyHolder);
            int questionId = questionKeyHolder.getKey().intValue();
            int trueCountHAHAHHAHA = 0;
            for (OptionDto option : ques.getOptions()) {
                String sqlOption = "insert into options(question_id, option_text,is_correct) values (?,?,?)";
                jdbcTemplate.update(sqlOption, questionId, option.getOptionText(), option.getIsCorrect());
                if (option.getIsCorrect()) {trueCountHAHAHHAHA++;}
            }
            if (trueCountHAHAHHAHA != 1) {throw new IllegalArgumentException("должен быть один правильный вариант");}
        }
    }

    public Long UserId(String username) {
        System.out.println(username);
        String sql = "select id from users where email = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, username);
    }
}