package wms.order.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class OrderMasterDto {

    private String vendorId;

    @NotBlank(message = "벤더 이름(vendorNm)은 필수입니다.")
    private String vendorNm;

    private String channelId;

    @NotBlank(message = "채널 이름(channelNm)은 필수입니다.")
    private String channelNm;

    @NotBlank(message = "채널 주문 ID(channelOrderId)는 필수입니다.")
    private String channelOrderId;

    @NotBlank(message = "채널 주문 날짜(channelOrderDate)는 필수입니다.")
    private String channelOrderDate;

    @NotBlank(message = "채널 주문 사용자 이름(channelOrderUserNm)은 필수입니다.")
    private String channelOrderUserNm;

    @NotBlank(message = "채널 주문 통화 코드(channelOrderCurrencyCd)는 필수입니다.")
    private String channelOrderCurrencyCd;

    @NotNull(message = "채널 주문 금액(channelOrderAmount)은 필수입니다.")
    private Double channelOrderAmount;

    @NotNull(message = "배송비(shippingFee)는 필수입니다.")
    private Double shippingFee;
}
