package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.QuizzeDao;
import kg.attractor.quizplatform.dto.QuizzeDto;
import kg.attractor.quizplatform.dto.GetAllQuizDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizzeServise {
    private final QuizzeDao quizzeDao;

    public QuizzeDto createQuizze(QuizzeDto quizzeDto, String username) {
        if (quizzeDto.getTitle().equals(quizzeDao.getQuizTitle(quizzeDto.getTitle()))) {
            throw new IllegalArgumentException("Quiz like your already exists (quiz title is unique)");
        }
        Long userId = quizzeDao.UserId(username);
        quizzeDao.createQuiz(quizzeDto, userId);
        return quizzeDto;
    }

    public List<GetAllQuizDto> getQuizzes() {
        return quizzeDao.getAllQuiz();
    }
}
