package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.UserDao;
import kg.attractor.quizplatform.dto.modelsDto.UserDto;
import kg.attractor.quizplatform.exeptions.EmailExist;
import kg.attractor.quizplatform.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail().equals(userDao.getEmailExist(userDto.getEmail()))) {
            throw new EmailExist("account with email " + userDto.getEmail() + " already exist");
        }

        User createdUser = User.builder()
                .username(userDto.getUsername())
                .password(userDto.getPassword())
                .email(userDto.getEmail())
                .enabled(true)
                .roleId(1L)
                .build();
        userDao.createUser(createdUser);
        return userDto;
    }

}
