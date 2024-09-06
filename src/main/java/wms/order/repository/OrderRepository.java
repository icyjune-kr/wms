package wms.order.repository;

import wms.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    Optional<Order> findById(String orderId);

    Optional<Order> findByVendorId(String vendorId);
}
