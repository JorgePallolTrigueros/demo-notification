package com.shoppingcart.notification.dao.repository;

import com.shoppingcart.notification.dao.entity.InvoiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface InvoiceEntityRepository extends JpaRepository<InvoiceEntity, String> {

}