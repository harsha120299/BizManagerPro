package lk.ijse.backend.service.impl;

import jakarta.transaction.Transactional;
import lk.ijse.backend.dto.OrderDTO;
import lk.ijse.backend.dto.OrderItemDTO;
import lk.ijse.backend.entity.*;
import lk.ijse.backend.exception.ResourceNotFoundException;
import lk.ijse.backend.repository.*;
import lk.ijse.backend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository ORDER_REPOSITORY;
    private final CustomerRepository CUSTOMER_REPOSITORY;
    private final ProductRepository PRODUCT_REPOSITORY;
    private final BusinessRepository BUSINESS_REPOSITORY;
    private final OrderItemRepository ORDER_ITEM_REPOSITORY;
    private final ModelMapper MAPPER;

    @Override
    @Transactional
    public OrderDTO saveOrder(OrderDTO dto) {
        Business business = BUSINESS_REPOSITORY.findById(dto.getBusinessId())
                .orElseThrow(() -> new ResourceNotFoundException("Business"));
        Customer customer = CUSTOMER_REPOSITORY.findById(dto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with ID: " + dto.getCustomerId()));

        Order order = new Order();
        order.setOrderDate(LocalDate.now());
        order.setStatus(dto.getStatus() != null ? dto.getStatus() : "PENDING");
        order.setCustomer(customer);

        List<OrderItems> items = dto.getOrderItems().stream().map(itemDto -> {
            Product product = PRODUCT_REPOSITORY.findById(itemDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDto.getProductId()));

            // Reduce stock
            double orderQty = itemDto.getQuantity();
            if(product.getQuantity() < orderQty) {
                throw new IllegalStateException("Insufficient stock for product: " + product.getName());
            }
            product.setQuantity(product.getQuantity() - orderQty);
            PRODUCT_REPOSITORY.save(product);

            OrderItems item = new OrderItems();
            item.setProduct(product);
            item.setQuantity(orderQty);
            item.setPrice(product.getDefaultPrice() * orderQty);
            item.setOrder(order);
            return item;
        }).collect(Collectors.toList());

        order.setOrderItems(items);
        order.setTotalAmount(items.stream().mapToDouble(OrderItems::getPrice).sum());
        order.setBusiness(business);

        Order saved = ORDER_REPOSITORY.save(order);
        return MAPPER.map(saved, OrderDTO.class);
    }
    @Override
    public Page<OrderDTO> getAllOrdersByPages(Long businessId, int page, int size, String search) {
         Pageable pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());
         Page<Order> orders;

        if(search != null && !search.isEmpty()) {
            orders = ORDER_REPOSITORY.findByCustomerNameContainingAndCustomerBusinessId(search, businessId, pageable);
        } else {
            orders = ORDER_REPOSITORY.findOrdersByBusinessId(businessId, pageable);
        }

        return orders.map(order -> {
             OrderDTO dto = MAPPER.map(order, OrderDTO.class);
            if(order.getOrderItems() != null && !order.getOrderItems().isEmpty()) {
                dto.setOrderItems(order.getOrderItems().stream()
                        .map(item -> MAPPER.map(item, OrderItemDTO.class))
                        .collect(Collectors.toList()));
            } else {
                dto.setOrderItems(new ArrayList<>());
            }
            return dto;
        });
    }

    @Override
    public List<OrderDTO> getAllOrders(Long businessId) {
         List<Order> orders = ORDER_REPOSITORY.findByCustomerBusinessId(businessId);
        return orders.stream().map(o -> MAPPER.map(o, OrderDTO.class)).collect(Collectors.toList());
    }

    @Override
    public OrderDTO updateOrder(OrderDTO dto) {
         Order order = ORDER_REPOSITORY.findById(dto.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with ID: " + dto.getOrderId()));

        order.setStatus(dto.getStatus());
        order.setOrderDate(dto.getOrderDate() != null ? dto.getOrderDate() : order.getOrderDate());

        if(dto.getOrderItems() != null && !dto.getOrderItems().isEmpty()) {
            order.getOrderItems().clear();

             List<OrderItems> items = dto.getOrderItems().stream().map(itemDto -> {
                 Product product = PRODUCT_REPOSITORY.findById(itemDto.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + itemDto.getProductId()));

                 OrderItems item = new OrderItems();
                item.setProduct(product);
                item.setQuantity(Double.valueOf(itemDto.getQuantity()));
                item.setPrice(product.getDefaultPrice() * itemDto.getQuantity());
                item.setOrder(order);
                return item;
            }).collect(Collectors.toList());

            order.getOrderItems().addAll(items);
            order.setTotalAmount(items.stream().mapToDouble(OrderItems::getPrice).sum());
        }

         Order updated = ORDER_REPOSITORY.save(order);
        return MAPPER.map(updated, OrderDTO.class);
    }

    @Override
    public boolean deleteOrder(Long id) {
        if(!ORDER_REPOSITORY.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with ID: " + id);
        }
        ORDER_REPOSITORY.deleteById(id);
        return true;
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = ORDER_REPOSITORY.findOrderWithItems(orderId);
        if (order == null) return null;

        return OrderDTO.builder()
                .orderId(order.getOrderId())
                .orderDate(order.getOrderDate())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .note(order.getNote())
                .customerId(order.getCustomer() != null ? order.getCustomer().getCustomerId() : null)
                .businessId(order.getBusiness() != null ? order.getBusiness().getBusinessId() : null)
                .orderItems(order.getOrderItems().stream().map(oi ->
                        OrderItemDTO.builder()
                                .itemId(oi.getId())
                                .productId(oi.getProduct().getProductId())
                                .price(oi.getPrice())
                                .quantity(oi.getQuantity())
                                .build()
                ).collect(Collectors.toList()))
                .build();
    }
}

