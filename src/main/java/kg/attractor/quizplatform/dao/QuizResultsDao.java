package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.exeptions.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class QuizResultsDao {
    private final JdbcTemplate jdbcTemplate;

    public void SetQuizResult(Long userId, Long quizId, Integer score) {
        String sql = "insert into quiz_results (quiz_id, user_id, score) values (?, ?, ?)";
        jdbcTemplate.update(sql, quizId, userId, score);
    }

    public Integer getQuizResult(Long userId, Long quizId) {
        String sql = "select score from quiz_results where user_id = ? and quiz_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, quizId);
        } catch (Exception e) {
            return null;
        }
    }

    public Long getQuizResultInteger(Long userId, Long quizId) {
        String sql = "select id from quiz_results where user_id = ? and quiz_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, userId, quizId);
        } catch (Exception e) {
            throw new NotFound("Вы еще не проходили данный квиз");
        }
    }

    public List<Long> questionId (Long quizId) {
        String sql = "select id from questions where quiz_id = ?";
        return jdbcTemplate.queryForList(sql, Long.class, quizId);
    }

    public String correctAnswer (Long ques_id) {
        String sql = "select option_text from options where question_id = ? and is_correct = true";
        return jdbcTemplate.queryForObject(sql, String.class, ques_id);
    }

    public Integer getScore(Long userId, Long quizId) {
        String sql = "select score from quiz_results where user_id = ? and quiz_id = ?";
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, quizId);
    }

    public List<Integer> getQuestionsCount(Long quizId) {
        String sql = "select id from questions where quiz_id = ?";
        return jdbcTemplate.queryForList(sql, Integer.class, quizId);
    }

}
