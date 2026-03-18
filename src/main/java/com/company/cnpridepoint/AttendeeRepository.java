package com.company.cnpridepoint;

import com.company.cnpridepoint.entity.Attendee;
import io.jmix.core.repository.JmixDataRepository;

import java.util.UUID;

public interface AttendeeRepository extends JmixDataRepository<Attendee, UUID> {
}