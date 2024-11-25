package postman.bottler.mapletter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.mapletter.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.dto.response.OneLetterResponse;
import postman.bottler.mapletter.exception.EmptyMapLetterContentException;
import postman.bottler.mapletter.exception.EmptyMapLetterTargetException;
import postman.bottler.mapletter.exception.EmptyMapLetterTitleException;
import postman.bottler.mapletter.service.MapLetterService;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapLetterController {

    private final MapLetterService mapLetterService;

    @PostMapping("/public")
    public ApiResponse<?> createMapLetter(@Valid @RequestBody CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO,
                                          BindingResult bindingResult, Long userId) {
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

    @PostMapping("/target")
    public ApiResponse<?> createTargetLetter(@Valid @RequestBody CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO, BindingResult bindingResult, Long userId) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                if ("title".equals(error.getField())) {
                    throw new EmptyMapLetterTitleException(error.getDefaultMessage());
                } else if ("content".equals(error.getField())) {
                    throw new EmptyMapLetterContentException(error.getDefaultMessage());
                } else if ("target".equals(error.getField())) {
                    throw new EmptyMapLetterTargetException(error.getDefaultMessage());
                }
            });

            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage()); //기타 오류
        }
        mapLetterService.createTargetMapLetter(createTargetMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("타겟 편지 생성이 성공되었습니다.");
    }

    @GetMapping("/{letterId}")
    public ApiResponse<OneLetterResponse> findOneMapLetter(@PathVariable Long letterId, Long userId) {
        return ApiResponse.onSuccess(mapLetterService.findOneMepLetter(letterId, userId));
    }
}