package wms.order.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "OrderItems")
@IdClass(OrderItemId.class)
public class OrderItem {

    @Id
    @Column(name = "order_id", nullable = false)
    private String orderId;

    @Id
    @Column(nullable = false)
    private Long orderSeq;

    @ManyToOne
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private Order order;

    @Column(nullable = false)
    private String channelOrderSeq;

    @Column(nullable = false)
    private String channelItemCode;

    @Column(nullable = true)
    private String channelOptionCode;

    @Column(nullable = false)
    private String channelItemNm;

    @Column(nullable = false)
    private double channelOrderAmt;

    @Column(nullable = true)
    private String channelItemUrl;

    @Column(nullable = false)
    private String vendorOrderId;

    @Column(nullable = false)
    private String vendorOrderSeq;

    @Column(nullable = false)
    private String vendorItemCode;

    @Column(nullable = true)
    private String vendorOptionCode;

    @Column(nullable = true)
    private String vendorBarcode;

    @Column(nullable = false)
    private String vendorItemNm;

    @Column(nullable = true)
    private String vendorItemUrl;

    @Column(nullable = true)
    private String upcCode;

    @Column(nullable = true)
    private String hsCode;

    @Column(nullable = true)
    private String optionTypeNm1;

    @Column(nullable = true)
    private String optionNm1;

    @Column(nullable = true)
    private String optionTypeNm2;

    @Column(nullable = true)
    private String optionNm2;

    @Column(nullable = true)
    private String optionTypeNm3;

    @Column(nullable = true)
    private String optionNm3;

    @Column(nullable = true)
    private String optionTypeNm4;

    @Column(nullable = true)
    private String optionNm4;

    @Column(nullable = true)
    private String optionTypeNm5;

    @Column(nullable = true)
    private String optionNm5;

    @Column(nullable = true)
    private String itemImgUrl;

    @Column(nullable = true)
    private String itemOrderUrl;

    @Column(nullable = true)
    private String originNationCd;

    @Column(nullable = true)
    private String material;

    @Column(nullable = true)
    private double weight;

    @Column(nullable = true)
    private String weightUnit;

    @Column(nullable = false)
    private int orderQty;
}