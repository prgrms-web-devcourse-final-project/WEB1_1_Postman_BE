package postman.bottler.mapletter.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.global.response.ApiResponse;
import postman.bottler.mapletter.application.dto.request.CreatePublicMapLetterRequestDTO;
import postman.bottler.mapletter.application.dto.request.CreateReplyMapLetterRequestDTO;
import postman.bottler.mapletter.application.dto.request.CreateTargetMapLetterRequestDTO;
import postman.bottler.mapletter.application.dto.request.DeleteArchivedLettersRequestDTO;
import postman.bottler.mapletter.application.dto.request.DeleteArchivedLettersRequestDTOV1;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersRequestDTO;
import postman.bottler.mapletter.application.dto.request.DeleteMapLettersV1RequestDTO;
import postman.bottler.mapletter.application.dto.response.CheckReplyMapLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindAllArchiveLetters;
import postman.bottler.mapletter.application.dto.response.FindAllReplyMapLettersResponseDTO;
import postman.bottler.mapletter.application.dto.response.FindNearbyLettersResponseDTO;
import postman.bottler.mapletter.application.dto.response.MapLetterPageResponseDTO;
import postman.bottler.mapletter.application.dto.response.OneLetterResponseDTO;
import postman.bottler.mapletter.application.dto.response.OneReplyLetterResponseDTO;
import postman.bottler.mapletter.exception.EmptyMapLetterContentException;
import postman.bottler.mapletter.exception.EmptyMapLetterDescriptionException;
import postman.bottler.mapletter.exception.EmptyMapLetterTargetException;
import postman.bottler.mapletter.exception.EmptyMapLetterTitleException;
import postman.bottler.mapletter.exception.EmptyReplyMapLetterSourceException;
import postman.bottler.mapletter.exception.LocationNotFoundException;
import postman.bottler.mapletter.application.service.MapLetterService;
import postman.bottler.mapletter.exception.TypeNotFoundException;
import postman.bottler.user.auth.CustomUserDetails;

@RestController
@RequestMapping("/map")
@RequiredArgsConstructor
@Tag(name = "지도 편지 컨트롤러")
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
    @Operation(summary = "지도 퍼블릭 편지 생성", description = "로그인 필수. 제목 없으면 무제로 넣어주세요.")
    public ApiResponse<?> createMapLetter(
            @Valid @RequestBody CreatePublicMapLetterRequestDTO createPublicMapLetterRequestDTO,
            BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        validateMapLetterRequest(bindingResult);
        mapLetterService.createPublicMapLetter(createPublicMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("지도 편지 생성이 성공되었습니다.");
    }

    @PostMapping("/target")
    @Operation(summary = "지도 타겟 편지 생성", description = "로그인 필수. 제목 없으면 무제로 넣어주세요.")
    public ApiResponse<?> createTargetLetter(
            @Valid @RequestBody CreateTargetMapLetterRequestDTO createTargetMapLetterRequestDTO,
            BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        validateMapLetterRequest(bindingResult);
        mapLetterService.createTargetMapLetter(createTargetMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("타겟 편지 생성이 성공되었습니다.");
    }

    @GetMapping("/{letterId}")
    @Operation(summary = "편지 상세 조회", description = "로그인 필수. 위경도 필수. 반경 15m 내 편지만 상세조회 가능. 내가 타겟인 편지와 퍼블릭 편지만 조회 가능. 나머지는 오류")
    public ApiResponse<OneLetterResponseDTO> findOneMapLetter(@RequestParam String latitude,
                                                              @RequestParam String longitude,
                                                              @PathVariable Long letterId,
                                                              @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
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

    @GetMapping
    @Operation(summary = "주변 편지 조회", description = "로그인 필수. 반경 500m 내 퍼블릭 편지, 나에게 타겟으로 온 편지 조회")
    public ApiResponse<List<FindNearbyLettersResponseDTO>> findNearbyMapLetters(@RequestParam String latitude,
                                                                                @RequestParam String longitude,
                                                                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
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

    @PostMapping("/reply")
    @Operation(summary = "답장 편지 생성", description = "로그인 필수. 지도편지 답장을 생성한다.")
    public ApiResponse<?> createReplyMapLetter(
            @Valid @RequestBody CreateReplyMapLetterRequestDTO createReplyMapLetterRequestDTO,
            BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        validateMapLetterRequest(bindingResult);
        mapLetterService.createReplyMapLetter(createReplyMapLetterRequestDTO, userId);
        return ApiResponse.onCreateSuccess("답장 편지 생성이 성공되었습니다.");
    }

    @GetMapping("/{letterId}/reply")
    @Operation(summary = "특정 편지 답장 전체보기", description = "로그인 필수. 특정 편지에 대한 답장을 조회한다.")
    public ApiResponse<MapLetterPageResponseDTO<FindAllReplyMapLettersResponseDTO>> findAllReplyMapLetter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @PathVariable Long letterId, @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findAllReplyMapLetter(page, size, letterId, userId)));
    }

    @GetMapping("/reply/{letterId}")
    @Operation(summary = "답장 편지 상세 조회", description = "로그인 필수. 답장 편지를 상세조회한다.")
    public ApiResponse<OneReplyLetterResponseDTO> findOneReplyMapLetter(@PathVariable Long letterId,
                                                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.onSuccess(mapLetterService.findOneReplyMapLetter(letterId, userId));
    }

    @PostMapping("/{letterId}")
    @Operation(summary = "편지 보관", description = "로그인 필수. 퍼블릭 편지를 보관한다.")
    public ApiResponse<?> mapLetterArchive(@PathVariable Long letterId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        mapLetterService.mapLetterArchive(letterId, userId);
        return ApiResponse.onCreateSuccess("편지 저장이 성공되었습니다.");
    }

    @GetMapping("/archived")
    @Operation(summary = "보관한 편지 조회", description = "로그인 필수. 보관한 편지를 조회한다.")
    public ApiResponse<MapLetterPageResponseDTO<FindAllArchiveLetters>> findArchiveLetters(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "9") int size,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.onSuccess(
                MapLetterPageResponseDTO.from(mapLetterService.findArchiveLetters(page, size, userId)));
    }

    @DeleteMapping("/archived")
    @Operation(summary = "편지 보관 취소(삭제) 프론트 v2로 변경 후 삭제 할 코드입니다.", description = "로그인 필수. 편지를 보관함에서 삭제한다.(리스트로 1~n개 까지의 편지를 한 번에 삭제 한다.")
    public ApiResponse<?> archiveLetterV1(@RequestBody DeleteArchivedLettersRequestDTOV1 deleteArchivedLettersRequestDTO
            , @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        mapLetterService.deleteArchivedLetter(deleteArchivedLettersRequestDTO, userId);
        return ApiResponse.onDeleteSuccess(deleteArchivedLettersRequestDTO);
    }

    @DeleteMapping("/archived/v2")
    @Operation(summary = "편지 보관 취소(삭제) 변경 한 코드입니다.", description = "로그인 필수. 편지를 보관함에서 삭제한다.(리스트로 1~n개 까지의 편지를 한 번에 삭제 한다.")
    public ApiResponse<?> archiveLetter(@RequestBody DeleteArchivedLettersRequestDTO deleteArchivedLettersRequestDTO
            , @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        mapLetterService.deleteArchivedLetter(deleteArchivedLettersRequestDTO, userId);
        return ApiResponse.onDeleteSuccess(deleteArchivedLettersRequestDTO);
    }

    @GetMapping("/reply/check/{letterId}")
    @Operation(summary = "유저가 해당 편지에 답장을 보냈는지 확인합니다.", description = "로그인 필수. 편지 보관할 때 이미 답장이 있으면 에러를 터트리지만 혹시 몰라서 넣었어요.")
    public ApiResponse<CheckReplyMapLetterResponseDTO> checkReplyMapLetter(@PathVariable Long letterId,
                                                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.onSuccess(mapLetterService.checkReplyMapLetter(letterId, userId));
    }

    @GetMapping("/archive/{letterId}")
    @Operation(summary = "마이페이지에 있는 편지 상세 조회", description = "로그인 필수. 기존 편지 상세 조회는 15m 내에 있는 편지만 조회가 가능해서 거리 제한 없이 편지 조회가 가능한 api 별도 생성")
    public ApiResponse<OneLetterResponseDTO> findArchiveOneLetter(@PathVariable Long letterId,
                                                                  @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        return ApiResponse.onSuccess(mapLetterService.findArchiveOneLetter(letterId, userId));
    }

    @GetMapping("/guest")
    @Operation(summary = "로그인 하지 않은 유저 주변 편지 조회", description = "로그인 하지 않은 유저의 반경 500m 내 퍼블릭 편지 조회")
    public ApiResponse<List<FindNearbyLettersResponseDTO>> guestFindNearbyMapLetters(@RequestParam String latitude,
                                                                                     @RequestParam String longitude) {
        BigDecimal lat = BigDecimal.ZERO;
        BigDecimal lon = BigDecimal.ZERO;
        try {
            lat = new BigDecimal(latitude);
            lon = new BigDecimal(longitude);
        } catch (Exception e) {
            throw new LocationNotFoundException("해당 위치를 찾을 수 없습니다.");
        }

        return ApiResponse.onSuccess(mapLetterService.guestFindNearByMapLetters(lat, lon));
    }

    @GetMapping("/guest/{letterId}")
    @Operation(summary = "비로그인 유저 편지 상세 조회", description = "위경도 필수. 반경 15m 내 편지만 상세조회 가능. 내가 타겟인 편지와 퍼블릭 편지만 조회 가능. 나머지는 오류")
    public ApiResponse<OneLetterResponseDTO> guestFindOneMapLetter(@RequestParam String latitude,
                                                                   @RequestParam String longitude,
                                                                   @PathVariable Long letterId) {
        BigDecimal lat = BigDecimal.ZERO;
        BigDecimal lon = BigDecimal.ZERO;
        try {
            lat = new BigDecimal(latitude);
            lon = new BigDecimal(longitude);
        } catch (Exception e) {
            throw new LocationNotFoundException("해당 위치를 찾을 수 없습니다.");
        }

        return ApiResponse.onSuccess(mapLetterService.guestFindOneMapLetter(letterId, lat, lon));
    }

    @GetMapping("/saved")
    public ApiResponse<?> savedLettersV1(@RequestParam String type, @AuthenticationPrincipal CustomUserDetails user,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "9") int size) {
        Long userId = user.getUserId();
        return switch (type) {
            case "sent-all" -> //보낸 편지 전체 조회(지도편지, 답장 편지)
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(mapLetterService.findSentMapLetters(page, size, userId)));
            case "sent-reply" -> //보낸 답장 편지 전체 조회
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(
                                    mapLetterService.findAllSentReplyMapLetter(page, size, userId)));
            case "sent-map" -> //보낸 지도 편지 전체 조회
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(mapLetterService.findAllSentMapLetter(page, size, userId)));
            case "received-all" -> //받은 편지 전체 조회(타겟 편지, 답장 편지)
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(mapLetterService.findReceivedMapLetters(page, size, userId)));
            case "received-reply" -> //받은 답장 편지 전체 조회
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(
                                    mapLetterService.findAllReceivedReplyLetter(page, size, userId)));
            case "received-target" -> //받은 타겟 편지 전체 조회
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(mapLetterService.findAllReceivedLetter(page, size, userId)));
            default -> throw new TypeNotFoundException("올바르지 못한 타입입니다.");
        };
    }

    @GetMapping("/saved/v2")
    public ApiResponse<?> savedLetters(@RequestParam String type, @AuthenticationPrincipal CustomUserDetails user,
                                       @RequestParam(defaultValue = "1") int page,
                                       @RequestParam(defaultValue = "9") int size) {
        Long userId = user.getUserId();
        return switch (type) {
            case "sent-all" -> //보낸 편지 전체 조회(지도편지, 답장 편지)
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(mapLetterService.findSentMapLetters(page, size, userId)));
            case "sent-reply" -> //보낸 답장 편지 전체 조회
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(
                                    mapLetterService.findAllSentReplyMapLetter(page, size, userId)));
            case "sent-map" -> //보낸 지도 편지 전체 조회
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(mapLetterService.findAllSentMapLetter(page, size, userId)));
            case "received-all" -> //받은 편지 전체 조회(타겟 편지, 답장 편지)
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(mapLetterService.findReceivedMapLetters(page, size, userId)));
            case "received-reply" -> //받은 답장 편지 전체 조회
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(
                                    mapLetterService.findAllReceivedReplyLetter(page, size, userId)));
            case "received-map" -> //받은 타겟 편지 전체 조회
                    ApiResponse.onSuccess(
                            MapLetterPageResponseDTO.from(mapLetterService.findAllReceivedLetter(page, size, userId)));
            default -> throw new TypeNotFoundException("올바르지 못한 타입입니다.");
        };
    }

    @DeleteMapping
    @Operation(summary = "편지 삭제", description = "로그인 필수. 리스트 형태로 1개 ~ n개까지 삭제 가능. 3차 스프린트 기간 삭제 예정")
    public ApiResponse<?> deleteMapLetter(@RequestBody DeleteMapLettersV1RequestDTO letters,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        mapLetterService.deleteMapLetter(letters.letterIds(), userId);
        return ApiResponse.onDeleteSuccess(letters);
    }

    @DeleteMapping("/reply")
    @Operation(summary = "답장 편지 삭제", description = "로그인 필수. 답장 편지 삭제. 리스트 형태. 3차 스프린트 기간 삭제 예정")
    public ApiResponse<?> deleteReplyMapLetter(@RequestBody DeleteMapLettersV1RequestDTO letters,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        mapLetterService.deleteReplyMapLetter(letters.letterIds(), userId);
        return ApiResponse.onDeleteSuccess(letters);
    }

    @DeleteMapping("/v2")
    @Operation(summary = "편지 삭제", description = "로그인 필수. 지도편지, 답장편지 구분해서 보내주세요. 리스트 형태로 1개 ~ n개까지 삭제 가능")
    public ApiResponse<?> deleteMapLetter(@RequestBody DeleteMapLettersRequestDTO deleteLetters,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        mapLetterService.deleteMapLetters(deleteLetters, userId);
        return ApiResponse.onDeleteSuccess(deleteLetters);
    }

    @DeleteMapping("/all")
    @Operation(summary = "편지 전체 삭제", description = "로그인 필수. 지도편지, 답장편지 구분해서 보내주세요. 리스트 형태로 1개 ~ n개까지 삭제 가능")
    public ApiResponse<?> deleteAllMapLetter(@RequestParam String type,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        mapLetterService.deleteAllMapLetters(type, userId);
        return ApiResponse.onDeleteSuccess(type);
    }
}
