package wms.order.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "orders")
@Data
public class Order {

    @Id
    @Column(unique = true, nullable = false)
    private String orderId;  // 기본 키 (UUID + 현재 일시)

    @NotNull
    @Column(nullable = true)
    private String vendorId;

    @NotNull
    @Column(nullable = false)
    private String vendorNm;

    @NotNull
    @Column(nullable = true)
    private String channelId;

    @NotNull
    @Column(nullable = false)
    private String channelNm;

    @NotNull
    @Column(nullable = false)
    private String channelOrderId;

    @NotNull
    @Column(nullable = false)
    private String channelOrderDate;

    @NotNull
    @Column(nullable = false)
    private String channelOrderUserNm;

    @NotNull
    @Column(nullable = false)
    private String channelOrderCurrencyCd;

    @NotNull
    @Column(nullable = false)
    private double channelOrderAmount;

    @NotNull
    @Column(nullable = false)
    private double shippingFee;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "order")
    private List<OrderItem> orderItems;

    @NotNull
    @Column(nullable = false)
    private String recipientNm;

    @NotNull
    @Column(nullable = false)
    private String recipientZipCode;

    @NotNull
    @Column(nullable = false)
    private String recipientAddr1;

    @Column(nullable = true)
    private String recipientAddr2;

    @NotNull
    @Column(nullable = true)
    private String recipientTelNo;

    @NotNull
    @Column(nullable = false)
    private String recipientMobileNo;

    @NotNull
    @Column(nullable = true)
    private String recipientEmail;

    @Column(nullable = true)
    private String recipientMemo;

    @PrePersist
    public void prePersist() {
        if (this.orderId == null) {
            String currentTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
            String uuid = UUID.randomUUID().toString().replace("-", "");
            this.orderId = currentTime + uuid;
        }
    }
}