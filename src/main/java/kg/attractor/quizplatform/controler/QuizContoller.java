package kg.attractor.quizplatform.controler;

import jakarta.validation.Valid;
import kg.attractor.quizplatform.dto.groupedDto.GetAllQuizDto;
import kg.attractor.quizplatform.dto.groupedDto.HeaderWithQuesAndAnswer;
import kg.attractor.quizplatform.dto.groupedDto.HeaderWithQuiz;
import kg.attractor.quizplatform.dto.groupedDto.QuesAndAnswerDto;
import kg.attractor.quizplatform.dto.modelsDto.QuizzeDto;
import kg.attractor.quizplatform.servise.QuizzeServise;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("quiz")
public class QuizContoller {
    private final QuizzeServise quizzeService;

    @GetMapping
    public ResponseEntity<String> getAllQuizzes(){
        return ResponseEntity.status(HttpStatus.OK).body("WELCOME TO Quiz Platform, should register to play");
    }

    @PostMapping("quizzes")
    public ResponseEntity<QuizzeDto> createQuiz(@Valid @RequestBody QuizzeDto quizzeDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        quizzeService.createQuizze(quizzeDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(quizzeDto);
    }

    @GetMapping("quizzes")
    public ResponseEntity<List<GetAllQuizDto>> getAllQuiz() {
        return ResponseEntity.status(HttpStatus.FOUND).body(quizzeService.getQuizzes());
    }

    //получение квиза с варинатами ответа без ответа
    @GetMapping("quizzes/{quizId}")
    public ResponseEntity<HeaderWithQuiz> getQuiz(@PathVariable Long quizId) {
        return ResponseEntity.status(HttpStatus.OK).body(quizzeService.getQuizToAnswer(quizId));
    }

    //отправить ответы на данный по айди квиз
    @PostMapping("quizzes/{quizId}/solve")
    public ResponseEntity<HeaderWithQuesAndAnswer> getSolveQuiz(@PathVariable Long quizId, @RequestBody List<String> answers) {
        return ResponseEntity.status(HttpStatus.OK).body(quizzeService.getSolveQuiz(quizId, answers));
    }

//    @GetMapping("quizzes/{quizId}/")
}
