package kg.attractor.quizplatform.servise;

import kg.attractor.quizplatform.dao.QuizResultsDao;
import kg.attractor.quizplatform.dao.QuizWithQuesDao;
import kg.attractor.quizplatform.dao.QuizzeDao;
import kg.attractor.quizplatform.dto.groupedDto.*;
import kg.attractor.quizplatform.dto.modelsDto.OptionsDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizzeDto;
import kg.attractor.quizplatform.exeptions.NotFound;
import kg.attractor.quizplatform.util.PaginationParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuizzeServise {
    private final QuizzeDao quizzeDao;
    private final QuizWithQuesDao quizWithQuesDao;
    private final QuizResultsDao quizResultsDao;

    public QuizzeDto createQuizze(QuizzeDto quizzeDto, String username) {
        if (quizzeDto.getTitle().equals(quizzeDao.getQuizTitle(quizzeDto.getTitle()))) {
            log.info("Quiz with title '{}' already exists", quizzeDto.getTitle());
            throw new IllegalArgumentException("Quiz like your already exists (quiz title is unique)");
        }
        Long userId = quizzeDao.UserId(username);
        quizzeDao.createQuiz(quizzeDto, userId);
        log.info("Created quiz with title '{}'", quizzeDto.getTitle());
        return quizzeDto;
    }

    public List<GetAllQuizDto> getQuizzes() {
        log.info("Retrieving quizzes");
        return quizzeDao.getAllQuiz();
    }

    public HeaderWithQuiz getQuizToAnswer(Long quizId, String username) {
        Long userId = quizzeDao.UserId(username);
        Integer timeLimit = quizzeDao.getTimeLimit(quizId);
        String quizTitle = quizWithQuesDao.getTitle(quizId);
        String category = quizzeDao.getCategory(quizId);
        HeaderWithQuiz headerWithQuiz = new HeaderWithQuiz(quizTitle, category, timeLimit,
                "Отправлять ответы поочередно с верху в вниз, АЙДИ данного квиза " + quizId
                + ". Также не забывайте пройти квиз можно лишь раз",
                quizWithQuesDao.getQuizToAnswer(quizId));
        log.info("Retrieved quiz with title '{}'", quizTitle);
        quizzeDao.deleteTime(quizId, userId);
        quizzeDao.createTimeToAnswer(quizId,userId);
        return headerWithQuiz;
    }

    public HeaderWithQuesAndAnswer getSolveQuiz(Long quizId, List<String> answers, String username) {
        List<QuizWithQuesDto> questions = quizWithQuesDao.getQuizToAnswer(quizId);
        List<QuesAndAnswerDto> solves = new ArrayList<>();
        String mark = "";
        int count = questions.size() - answers.size();
        if (count < 0 ) {
            log.info("количество ответов больше чем вопросы либо квиза не существует");
            throw new IllegalArgumentException("количество ответов больше чем вопросы либо квиза не существует");}
        if (count == 0) {
            mark = "вы ответили на все вопросы";
        } else {
            mark = "вы не ответили на " + count + " количество вопросов (автоматически засчитываются за неправильный ответ)";
            while (answers.size() < questions.size()) {
                answers.add("");
            }
        }
        for (int i = 0; i < questions.size(); i++) {
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
        Long userId = quizzeDao.UserId(username);
        Integer i = quizResultsDao.getQuizResult(userId, quizId);
        Integer minute = quizzeDao.getTime(quizId);
        System.out.println(minute);
        LocalDateTime startssTime = quizzeDao.getStartsTime(quizId, userId);
        System.out.println(startssTime);
        if (minute == null) {
            if (i == null) {
                quizResults(header, userId, quizId);
            } else {
                log.info("Возможно вы уже прошли данный квиз");
                throw new IllegalArgumentException("Возможно вы уже прошли данный квиз");
            }
        } else {
            LocalDateTime startTime = quizzeDao.getStartsTime(quizId, userId);

            int durationSeconds = quizzeDao.getTime(quizId);

            LocalDateTime now = LocalDateTime.now();

            long secondsBetween = ChronoUnit.SECONDS.between(startTime, now);

            System.out.println("Start time: " + startTime);
            System.out.println("Current time: " + now);
            System.out.println("Seconds passed: " + secondsBetween);
            System.out.println("Allowed duration: " + durationSeconds);

            if (secondsBetween > durationSeconds) {
                if (i == null) {
                    log.info("Пользователь не успел по времени (просрочено {} секунд)", secondsBetween - durationSeconds);
                    quizResultsTime(header, userId, quizId);
                } else {
                    log.warn("Пользователь уже завершил квиз");
                    throw new IllegalArgumentException("Вы уже завершили этот квиз");
                }
            } else {
                if (i == null) {
                    log.info("Ответ принят в срок (осталось {} секунд)", durationSeconds - secondsBetween);
                    quizResults(header, userId, quizId);
                } else {
                    log.warn("Попытка повторной отправки ответов");
                    throw new IllegalArgumentException("Вы уже завершили этот квиз");
                }
            }
        }
        quizzeDao.deleteTime(quizId, userId);
        log.info("юзер ответил");
        return header;
    }

    private void quizResults (HeaderWithQuesAndAnswer headerWithQuesAndAnswer, Long userId, Long quizId) {
        Integer score = 0;
        for (QuesAndAnswerDto i : headerWithQuesAndAnswer.getQuesAndAnswerDtos()) {
            if (i.getCorrectAnswer().equals(i.getYourAnswer())) {
                score = score + 10;
            }
        }
        quizResultsDao.SetQuizResult(userId, quizId, score);
    }

    private void quizResultsTime (HeaderWithQuesAndAnswer headerWithQuesAndAnswer, Long userId, Long quizId) {
        Integer score = -50;
        for (QuesAndAnswerDto i : headerWithQuesAndAnswer.getQuesAndAnswerDtos()) {
            if (i.getCorrectAnswer().equals(i.getYourAnswer())) {
                score = score + 10;
            }
        }
        quizResultsDao.SetQuizResult(userId, quizId, score);
    }

    public GetScoreDto getQuizResults (Long quizId, String username) {
        Long userId = quizzeDao.UserId(username);

        Long getIdResults = quizResultsDao.getQuizResultInteger(quizId, userId);
        List<Long> questionIds = quizResultsDao.questionId(getIdResults);
        Integer answerscount = questionIds.size();
        Integer score = quizResultsDao.getQuizResult(userId, quizId);
        Integer correctAnswersCount = score / 10;
        Integer rating = quizResultsDao.getRating(userId, quizId);
        GetScoreDto getScore = new GetScoreDto(correctAnswersCount, answerscount,score,rating);
        log.info("результаты по '{}'",quizId);
        return getScore;
    }

    public ScoreDto SetScore (String username, Long quizId, ScoreDto score) {
        Long userId = quizzeDao.UserId(username);
        Integer rating = quizResultsDao.getRating(userId, quizId);
        if (rating != null) {throw new IllegalArgumentException("Вы уже поставили оценку");}
        try {
            Long resultsId = Long.valueOf(quizResultsDao.getQuizResultsId(userId, quizId));
            quizResultsDao.SetRating(resultsId, score);
            return score;
        } catch (Exception e) {
            log.info("пользователь не прошел данный квиз");
            throw new NotFound("Нельзя дать оценку без прохождения");
        }
    }

    public List<LeaderBoardDto> GetRating(Long quizId) {
        List<Map<String, Object>> users = quizResultsDao.getUsers(quizId);
        List<LeaderBoardDto> ratings = new ArrayList<>();
        for (Map<String, Object> row : users) {
            String username = (String) row.get("username");
            Double score = (Double) row.get("score");

            ratings.add(new LeaderBoardDto(username, score));
        }
        ratings = ratings.stream()
                .sorted(Comparator.comparing(LeaderBoardDto::getScore, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        log.info("лидерборд");
        return ratings;
    }

    public List<QuizDto> getQuizzes(int page, int limit, String category) {
        PaginationParam request = new PaginationParam();
        request.setPage(page);
        request.setLimit(limit);
        request.setCategory(category);
        log.info("отправка викторин");
        return quizzeDao.getAll(request);
    }
}
