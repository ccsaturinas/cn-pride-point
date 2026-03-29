package com.company.cnpridepoint;

import com.company.cnpridepoint.entity.ActivityAttendance;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ActivityAttendanceRepository extends JmixDataRepository<ActivityAttendance, UUID> {


    Page<ActivityAttendance> findByActivitySchedule_Id(UUID activityScheduleId, Pageable pageable);
}