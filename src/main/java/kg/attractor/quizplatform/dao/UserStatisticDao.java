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

    public Long UserId(String username) {
        System.out.println(username);
        String sql = "select id from users where email = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, username);
    }

    public Integer CountAllPassedQuizzes(Long userId) {
        String sql = "select id from quiz_results where user_id = ?";
        List<Long> ids = jdbcTemplate.queryForList(sql,Long.class, userId);
        Integer quizPassedCount = ids.size();
        if (quizPassedCount == 0) {
            throw new NotFound("The user has no statistics (has not completed any quizzes)");
        }
        return quizPassedCount;
    }

    public Double AverageScore(Long userId) {
        String sql = "select score from quiz_results where user_id = ?";
        List<Integer> scores = jdbcTemplate.queryForList(sql,Integer.class,userId);
        return scores.stream().mapToDouble(Integer::doubleValue).sum()/scores.size();
    }

    public String getName(Long userId) {
        String sql = "select username from users where id = ?";
        return jdbcTemplate.queryForObject(sql,String.class,userId);
    }
}
