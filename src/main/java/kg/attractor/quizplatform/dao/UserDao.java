package kg.attractor.quizplatform.dao;


import kg.attractor.quizplatform.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserDao {
    private final JdbcTemplate jdbcTemplate;

    public void createUser(User u) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        u.setPassword(encoder.encode(u.getPassword()));
        String sql = "insert into users (username, password, email, enabled, role_id) values (?,?,?,?,?)";
        jdbcTemplate.update(sql, u.getUsername(), u.getPassword(), u.getEmail(), u.getEnabled(), u.getRoleId());
    }

    public String getEmailExist(String email) {
        String sql = "select email from users where email = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, email);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
