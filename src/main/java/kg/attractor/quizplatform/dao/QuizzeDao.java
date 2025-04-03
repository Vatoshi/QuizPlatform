package kg.attractor.quizplatform.dao;

import kg.attractor.quizplatform.dto.groupedDto.GetAllQuizDto;
import kg.attractor.quizplatform.dto.modelsDto.OptionDto;
import kg.attractor.quizplatform.dto.modelsDto.QuestionDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizzeDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class QuizzeDao {
    private final JdbcTemplate jdbcTemplate;

    public String getQuizTitle(String quiztitle) {
        String sql = "select title from quizzes where title = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, quiztitle);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    // при ошибке счетчика truecount предыдущие операции сохраняются в базе
    @Transactional
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

    public List<GetAllQuizDto> getAllQuiz() {
        String sqlGetName = "select title from quizzes";
        List<String> quizNames = jdbcTemplate.queryForList(sqlGetName, String.class);
        List<GetAllQuizDto> getAllQuizDtos = new ArrayList<>();
        for(String quizName : quizNames) {
            String sql = "select q.id, q.quiz_id, qu.title "
                    + "from questions q "
                    + "join quizzes qu on q.quiz_id = qu.id "
                    + "where qu.title = ?";
            List<Map<String, Object>> questionList = jdbcTemplate.queryForList(sql, quizName);
            int sum = questionList.size();
            Long id = quizId(quizName);
            getAllQuizDtos.add(new GetAllQuizDto(id,quizName,sum));
        }
        return getAllQuizDtos;
    }


    public Long quizId(String name) {
        String sql = "select id from quizzes where title = ?";
        return jdbcTemplate.queryForObject(sql, Long.class, name);
    }
}