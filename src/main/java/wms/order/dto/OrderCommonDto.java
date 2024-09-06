package wms.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class OrderCommonDto {

    @NotBlank(message = "API 이름(apiNm)은 필수입니다.")
    private String apiNm;

    @NotBlank(message = "API 인증 키(apiAuthKey)는 필수입니다.")
    private String apiAuthKey;

    @NotBlank(message = "API 호출 날짜(apiCallDate)는 필수입니다.")
    private String apiCallDate;
}
