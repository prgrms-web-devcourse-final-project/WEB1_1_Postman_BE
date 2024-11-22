package postman.bottler.label.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;

@RestController
@RequestMapping("/labels")
public class LabelController {
    private final LabelService labelService;

    public LabelController(LabelService labelService) {
        this.labelService = labelService;
    }

    @PostMapping
    public ApiResponse<String> createLabel(@RequestParam String labelImageUrl) {
        labelService.createLabel(labelImageUrl);
        return ApiResponse.onCreateSuccess("라벨 추가 성공");
    }
}
