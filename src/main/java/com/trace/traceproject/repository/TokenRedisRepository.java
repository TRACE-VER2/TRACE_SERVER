package com.trace.traceproject.repository;

import com.trace.traceproject.dto.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenRedisRepository extends CrudRepository<Token, String> {
}
