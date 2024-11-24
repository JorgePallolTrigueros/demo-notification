package com.shoppingcart.notification.dao.repository;

import com.shoppingcart.notification.dao.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository <ProductEntity,Long> {

}
