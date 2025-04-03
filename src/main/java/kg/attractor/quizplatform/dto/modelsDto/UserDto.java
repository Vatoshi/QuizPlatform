package kg.attractor.quizplatform.dto.modelsDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
    @NotNull(message = "обязательный ввод")
    @Size(min = 2, max = 20)
    String username;
    @NotNull(message = "обязательный ввод")
    @Pattern(regexp = "^(?=.*\\d).{5,}$", message = "длина должна быть минимум 5 символов и иметь хотя бы одну цифру")
    String password;
    @NotNull(message = "обязательный ввод")
    @Email(message = "неправильный ввод")
    String email;
}
