package postman.bottler.mapletter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.mapletter.dto.request.*;
import postman.bottler.mapletter.dto.response.*;
import postman.bottler.mapletter.exception.*;
import postman.bottler.mapletter.service.BlockMapLetterType;
import postman.bottler.mapletter.service.MapLetterService;

import java.math.BigDecimal;
import java.util.List;

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
                } else if("description".equals(error.getField())) {
                    throw new EmptyMapLetterDescriptionException(error.getDefaultMessage());
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
                }else if("description".equals(error.getField())) {
                    throw new EmptyMapLetterDescriptionException(error.getDefaultMessage());
                }
            });

            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage()); //기타 오류
        }
        mapLetterService.createTargetMapLetter(createTargetMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("타겟 편지 생성이 성공되었습니다.");
    }

    @GetMapping("/{letterId}")
    public ApiResponse<OneLetterResponseDTO> findOneMapLetter(@PathVariable Long letterId, Long userId) {
        return ApiResponse.onSuccess(mapLetterService.findOneMepLetter(letterId, userId));
    }

    @DeleteMapping
    public ApiResponse<?> deleteMapLetter(@RequestBody DeleteMapLettersRequestDTO letters, Long userId) {
        mapLetterService.deleteMapLetter(letters.letterIds(), userId);
        return ApiResponse.onDeleteSuccess(letters);
    }

    @GetMapping("/sent")
    public ApiResponse<List<FindMapLetterResponseDTO>> findSentMapLetters(Long userId) {
        return ApiResponse.onSuccess(mapLetterService.findSentMapLetters(userId));
    }

    @GetMapping("/received")
    public ApiResponse<List<FindReceivedMapLetterResponseDTO>> findReceivedMapLetters(Long userId) {
        return ApiResponse.onSuccess(mapLetterService.findReceivedMapLetters(userId));
    }

    @GetMapping
    public ApiResponse<List<FindNearbyLettersResponseDTO>> findNearbyMapLetters(@RequestParam String latitude, @RequestParam String longitude, Long userId) {
        BigDecimal lat = BigDecimal.ZERO;
        BigDecimal lon = BigDecimal.ZERO;
        try {
            lat=new BigDecimal(latitude);
            lon=new BigDecimal(longitude);
        }catch (Exception e) {
            throw new LocationNotFoundException("해당 위치를 찾을 수 없습니다.");
        }

        return ApiResponse.onSuccess(mapLetterService.findNearByMapLetters(lat, lon, userId));
    }

    @PostMapping("/reply")
    public ApiResponse<?> createReplyMapLetter(
            @Valid @RequestBody CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO,
            BindingResult bindingResult, Long userId) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                if ("content".equals(error.getField())) {
                    throw new EmptyMapLetterContentException(error.getDefaultMessage());
                } else if ("sourceLetter".equals(error.getField())) {
                    throw new EmptyReplyMapLetterSourceException(error.getDefaultMessage());
                }
            });

            throw new IllegalArgumentException(bindingResult.getAllErrors().get(0).getDefaultMessage()); //기타 오류
        }
        mapLetterService.createReplyMapLetter(createReplyMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("답장 편지 생성이 성공되었습니다.");
    }

    @GetMapping("/{letterId}/reply")
    public ApiResponse<List<FindAllReplyMapLettersResponseDTO>> findAllReplyMapLetter(@PathVariable Long letterId, Long userId) {
        return ApiResponse.onSuccess(mapLetterService.findAllReplyMapLetter(letterId, userId));
    }

    @GetMapping("/reply/{letterId}")
    public ApiResponse<OneReplyLetterResponseDTO> findOneReplyMapLetter(@PathVariable Long letterId, Long userId){
        return ApiResponse.onSuccess(mapLetterService.findOneReplyMapLetter(letterId, userId));
    }

    @PostMapping("/{letterId}")
    public ApiResponse<?> mapLetterArchive(@PathVariable Long letterId, Long userId) {
        mapLetterService.mapLetterArchive(letterId, userId);
        return ApiResponse.onCreateSuccess("편지 저장이 성공되었습니다.");
    }

    @GetMapping("/archived")
    public ApiResponse<List<FindAllArchiveLetters>> findArchiveLetters(Long userId) {
        return ApiResponse.onSuccess(mapLetterService.findArchiveLetters(userId));
    }

    @DeleteMapping("/archived")
    public ApiResponse<?> archiveLetter(@RequestBody DeleteArchivedLettersRequestDTO deleteArchivedLettersRequestDTO
            , Long userId) {
        mapLetterService.deleteArchivedLetter(deleteArchivedLettersRequestDTO, userId);
        return ApiResponse.onDeleteSuccess(deleteArchivedLettersRequestDTO);
    }

    @GetMapping("/reply/check/{letterId}")
    public ApiResponse<CheckReplyMapLetterResponseDTO> checkReplyMapLetter(@PathVariable Long letterId, Long userId) {
        return ApiResponse.onSuccess(mapLetterService.checkReplyMapLetter(letterId, userId));
    }

    @DeleteMapping("/reply")
    public ApiResponse<?> deleteReplyMapLetter(@RequestBody DeleteMapLettersRequestDTO letters, Long userId) {
        mapLetterService.deleteReplyMapLetter(letters.letterIds(), userId);
        return ApiResponse.onDeleteSuccess(letters);
    }
}
