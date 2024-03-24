package com.moyeota.moyeotaproject.service;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.moyeota.moyeotaproject.domain.invoice.Invoice;
import com.moyeota.moyeotaproject.domain.invoice.InvoiceRepository;
import com.moyeota.moyeotaproject.domain.invoice.Payment;
import com.moyeota.moyeotaproject.domain.invoice.PaymentRepository;
import com.moyeota.moyeotaproject.domain.invoice.PaymentStatus;
import com.moyeota.moyeotaproject.domain.users.Users;

import lombok.RequiredArgsConstructor;

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
