package com.drmtx.app;

import java.util.List;


public interface FrequencyService {

	public List<FrequencyResource> getFrequencyResourceById(long id, int counter);
	
	public FrequencyEntity createFrequencyResource(String comments);
}
