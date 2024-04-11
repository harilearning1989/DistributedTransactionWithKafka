package com.web.demo.repos;

import com.web.demo.models.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    Iterable<Payment> findByOrderId(int orderId);
}
