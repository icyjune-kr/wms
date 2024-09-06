package wms.order.dto;

import lombok.Data;

@Data
public class CommonResponseDto {
    private String resultCd;
    private String message;

    public CommonResponseDto(String resultCd, String message) {
        this.resultCd = resultCd;
        this.message = message;
    }

    public static CommonResponseDto success() {
        return new CommonResponseDto("00", "success");
    }
    public static CommonResponseDto error(String resultCd, String message) {
        return new CommonResponseDto(resultCd, message);
    }
}
