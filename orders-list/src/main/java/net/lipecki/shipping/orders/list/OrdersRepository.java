package net.lipecki.shipping.orders.list;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrdersRepository extends JpaRepository<OrderListEntity, String> {
}
