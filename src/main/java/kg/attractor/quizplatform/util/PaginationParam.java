package kg.attractor.quizplatform.util;

import lombok.Data;

@Data
public class PaginationParam {
    private Integer page;
    private Integer limit;
    private String category;
}
