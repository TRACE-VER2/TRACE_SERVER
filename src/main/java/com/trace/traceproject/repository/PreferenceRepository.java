package com.trace.traceproject.repository;

import com.trace.traceproject.domain.Preference;
import com.trace.traceproject.domain.enums.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preference, Long> {

    Preference findByTag(Tag tag);
}
