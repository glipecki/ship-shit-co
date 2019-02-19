package net.lipecki.shipping.orders.list;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("orders")
public class OrderListController {

    private OrdersRepository repository;

    @GetMapping("/")
    public List<OrderListEntity> orders() {
        return repository.findAll();
    }

}
