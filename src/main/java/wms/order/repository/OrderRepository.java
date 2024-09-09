package wms.order.repository;

import wms.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByOrderId(String orderId);

    List<Order> findByVendorId(String vendorId);
}
