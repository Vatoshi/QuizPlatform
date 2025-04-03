package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.QuizWithQuesDao;
import kg.attractor.quizplatform.dao.QuizzeDao;
import kg.attractor.quizplatform.dto.groupedDto.GetAllQuizDto;
import kg.attractor.quizplatform.dto.groupedDto.HeaderWithQuesAndAnswer;
import kg.attractor.quizplatform.dto.groupedDto.HeaderWithQuiz;
import kg.attractor.quizplatform.dto.groupedDto.QuesAndAnswerDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizzeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizzeServise {
    private final QuizzeDao quizzeDao;
    private final QuizWithQuesDao quizWithQuesDao;

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

    public HeaderWithQuiz getQuizToAnswer(Long quizId) {
        String quizTitle = quizWithQuesDao.getTitle(quizId);
        HeaderWithQuiz headerWithQuiz = new HeaderWithQuiz(quizTitle,
                "Отправлять ответы поочередно с верху в вниз, АЙДИ данного квиза " + quizId
                + ". Также не забывайте пройти квиз можно лишь раз",
                quizWithQuesDao.getQuizToAnswer(quizId));
        return headerWithQuiz;
    }

    public HeaderWithQuesAndAnswer getSolveQuiz(Long quizId, List<String> answers) {
        return quizWithQuesDao.getSolveQuiz(quizId, answers);
    }
}
