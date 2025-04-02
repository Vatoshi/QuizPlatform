package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.QuizzeDao;
import kg.attractor.quizplatform.dto.QuizzeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizzeServise {
    private final QuizzeDao quizzeDao;

    public QuizzeDto createQuizze(QuizzeDto quizzeDto, String username) {
        Long userId = quizzeDao.UserId(username);
        quizzeDao.createQuiz(quizzeDto, userId);
        return quizzeDto;
    }
}
