package kg.attractor.quizplatform.controler;

import jakarta.validation.Valid;
import kg.attractor.quizplatform.dto.groupedDto.UserStatistic;
import kg.attractor.quizplatform.dto.modelsDto.UserDto;
import kg.attractor.quizplatform.servise.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody UserDto userDto) {
        userService.createUser(userDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping("/{userId}/statistics")
    public ResponseEntity<UserStatistic> getUserStatistic(@PathVariable Long userId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserStat(userId));
    }

    @GetMapping("/global-statistics")
    public ResponseEntity<List<UserStatistic>> getGlobalStatistics() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserStatisticsLeaderboard());
    }
}
