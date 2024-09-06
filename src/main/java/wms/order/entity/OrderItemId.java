package wms.order.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderItemId implements Serializable {
    private String orderId;
    private Long orderSeq;
}
