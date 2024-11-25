package postman.bottler.label.controller;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.label.dto.response.LabelResponseDTO;
import postman.bottler.label.service.LabelService;

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

    @GetMapping
    public ApiResponse<?> findAllLabels() {
        List<LabelResponseDTO> labelResponseDTO = labelService.findAllLabels();
        return ApiResponse.onCreateSuccess(labelResponseDTO);
    }
}
