package postman.bottler.complaint.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import postman.bottler.complaint.dto.request.ComplaintRequestDTO;
import postman.bottler.complaint.dto.response.ComplaintResponseDTO;
import postman.bottler.complaint.exception.InvalidComplainException;
import postman.bottler.complaint.service.ComplaintService;
import postman.bottler.global.response.ApiResponse;

@RestController
@RequiredArgsConstructor
public class ComplaintController {
    private final ComplaintService complaintService;

    @PostMapping("/letters/{letterId}/complaint")
    public ApiResponse<?> complainKeywordLetter(@PathVariable Long letterId,
                                                @RequestBody ComplaintRequestDTO complaintRequest,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidComplainException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ComplaintResponseDTO response = complaintService.complainKeywordLetter(letterId,
                complaintRequest.reporterId(), complaintRequest.description());
        return ApiResponse.onCreateSuccess(response);
    }

    @PostMapping("/map/{letterId}/complaint")
    public ApiResponse<?> complainMapLetter(@PathVariable Long letterId,
                                            @RequestBody ComplaintRequestDTO complaintRequest,
                                            BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidComplainException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ComplaintResponseDTO response = complaintService.complainMapLetter(letterId,
                complaintRequest.reporterId(), complaintRequest.description());
        return ApiResponse.onCreateSuccess(response);
    }

    @PostMapping("/map/{letterId}/reply/complaint")
    public ApiResponse<?> complainMapReplyLetter(@PathVariable Long letterId,
                                                 @RequestBody ComplaintRequestDTO complaintRequest,
                                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidComplainException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ComplaintResponseDTO response = complaintService.complainMapReplyLetter(letterId,
                complaintRequest.reporterId(), complaintRequest.description());
        return ApiResponse.onCreateSuccess(response);
    }

    @PostMapping("/keyword/{letterId}/reply/complaint")
    public ApiResponse<?> complainKeywordReplyLetter(@PathVariable Long letterId,
                                                     @RequestBody ComplaintRequestDTO complaintRequest,
                                                     BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new InvalidComplainException(bindingResult.getAllErrors().get(0).getDefaultMessage());
        }
        ComplaintResponseDTO response = complaintService.complainKeywordReplyLetter(letterId,
                complaintRequest.reporterId(), complaintRequest.description());
        return ApiResponse.onCreateSuccess(response);
    }


}
