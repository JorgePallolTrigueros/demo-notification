package com.shoppingcart.notification.dao.repository;

import com.shoppingcart.notification.dao.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository <UserEntity,String>{

}
