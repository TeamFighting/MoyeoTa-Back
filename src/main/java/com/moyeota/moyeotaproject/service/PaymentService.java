package com.moyeota.moyeotaproject.service;

import com.moyeota.moyeotaproject.controller.dto.PayDto.PaymentCallbackRequest;
import com.moyeota.moyeotaproject.controller.dto.PayDto.RequestPayDto;
import com.moyeota.moyeotaproject.domain.invoice.*;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final InvoiceRepository invoiceRepository;
    private final PaymentRepository paymentRepository;
    private final IamportClient iamportClient;

    // 결제 요청 데이터 조회
    public RequestPayDto findRequestDto(String orderUid) {
        Invoice invoice = invoiceRepository.findInvoiceAndPaymentAndUsers(orderUid)
                .orElseThrow(() -> new IllegalArgumentException("주문이 없습니다."));
        return RequestPayDto.builder()
                .buyerName(invoice.getUsers().getName())
                .buyerEmail(invoice.getUsers().getEmail())
                .buyerAddress(invoice.getUsers().getSchool())
                .paymentPrice(invoice.getPayment().getPrice())
                .itemName(invoice.getItemName())
                .orderUid(invoice.getOrderUid())
                .build();
    }

    // 결제 (콜백)
    public IamportResponse<Payment> paymentByCallback(PaymentCallbackRequest request) {
        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = iamportClient.paymentByImpUid(request.getPaymentUid());
            // 주문내역 조회
            Invoice invoice = invoiceRepository.findInvoiceAndPayment(request.getOrderUid())
                    .orElseThrow(() -> new IllegalArgumentException("주문 내역이 없습니다."));

            // 결제 완료가 아니면
            if (!iamportResponse.getResponse().getStatus().equals("paid")) {
                // 주문, 결제 삭제
                invoiceRepository.delete(invoice);
                paymentRepository.delete(invoice.getPayment());

                throw new RuntimeException("결제 미완료");
            }

            // DB에 저장된 결제 금액
            Long price = invoice.getPayment().getPrice();
            // 실 결제 금액
            int iamportPrice = iamportResponse.getResponse().getAmount().intValue();

            // 결제 금액 검증
            if (iamportPrice != price) {
                // 주문, 결제 삭제
                invoiceRepository.delete(invoice);
                paymentRepository.delete(invoice.getPayment());

                // 결제금액 위변조로 의심되는 결제금액을 취소(아임포트)
                iamportClient.cancelPaymentByImpUid(new CancelData(iamportResponse.getResponse().getImpUid(), true, new BigDecimal(iamportPrice)));

                throw new RuntimeException("결제금액 위변조 의심");
            }

            // 결제 상태 변경
            invoice.getPayment().changePaymentBySuccess(PaymentStatus.OK, iamportResponse.getResponse().getImpUid());

            return iamportResponse;

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
