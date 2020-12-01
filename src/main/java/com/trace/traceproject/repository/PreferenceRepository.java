package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Preference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {
}
