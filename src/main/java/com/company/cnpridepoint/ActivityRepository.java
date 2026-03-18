package com.company.cnpridepoint;

import com.company.cnpridepoint.entity.Activity;
import com.company.cnpridepoint.entity.Status;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityRepository extends JmixDataRepository<Activity, UUID> {

    Page<Activity> findByStatus(Status status, Pageable pageable);
}