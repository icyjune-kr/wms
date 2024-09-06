package wms.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class DeliveryInfoDto {

    @NotBlank(message = "수령인 이름(recipientNm)은 필수입니다.")
    private String recipientNm;

    @NotBlank(message = "우편번호(recipientZipCode)는 필수입니다.")
    private String recipientZipCode;

    @NotBlank(message = "주소 1(recipientAddr1)은 필수입니다.")
    private String recipientAddr1;

    private String recipientAddr2;

    private String recipientTelNo;

    @NotBlank(message = "수령인 휴대전화번호(recipientMobileNo)는 필수입니다.")
    private String recipientMobileNo;

    private String recipientEmail;

    private String recipientMemo;
}
