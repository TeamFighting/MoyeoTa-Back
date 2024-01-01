package com.moyeota.moyeotaproject.domain.invoice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    @Query("select o from Invoice o" +
            " left join fetch o.payment p" +
            " left join fetch o.users m" +
            " where o.orderUid = :orderUid")
    Optional<Invoice> findOrderAndPaymentAndMember(String orderUid);

    @Query("select o from Invoice o" +
            " left join fetch o.payment p" +
            " where o.orderUid = :orderUid")
    Optional<Invoice> findOrderAndPayment(String orderUid);
}
