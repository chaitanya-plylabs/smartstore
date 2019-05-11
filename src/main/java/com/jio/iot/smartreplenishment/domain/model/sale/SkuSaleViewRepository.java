package com.jio.iot.smartreplenishment.domain.model.sale;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SkuSaleViewRepository extends CrudRepository<SkuSaleView, Long> {
    @Query("SELECT sv from SkuSaleView sv WHERE sv.id.storeId = :storeId")
    List<SkuSaleView> findByStoreId(@Param("storeId") String storeId);
}
