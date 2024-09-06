package wms.order.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class OrderRequestDto {

    @NotNull(message = "common 정보는 필수입니다.")
    @Valid
    private OrderCommonDto common;

    @NotNull(message = "deliveryInfo 정보는 필수입니다.")
    @Valid
    private DeliveryInfoDto deliveryInfo;

    @NotNull(message = "orderItem 정보는 필수입니다.")
    @Valid
    private List<@Valid OrderItemDto> orderItem;

    @NotNull(message = "orderMaster 정보는 필수입니다.")
    @Valid
    private OrderMasterDto orderMaster;
}
