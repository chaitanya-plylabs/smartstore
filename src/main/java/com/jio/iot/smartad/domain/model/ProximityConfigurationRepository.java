package com.jio.iot.smartad.domain.model;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProximityConfigurationRepository extends CrudRepository<ProximityConfiguration, Integer> {
    @Query("SELECT pc from ProximityConfiguration pc WHERE pc.storeId = :storeId AND pc.beaconId = :beaconId")
    List<ProximityConfiguration> findByBeaconId(@Param("storeId") String storeId, @Param("beaconId") final String beaconId);
}
