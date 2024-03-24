package com.moyeota.moyeotaproject.domain.invoice;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Payment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long price;
	private PaymentStatus status;
	private String paymentUid; // 결제 고유 번호

	@Builder
	public Payment(Long price, PaymentStatus status) {
		this.price = price;
		this.status = status;
	}

	public void changePaymentBySuccess(PaymentStatus status, String paymentUid) {
		this.status = status;
		this.paymentUid = paymentUid;
	}
}
