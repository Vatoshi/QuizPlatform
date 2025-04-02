package kg.attractor.quizplatform.exeptions;

public class EmailExist extends RuntimeException {
    public EmailExist(String message) {
        super(message);
    }
}
