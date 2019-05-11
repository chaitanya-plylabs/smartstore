package com.jio.iot.smartad.domain.model.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserSkuViewRepository extends CrudRepository<UserSkuView, Long> {
    @Query("SELECT usv from UserSkuView usv WHERE usv.id.userId = :userId")
    List<UserSkuView> findByUserId(@Param("userId") String userId);
}
