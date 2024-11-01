package com.rapid.dao;

import com.rapid.core.entity.ConfigurationKeys;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigurationKeyRepo extends JpaRepository<ConfigurationKeys, String> {

    Optional<ConfigurationKeys> findByName(String name);
}
