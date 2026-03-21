package com.company.cnpridepoint;

import com.company.cnpridepoint.entity.Section;
import io.jmix.core.repository.JmixDataRepository;

import java.util.UUID;

public interface SectionRepository extends JmixDataRepository<Section, UUID> {
}