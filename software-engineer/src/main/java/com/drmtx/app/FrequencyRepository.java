package com.drmtx.app;

import org.springframework.data.repository.CrudRepository;

public interface FrequencyRepository extends CrudRepository<FrequencyEntity, Long> {
	
	FrequencyEntity findById(long id);

}
