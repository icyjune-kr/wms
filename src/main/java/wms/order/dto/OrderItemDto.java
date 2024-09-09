package wms.order.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class OrderItemDto {
    @NotBlank(message = "채널 주문 시퀀스(channelOrderSeq)는 필수입니다.")
    private String channelOrderSeq;
    @NotBlank(message = "채널 상품 코드(channelItemCode)는 필수입니다.")
    private String channelItemCode;
    private String channelOptionCode;
    @NotBlank(message = "채널 상품 이름(channelItemNm)은 필수입니다.")
    private String channelItemNm;
    @NotNull(message = "채널 주문 금액(channelOrderAmt)은 필수입니다.")
    private double channelOrderAmt;
    private String channelItemUrl;
    @NotBlank(message = "벤더 주문 ID(vendorOrderId)는 필수입니다.")
    private String vendorOrderId;
    @NotBlank(message = "벤더 주문 시퀀스(vendorOrderSeq)는 필수입니다.")
    private String vendorOrderSeq;
    @NotBlank(message = "벤더 상품 코드(vendorItemCode)는 필수입니다.")
    private String vendorItemCode;
    @NotBlank(message = "벤더 옵션 코드(vendorOptionCode)는 필수입니다.")
    private String vendorOptionCode;
    private String vendorBarcode;
    private String vendorItemNm;
    private String vendorItemUrl;
    private String upcCode;
    private String hsCode;
    private String optionTypeNm1;
    private String optionNm1;
    private String optionTypeNm2;
    private String optionNm2;
    private String optionTypeNm3;
    private String optionNm3;
    private String optionTypeNm4;
    private String optionNm4;
    private String optionTypeNm5;
    private String optionNm5;
    private String itemImgUrl;
    private String itemOrderUrl;
    private String originNationCd;
    private String material;
    private double weight;
    private String weightUnit;
    private int orderQty;
}
