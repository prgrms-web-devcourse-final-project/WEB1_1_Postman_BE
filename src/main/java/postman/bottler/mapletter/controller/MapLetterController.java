package postman.bottler.mapletter.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.mapletter.dto.request.*;
import postman.bottler.mapletter.dto.response.*;
import postman.bottler.mapletter.exception.*;
import postman.bottler.mapletter.service.MapLetterService;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
public class MapLetterController {

    private final MapLetterService mapLetterService;

    private void validateMapLetterRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(error -> {
                switch (error.getField()) {
                    case "title":
                        throw new EmptyMapLetterTitleException(error.getDefaultMessage());
                    case "description":
                        throw new EmptyMapLetterDescriptionException(error.getDefaultMessage());
                    case "content":
                        throw new EmptyMapLetterContentException(error.getDefaultMessage());
                    case "target":
                        throw new EmptyMapLetterTargetException(error.getDefaultMessage());
                    case "sourceLetter":
                        throw new EmptyReplyMapLetterSourceException(error.getDefaultMessage());
                    default:
                        throw new IllegalArgumentException(
                                bindingResult.getAllErrors().get(0).getDefaultMessage()); //기타 오류
                }
            });
        }
    }

    @PostMapping("/public")
    public ApiResponse<?> createMapLetter(
            @Valid @RequestBody CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO,
            BindingResult bindingResult, Long userId) {
        validateMapLetterRequest(bindingResult);
        mapLetterService.createPublicMapLetter(createPublicMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("지도 편지 생성이 성공되었습니다.");
    }

    @PostMapping("/target")
    public ApiResponse<?> createTargetLetter(
            @Valid @RequestBody CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO,
            BindingResult bindingResult, Long userId) {
        validateMapLetterRequest(bindingResult);
        mapLetterService.createTargetMapLetter(createTargetMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("타겟 편지 생성이 성공되었습니다.");
    }

    @GetMapping("/{letterId}")
    public ApiResponse<OneLetterResponseDTO> findOneMapLetter(@RequestParam String latitude,
                                                              @RequestParam String longitude,
                                                              @PathVariable Long letterId, Long userId) {
        BigDecimal lat = BigDecimal.ZERO;
        BigDecimal lon = BigDecimal.ZERO;
        try {
            lat = new BigDecimal(latitude);
            lon = new BigDecimal(longitude);
        } catch (Exception e) {
            throw new LocationNotFoundException("해당 위치를 찾을 수 없습니다.");
        }

        return ApiResponse.onSuccess(mapLetterService.findOneMapLetter(letterId, userId, lat, lon));
    }

    @DeleteMapping
    public ApiResponse<?> deleteMapLetter(@RequestBody DeleteMapLettersRequestDTO letters, Long userId) {
        mapLetterService.deleteMapLetter(letters.letterIds(), userId);
        return ApiResponse.onDeleteSuccess(letters);
    }

    @GetMapping("/sent")
    public ApiResponse<MapLetterPageResponseDTO<FindMapLetterResponseDTO>> findSentMapLetters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size, Long userId) {
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findSentMapLetters(page, size, userId)));
    }

    @GetMapping("/received")
    public ApiResponse<MapLetterPageResponseDTO<FindReceivedMapLetterResponseDTO>> findReceivedMapLetters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Long userId) {
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findReceivedMapLetters(page, size, userId)));
    }

    @GetMapping
    public ApiResponse<List<FindNearbyLettersResponseDTO>> findNearbyMapLetters(@RequestParam String latitude,
                                                                                @RequestParam String longitude,
                                                                                Long userId) {
        BigDecimal lat = BigDecimal.ZERO;
        BigDecimal lon = BigDecimal.ZERO;
        try {
            lat = new BigDecimal(latitude);
            lon = new BigDecimal(longitude);
        } catch (Exception e) {
            throw new LocationNotFoundException("해당 위치를 찾을 수 없습니다.");
        }

        return ApiResponse.onSuccess(mapLetterService.findNearByMapLetters(lat, lon, userId));
    }

    @PostMapping("/reply/{userId}")
    public ApiResponse<?> createReplyMapLetter(
            @Valid @RequestBody CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO,
            BindingResult bindingResult, @PathVariable Long userId) {
        validateMapLetterRequest(bindingResult);
        mapLetterService.createReplyMapLetter(createReplyMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("답장 편지 생성이 성공되었습니다.");
    }

    @GetMapping("/{letterId}/reply")
    public ApiResponse<MapLetterPageResponseDTO<FindAllReplyMapLettersResponseDTO>> findAllReplyMapLetter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @PathVariable Long letterId,
            Long userId) {
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findAllReplyMapLetter(page, size, letterId, userId)));
    }

    @GetMapping("/reply/{letterId}")
    public ApiResponse<OneReplyLetterResponseDTO> findOneReplyMapLetter(@PathVariable Long letterId, Long userId) {
        return ApiResponse.onSuccess(mapLetterService.findOneReplyMapLetter(letterId, userId));
    }

    @PostMapping("/{letterId}")
    public ApiResponse<?> mapLetterArchive(@PathVariable Long letterId, Long userId) {
        mapLetterService.mapLetterArchive(letterId, userId);
        return ApiResponse.onCreateSuccess("편지 저장이 성공되었습니다.");
    }

    @GetMapping("/archived")
    public ApiResponse<MapLetterPageResponseDTO<FindAllArchiveLetters>> findArchiveLetters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Long userId) {
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findArchiveLetters(page, size, userId)));
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

    @GetMapping("/archive/{letterId}")
    public ApiResponse<OneLetterResponseDTO> findArchiveOneLetter(@PathVariable Long letterId, Long userId) {
        return ApiResponse.onSuccess(mapLetterService.findArchiveOneLetter(letterId, userId));
    }

    @GetMapping("/sent/reply")
    public ApiResponse<MapLetterPageResponseDTO<FindAllSentReplyMapLetterResponseDTO>> findAllSentReplyMapLetter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Long userId) {
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findAllSentReplyMapLetter(page, size, userId)));
    }

    @GetMapping("/sent/letter")
    public ApiResponse<MapLetterPageResponseDTO<FindAllSentMapLetterResponseDTO>> findAllSentMapLetter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Long userId) {
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findAllSentMapLetter(page, size, userId)));
    }

    @GetMapping("/received/reply")
    public ApiResponse<MapLetterPageResponseDTO<FindAllReceivedReplyLetterResponseDTO>> findAllReceivedReplyMapLetter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Long userId
    ) {
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findAllReceivedReplyLetter(page, size, userId)));
    }

    @GetMapping("/received/letter")
    public ApiResponse<MapLetterPageResponseDTO<FindAllReceivedLetterResponseDTO>> findAllReceivedMapLetter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            Long userId
    ){
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findAllReceivedLetter(page, size, userId)));
    }

}
