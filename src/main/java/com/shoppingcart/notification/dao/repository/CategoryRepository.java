package com.shoppingcart.notification.dao.repository;

import com.shoppingcart.notification.dao.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<CategoryEntity,String> {

}
