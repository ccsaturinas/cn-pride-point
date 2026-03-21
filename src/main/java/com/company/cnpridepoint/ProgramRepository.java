package com.company.cnpridepoint;

import com.company.cnpridepoint.entity.Program;
import com.company.cnpridepoint.entity.Status;
import io.jmix.core.repository.JmixDataRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ProgramRepository extends JmixDataRepository<Program, UUID> {

    Page<Program> findByStatus(Status status, Pageable pageable);
}