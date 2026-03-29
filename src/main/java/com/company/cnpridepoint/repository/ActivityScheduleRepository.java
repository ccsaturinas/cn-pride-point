package com.company.cnpridepoint.repository;

import com.company.cnpridepoint.entity.ActivitySchedule;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityScheduleRepository extends JmixDataRepository<ActivitySchedule, UUID> {

    Page<ActivitySchedule> findByProgram_Id(UUID programId, Pageable pageable);

    Page<ActivitySchedule> findByActivity_Id(UUID activityId, Pageable pageable);

    Page<ActivitySchedule> findByProgram_IdAndActivity_Id(UUID programId, UUID activityId, Pageable pageable);
}