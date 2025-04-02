package kg.attractor.quizplatform.controler;

import jakarta.validation.Valid;
import kg.attractor.quizplatform.dto.QuizzeDto;
import kg.attractor.quizplatform.servise.QuizzeServise;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("quiz")
public class QuizContoller {
    private final QuizzeServise quizzeService;

    @PostMapping("quizzes")
    public ResponseEntity<QuizzeDto> createQuiz(@Valid @RequestBody QuizzeDto quizzeDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        quizzeService.createQuizze(quizzeDto, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(quizzeDto);
    }
}
