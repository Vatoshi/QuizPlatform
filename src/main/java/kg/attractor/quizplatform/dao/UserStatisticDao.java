package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.exeptions.NotFound;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserStatisticDao {
    private final JdbcTemplate jdbcTemplate;

    public Integer CountAllPassedQuizzes(Long userId) {
        String sql = "select id from quiz_results where user_id = ?";
        List<Long> ids = jdbcTemplate.queryForList(sql,Long.class, userId);
        Integer quizPassedCount = ids.size();
        if (quizPassedCount == 0) {
            return null;
        }
        return quizPassedCount;
    }

    public Double AverageScore(Long userId) {
        String sql = "select score from quiz_results where user_id = ?";
        List<Integer> scores = jdbcTemplate.queryForList(sql,Integer.class,userId);
        Double average = scores.stream().mapToDouble(Integer::doubleValue).sum()/scores.size();
        return Math.round(average * 100.0) / 100.0;
    }

    public String getName(Long userId) {
        String sql = "select username from users where id = ?";
        return jdbcTemplate.queryForObject(sql,String.class,userId);
    }

    public List<Long> getUsersId() {
        String sql = "select id from users";
        return jdbcTemplate.queryForList(sql, Long.class);
    }

    public Integer getALlQuesId(Long userId) {
        String sql = "select id from quiz_results where quiz_id = ?";
        List<Integer> ids = jdbcTemplate.queryForList(sql,Integer.class,userId);
        return ids.size();
    }
}
