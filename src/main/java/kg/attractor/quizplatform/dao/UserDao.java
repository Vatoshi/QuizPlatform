package kg.attractor.quizplatform.dao;


import kg.attractor.quizplatform.dto.UserDto;
import kg.attractor.quizplatform.exeptions.EmailExist;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDao {
    private JdbcTemplate jdbcTemplate;

    public void createUser(UserDto u) {
        EmailExist(u.getEmail());
        String sql = "insert into users (name, email, password) values (?, ?, ?)";
        jdbcTemplate.update(sql,u.getUsername(),u.getEmail(),u.getPassword());
    }

    public void EmailExist(String email) {
        String sql = "select id from users where email = ?";
        Integer id = jdbcTemplate.queryForObject(sql, Integer.class, email);
        if (id != null) {
            throw new EmailExist("account with email " + email + " already exist");
        }
    }
}
