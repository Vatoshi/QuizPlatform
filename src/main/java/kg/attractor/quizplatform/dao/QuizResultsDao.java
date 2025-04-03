package kg.attractor.quizplatform.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QuizResultsDao {
    private final JdbcTemplate jdbcTemplate;

    public void SetQuizResult(Long userId, Long quizId, Integer score) {
        String sql = "insert into quiz_results (quiz_id, user_id, score) values (?, ?, ?)";
        jdbcTemplate.update(sql,quizId,userId,score);
    }

    public Integer getQuizResult(Long userId, Long quizId) {
        String sql = "select id from quiz_results where user_id = ? and quiz_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, quizId);
        } catch (Exception e) {
            return null;
        }
    }
}
