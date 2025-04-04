package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.UserDao;
import kg.attractor.quizplatform.dao.UserStatisticDao;
import kg.attractor.quizplatform.dto.groupedDto.UserStatistic;
import kg.attractor.quizplatform.dto.modelsDto.UserDto;
import kg.attractor.quizplatform.exeptions.EmailExist;
import kg.attractor.quizplatform.exeptions.NotFound;
import kg.attractor.quizplatform.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;
    private final UserStatisticDao userStatisticDao;

    public UserDto createUser(UserDto userDto) {
        if (userDto.getEmail().equals(userDao.getEmailExist(userDto.getEmail()))) {
            log.info("пользователь с такой почтой уже существует");
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
        log.info("пользователь создан");
        return userDto;
    }

    public UserStatistic getUserStat(Long userId) {
        Integer passedQuiz = userStatisticDao.CountAllPassedQuizzes(userId);
        if (passedQuiz == null) { throw new NotFound("The user has no statistics (has not completed any quizzes) or not exist");}
        Double averageScore = userStatisticDao.AverageScore(userId);
        String name = userStatisticDao.getName(userId);
        log.info("отправка статистики");
        return new UserStatistic(name,passedQuiz,averageScore);
    }

    public List<UserStatistic> getUserStatisticsLeaderboard() {
        List<UserStatistic> userStatistics = new ArrayList<>();
        List<Long> usersId = userStatisticDao.getUsersId();
        for (Long userId : usersId) {
            String name = userStatisticDao.getName(userId);
            Double averageScore = userStatisticDao.AverageScore(userId);
            Integer passedQuiz = userStatisticDao.CountAllPassedQuizzes(userId);
            if (passedQuiz == null) {continue;}
            UserStatistic curentUser = new UserStatistic(name,passedQuiz,averageScore);
            userStatistics.add(curentUser);
        }
        userStatistics.sort((user1, user2) -> user2.getAverageScore().compareTo(user1.getAverageScore()));
        log.info("лучшие игроки квиза");
        return userStatistics;
    }
}
