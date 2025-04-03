package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.UserDao;
import kg.attractor.quizplatform.dao.UserStatisticDao;
import kg.attractor.quizplatform.dto.groupedDto.UserStatistic;
import kg.attractor.quizplatform.dto.modelsDto.UserDto;
import kg.attractor.quizplatform.exeptions.EmailExist;
import kg.attractor.quizplatform.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final UserStatisticDao userStatisticDao;

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

    public UserStatistic getUserStat(String username) {
        Long userId = userStatisticDao.UserId(username);
        Integer passedQuiz = userStatisticDao.CountAllPassedQuizzes(userId);
        Double averageScore = userStatisticDao.AverageScore(userId);
        String name = userStatisticDao.getName(userId);
        UserStatistic userStatistic = new UserStatistic(name,passedQuiz,averageScore);
        return userStatistic;
    }

    public List<UserStatistic> getUserStatisticsLeaderboard() {
        List<UserStatistic> userStatistics = new ArrayList<>();
        List<Long> usersId = userStatisticDao.getUsersId();
        for (Long userId : usersId) {
            String name = userStatisticDao.getName(userId);
            Double averageScore = userStatisticDao.AverageScore(userId);
            Integer passedQuiz = userStatisticDao.CountAllPassedQuizzes(userId);
            UserStatistic curentUser = new UserStatistic(name,passedQuiz,averageScore);
            userStatistics.add(curentUser);
        }

//        userStatistics.sort((user1, user2) -> user2.getQuizPassedCount().compareTo(user1.getQuizPassedCount()));
//        userStatistics.sort((user1, user2) -> user2.getAverageScore().compareTo(user1.getAverageScore()));
        return userStatistics;
    }
}
