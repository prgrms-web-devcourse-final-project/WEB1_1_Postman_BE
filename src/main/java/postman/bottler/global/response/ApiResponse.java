package postman.bottler.global.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"isSuccess", "code", "message", "result"})
public class ApiResponse<T> {

    @JsonProperty("isSuccess")
    private final Boolean isSuccess;
    private final String code;
    private final String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;

    // 일반적인 성공
    public static <T> ApiResponse<T> onSuccess(T result){
        return new ApiResponse<>(true, SuccessStatus._OK.getCode() , SuccessStatus._OK.getMessage(), result);
    }

    // 생성 성공
    public static <T> ApiResponse<T> onCreateSuccess(T result){
        return new ApiResponse<>(true, SuccessStatus.CREATE_SUCCESS.getCode(), SuccessStatus.CREATE_SUCCESS.getMessage(), result);
    }

    // 삭제 성공
    public static <T> ApiResponse<T> onDeleteSuccess(T result){
        return new ApiResponse<>(true, SuccessStatus.DELETE_SUCCESS.getCode(), SuccessStatus.DELETE_SUCCESS.getMessage(), result);
    }

    // 실패한 경우 응답 생성
    public static <T> ApiResponse<T> onFailure(String code, String message, T data){
        return new ApiResponse<>(false, code, message, data);
    }
}
