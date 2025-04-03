package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.dto.groupedDto.ScoreDto;
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

    public Integer getRating (Long userId, Long quizId) {
        String sql = "select rating_from_user from quiz_results where user_id = ? and quiz_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, quizId);
        } catch (Exception e) {
            return null;
        }
    }

    public void SetRating(Long resultId, ScoreDto score) {
        String sql = "update quiz_results set rating_from_user = ? where id = ?";
        jdbcTemplate.update(sql, score.getScore(), resultId);
    }
}
