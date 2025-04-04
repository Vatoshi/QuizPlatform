package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.dto.groupedDto.GetAllQuizDto;
import kg.attractor.quizplatform.dto.modelsDto.OptionDto;
import kg.attractor.quizplatform.dto.modelsDto.QuestionDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizzeDto;
import kg.attractor.quizplatform.exeptions.NotFound;
import kg.attractor.quizplatform.util.PaginationParam;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
//import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageImpl;
//import org.springframework.data.domain.Pageable;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                    "insert into quizzes(title, description, creator_id,category_id) values(?, ?, ?, ?)"
                            ,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, quizzeDto.getTitle());
            ps.setString(2, quizzeDto.getDescription());
            ps.setLong(3, userId);
            ps.setLong(4,categoryId);
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


    public Long quizId(String name) {
        String sql = "select id from quizzes where title = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }

    public List<QuizDto> getAll(PaginationParam param) {
        if (param.getPage() == null || param.getPage() < 1) {
            param.setPage(1);
        }
        if (param.getLimit() == null || param.getLimit() < 1) {
            param.setLimit(5);
        }
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

}