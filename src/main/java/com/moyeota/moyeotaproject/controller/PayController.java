package com.moyeota.moyeotaproject.controller;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = "Pay", description = "Pay Controller")
@ApiResponses({
	@ApiResponse(code = 200, message = "API 정상 작동"),
	@ApiResponse(code = 400, message = "BAD REQUEST"),
	@ApiResponse(code = 404, message = "NOT FOUND"),
	@ApiResponse(code = 500, message = "INTERNAL SERVER ERROR")
})
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class PayController {
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

	//    @ApiOperation(value = "구글 소셜 로그인", notes = "구글 소셜 로그인 회원가입 API")
	//    @GetMapping("/order")
	//    public String order(@RequestParam(name = "message", required = false) String message,
	//                        @RequestParam(name = "orderUid", required = false) String id,
	//                        Model model) {
	//
	//        model.addAttribute("message", message);
	//        model.addAttribute("orderUid", id);
	//
	//        return "order";
	//    }

	@ApiOperation(value = "비용 지불 요청", notes = "방장이 다른 사용자에게 비용 지불 요청하는 API")
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

	@ApiOperation(value = "", notes = " API")
	@GetMapping("/payment/{id}")
	public String paymentPage(@PathVariable(name = "id", required = false) String id, Model model) {
		log.info("id : " + id);
		RequestPayDto requestDto = paymentService.findRequestDto(id);
		log.info("address : " + requestDto.getBuyerAddress());
		model.addAttribute("requestDto", requestDto);
		return "payment";
	}

	@ApiOperation(value = "결제 요청", notes = "KG이니시스 결제 요청 API")
	@ResponseBody
	@PostMapping("/payment/KG")
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
