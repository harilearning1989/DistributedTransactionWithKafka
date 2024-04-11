package com.web.demo.repos;

import com.web.demo.models.WareHouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<WareHouse, Integer> {

    Optional<WareHouse> findOneByItem(String item);
    Optional<WareHouse> findOneByItemAndQuantityIsGreaterThanEqual(String item,int quantity);

}
