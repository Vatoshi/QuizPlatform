package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.dto.groupedDto.ScoreDto;
import kg.attractor.quizplatform.exeptions.NotFound;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QuizResultsDao {
    private static final Logger log = LoggerFactory.getLogger(QuizResultsDao.class);
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

    public Integer getQuizResultInteger(Long userId, Long quizId) {
        String sql = "select id from quiz_results where user_id = ? and quiz_id = ?";
        System.out.println(userId);
        System.out.println(quizId);
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, quizId);

    }

    public List<Long> questionId (Integer quizId) {
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

    public Integer getQuizResultsId (Long userId, Long quizId) {
        String sql = "select id from quiz_results where user_id = ? and quiz_id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, userId, quizId);
        } catch (Exception e) {
            log.info("нельзя дать оценку квизу который езе не пройден");
            throw new NotFound("Нельзя поставить оценку без прохождения");
        }

    }

    public void SetRating(Long resultId, ScoreDto score) {
        String sql = "update quiz_results set rating_from_user = ? where id = ?";
        jdbcTemplate.update(sql, score.getScore(), resultId);
    }

    public List<Map<String, Object>> getUsers(Long quizId) {
        String sql = "select user_id, score from quiz_results where quiz_id = ?";
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(sql, quizId);

        for (Map<String, Object> row : rows) {
            String sql1 = "select username from users where id = ?";
            String name = jdbcTemplate.queryForObject(sql1, String.class, row.get("user_id"));

            row.remove("user_id");
            row.put("username", name);
        }
        return rows;
    }
}
