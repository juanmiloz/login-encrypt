package com.cibersecurity.login.repository;

import com.cibersecurity.login.model.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository extends CrudRepository<Configuration,String> {
}
