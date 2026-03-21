package com.company.cnpridepoint;

import com.company.cnpridepoint.entity.ActivityAttendance;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityAttendanceRepository extends JmixDataRepository<ActivityAttendance, UUID> {

    Page<ActivityAttendance> findByProgram_Id(UUID programId, Pageable pageable);

    Page<ActivityAttendance> findByActivity_Id(UUID activityId, Pageable pageable);

    Page<ActivityAttendance> findByProgram_IdAndActivity_Id(UUID programId, UUID activityId, Pageable pageable);
}