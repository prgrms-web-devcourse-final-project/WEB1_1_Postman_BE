package postman.bottler.mapletter.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.mapletter.dto.PaperDTO;
import postman.bottler.mapletter.service.PaperService;

import java.util.List;

@RestController
@RequestMapping("/paper")
@RequiredArgsConstructor
public class PaperController {
    private final PaperService paperService;

    @GetMapping
    public ApiResponse<List<PaperDTO>> findPapers() {
        return ApiResponse.onSuccess(paperService.findPapers());
    }
}
