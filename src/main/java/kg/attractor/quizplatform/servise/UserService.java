package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.UserDao;
import kg.attractor.quizplatform.dto.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserDao userDao;

    public UserDto createUser(UserDto userDto) {
        userDao.createUser(userDto);
        return userDto;
    }

}
