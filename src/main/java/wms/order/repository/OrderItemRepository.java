package wms.order.repository;

import wms.order.entity.Order;
import wms.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // 특정 orderId에 대한 가장 큰 orderSeq를 찾는 쿼리
    @Query("SELECT MAX(oi.orderSeq) FROM OrderItem oi WHERE oi.order.orderId = :orderId")
    Long findMaxOrderSeqByOrderId(String orderId);
}