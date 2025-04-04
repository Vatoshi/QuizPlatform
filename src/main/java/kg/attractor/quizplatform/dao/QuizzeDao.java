package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.dto.groupedDto.GetAllQuizDto;
import kg.attractor.quizplatform.dto.modelsDto.OptionDto;
import kg.attractor.quizplatform.dto.modelsDto.QuestionDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizzeDto;
import kg.attractor.quizplatform.exeptions.NotFound;
import kg.attractor.quizplatform.util.PaginationParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class QuizzeDao {
    private final JdbcTemplate jdbcTemplate;

    public String getCategory(Long quizId) {
        String sql = "select category_id from quizzes where id = ?";
        Long id = jdbcTemplate.queryForObject(sql, Long.class, quizId);
        return jdbcTemplate.queryForObject("select category_name from categories where id = ?", String.class, id);
    }

    public String getQuizTitle(String quiztitle) {
        String sql = "select title from quizzes where title = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, quiztitle);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private Long getCategoryId(String category) {
        String sql = "select id from categories where category_name = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, category);
        } catch (Exception e) {
            log.info("такой категории не существует");
            throw new NotFound("Такой категории не существует");
        }

    }

    // при ошибке счетчика truecount предыдущие операции сохраняются в базе
    @Transactional
    public void createQuiz(QuizzeDto quizzeDto, Long userId) {
        Long categoryId = getCategoryId(quizzeDto.getCategory());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "insert into quizzes(title, description, creator_id,category_id,minute_time) values(?, ?, ?, ?,?)"
                            ,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, quizzeDto.getTitle());
            ps.setString(2, quizzeDto.getDescription());
            ps.setLong(3, userId);
            ps.setLong(4,categoryId);
            ps.setInt(5, quizzeDto.getTimeLimit());
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
            if (trueCountHAHAHHAHA != 1) {log.info("должен быть один правильный ответ");throw new IllegalArgumentException("должен быть один правильный вариант");}
        }
    }

    public Long UserId(String username) {
        System.out.println(username);
        String sql = "select id from users where email = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, username);
    }

    public List<GetAllQuizDto> getAllQuiz() {
        List<GetAllQuizDto> getAllQuizDtos = new ArrayList<>();
        List<Long> quizzesId = jdbcTemplate.queryForList("select id from quizzes", Long.class);
        for (Long quizId : quizzesId) {
            String name = jdbcTemplate.queryForObject("select title from quizzes where id = ?", String.class, quizId);
            Long categoryId = jdbcTemplate.queryForObject("select category_id from quizzes where id = ?", Long.class, quizId);
            String categoryName = jdbcTemplate.queryForObject("select category_name from categories where id = ?", String.class, categoryId);
            List<Long> questionIds = jdbcTemplate.queryForList("select id from questions where quiz_id = ?", Long.class, quizId);
            getAllQuizDtos.add(new GetAllQuizDto(quizId, categoryName, name, questionIds.size()));
        }
        return getAllQuizDtos;
    }

    public List<QuizDto> getAll(PaginationParam param) {
        int offset = (param.getPage() - 1) * param.getLimit();

        if (param.getCategory() != null) {
            Long categoryId = getCategoryId(param.getCategory());
            String sql = "select id, title, description, category_id from quizzes " +
                    "where category_id = ? order by id limit ? offset ?";

            return jdbcTemplate.query(
                    sql,
                    new Object[]{categoryId, param.getLimit(), offset},
                    (rs, rowNum) -> new QuizDto(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getLong("category_id"),
                            rs.getString("description")
                    )
            );
        } else {
            String sql = "select id, title, description, category_id from quizzes order by id limit ? offset ?";

            return jdbcTemplate.query(
                    sql,
                    new Object[]{param.getLimit(), offset},
                    (rs, rowNum) -> new QuizDto(
                            rs.getLong("id"),
                            rs.getString("title"),
                            rs.getLong("category_id"),
                            rs.getString("description")
                    )
            );
        }
    }

    public Long getTime(Long quizId) {
        String sql = "select minute_time from quizzes where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Long.class, quizId);
        }catch (Exception e) {
            return null;
        }
    }

    public void createTimeToAnswer(Long quizId, Long answerId) {
        String sql = "insert into time_to (quiz_id, user_id) values (?, ?)";
        jdbcTemplate.update(sql, quizId, answerId);
    }

    public void deleteTime(Long quizId, Long answerId) {
        String sql = "delete from time_to where quiz_id = ? and user_id = ?";
        jdbcTemplate.update(sql, quizId, answerId);
    }

    public Time getStartsTime(Long quizId, Long userId) {
        return jdbcTemplate.queryForObject("select starts from time_to where quiz_id = ? and user_id = ?", Time.class, quizId, userId);
    }

    public Integer getTimeLimit(Long quizId) {
        String sql = "select minute_time from quizzes where id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, Integer.class, quizId);
        } catch (Exception e) {
            return null;
        }

    }
}