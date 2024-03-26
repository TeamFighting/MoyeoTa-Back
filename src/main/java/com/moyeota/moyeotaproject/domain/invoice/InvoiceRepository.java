package com.moyeota.moyeotaproject.domain.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {

	@Query("select i from Invoice i" +
		" left join fetch i.payment p" +
		" left join fetch i.users u" +
		" where i.orderUid = :orderUid")
	Optional<Invoice> findInvoiceAndPaymentAndUsers(@Param("orderUid") String orderUid);

	@Query("select i from Invoice i" +
		" left join fetch i.payment p" +
		" where i.orderUid = :orderUid")
	Optional<Invoice> findInvoiceAndPayment(@Param("orderUid") String orderUid);
}
