package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.QuizWithQuesDao;
import kg.attractor.quizplatform.dao.QuizzeDao;
import kg.attractor.quizplatform.dto.groupedDto.*;
import kg.attractor.quizplatform.dto.modelsDto.OptionsDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizzeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<QuizWithQuesDto> questions = quizWithQuesDao.getQuizToAnswer(quizId);
        List<QuesAndAnswerDto> solves = new ArrayList<>();
        String mark = "";
        int count = questions.size() - answers.size();
        if (count < 0 ) {throw new IllegalArgumentException("количество ответов больше чем вопросы");}
        if (count == 0) {
            mark = "вы ответили на все вопросы";
        } else {
            mark = "вы не ответили на " + count + " количество вопросов (автоматически засчитываются за неправильный ответ)";
        }
        for (int i = 0; i < answers.size(); i++) {
            String ques = questions.get(i).getQuestion();
            String correctOption = questions.get(i).getAnswers().stream()
                    .filter(OptionsDto::getIsCorrect)
                    .map(OptionsDto::getOption)
                    .findFirst()
                    .orElse(null);
            QuesAndAnswerDto opa = new QuesAndAnswerDto(ques,correctOption,answers.get(i));
            solves.add(opa);
        }
        HeaderWithQuesAndAnswer header = new HeaderWithQuesAndAnswer(mark,solves);
        return header;
    }
}
