package com.safecar.platform.deviceManagement.infrastructure.persistence.jpa.repositories;


import com.safecar.platform.deviceManagement.domain.model.aggregates.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {



    List<Vehicle> findByDriverId(Long driverId);


}
