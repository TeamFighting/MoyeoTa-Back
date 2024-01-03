package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.domain.invoice.*;
import com.moyeota.moyeotaproject.domain.users.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;

    public Invoice autoOrder(Users users) {

        // 임시 결제내역 생성
        Payment payment = Payment.builder()
                .price(1000L)
                .status(PaymentStatus.READY)
                .build();

        paymentRepository.save(payment);

        // 주문 생성
        Invoice invoice = Invoice.builder()
                .users(users)
                .price(1000L)
                .itemName("N빵 결제")
                .orderUid(UUID.randomUUID().toString())
                .payment(payment)
                .build();

        return invoiceRepository.save(invoice);
    }
}
