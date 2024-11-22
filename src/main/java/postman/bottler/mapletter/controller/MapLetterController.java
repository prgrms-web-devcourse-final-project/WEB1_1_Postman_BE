package postman.bottler.mapletter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.exception.EmptyMapLetterContentException;
import postman.bottler.mapletter.exception.EmptyMapLetterTitleException;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapLetterController {

    private final MapLetterService mapLetterService;

    @PostMapping("/public")
    public ApiResponse<?> createMapLetter(@RequestBody @Valid CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO, Long userId,
                                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                if ("title".equals(error.getField())) {
                    throw new EmptyMapLetterTitleException(error.getDefaultMessage());
                } else if ("content".equals(error.getField())) {
                    throw new EmptyMapLetterContentException(error.getDefaultMessage());
                }
            });

            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage()); //기타 오류
        }
        mapLetterService.createPublicMapLetter(createPublicMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("지도 편지 생성이 성공되었습니다.");
    }
}
