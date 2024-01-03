package com.moyeota.moyeotaproject.controller;

import com.moyeota.moyeotaproject.controller.dto.PayDto.PaymentCallbackRequest;
import com.moyeota.moyeotaproject.controller.dto.PayDto.RequestPayDto;
import com.moyeota.moyeotaproject.domain.invoice.Invoice;
import com.moyeota.moyeotaproject.domain.users.Users;
import com.moyeota.moyeotaproject.service.InvoiceService;
import com.moyeota.moyeotaproject.service.KakaoPayService;
import com.moyeota.moyeotaproject.service.PaymentService;
import com.moyeota.moyeotaproject.service.UsersService;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PayControllerTemp {
    private final KakaoPayService kakaoPayService;
    private final UsersService usersService;
    private final InvoiceService invoiceService;
    private final PaymentService paymentService;


    @PostMapping("/kakaoPay")
    public String kakaoPay() {
        return kakaoPayService.kakaoPayReady();
    }

    @GetMapping("/kakaoPaySuccess")
    public void kakaoPaySuccess(@RequestParam("pg_token") String pg_token, Model model) {
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/order")
    public String order(@RequestParam(name = "message", required = false) String message,
                        @RequestParam(name = "orderUid", required = false) String id,
                        Model model) {

        model.addAttribute("message", message);
        model.addAttribute("orderUid", id);

        return "order";
    }

    @PostMapping("/order")
    public String autoOrder() {
        Users users = usersService.autoRegister(); // 사용자 임시 등록
        Invoice invoice = invoiceService.autoOrder(users);

        String message = "주문 실패";
        if (invoice != null) {
            message = "주문 성공";
        }

        String encode = URLEncoder.encode(message, StandardCharsets.UTF_8);

        return "redirect:/order?message=" + encode + "&orderUid=" + invoice.getOrderUid();
    }

    @GetMapping("/payment/{id}")
    public String paymentPage(@PathVariable(name = "id", required = false) String id, Model model) {
        log.info("id : " + id);
        RequestPayDto requestDto = paymentService.findRequestDto(id);
        log.info("address : " + requestDto.getBuyerAddress());
        model.addAttribute("requestDto", requestDto);
        return "payment";
    }

    @ResponseBody
    @PostMapping("/payment")
    public ResponseEntity<IamportResponse<Payment>> validationPayment(@RequestBody PaymentCallbackRequest request) {
        IamportResponse<Payment> iamportResponse = paymentService.paymentByCallback(request);

        log.info("결제 응답={}", iamportResponse.getResponse().toString());

        return new ResponseEntity<>(iamportResponse, HttpStatus.OK);
    }

    @GetMapping("/success-payment")
    public String successPaymentPage() {
        return "success-payment";
    }

    @GetMapping("/fail-payment")
    public String failPaymentPage() {
        return "fail-payment";
    }
}
