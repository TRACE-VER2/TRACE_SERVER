package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
