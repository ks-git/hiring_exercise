package com.drmtx.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FrequencyServiceImpl implements FrequencyService {
	
	@Autowired
	private FrequencyRepository repository;


	@Override
	public List<FrequencyResource> getFrequencyResourceById(long id, int counter) 
			throws FrequencyNotFoundException {

		List<FrequencyResource> resourceList = new ArrayList<FrequencyResource>();
		
		
		boolean isIDExists = repository.exists(id);

		if (Boolean.FALSE.equals(Boolean.valueOf(isIDExists))) {
			throw new FrequencyNotFoundException(id);
		}
		
		FrequencyEntity entity = repository.findById(id);

		if (entity != null) {
			TreeMap<String, Integer> map = entity.getWordCounter();
			int resultCounter = 0;
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				if (resultCounter >= counter) {
					break;
				}
				String key = entry.getKey();
				Integer value = entry.getValue();
				resourceList.add(new FrequencyResource(key, value));
				resultCounter++;
			}

		}
		
		return resourceList;
		
	}

	@Override
	public FrequencyEntity createFrequencyResource(String comments) {

		String[] splitTextArray = comments.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

		TreeMap<String, Integer> wordsFrequencyMap = new TreeMap<String, Integer>();
		for (String word : splitTextArray) {
			if (wordsFrequencyMap.containsKey(word))
				wordsFrequencyMap
						.put(word, wordsFrequencyMap.get(word) + 1);
			else
				wordsFrequencyMap.put(word, 1);
		}

		FrequencyEntity freqObj = new FrequencyEntity();
		freqObj.setWordCounter(wordsFrequencyMap);
		repository.save(freqObj);
		
		return freqObj;
	}

	public FrequencyRepository getRepository() {
		return repository;
	}

	public void setRepository(FrequencyRepository repository) {
		this.repository = repository;
	}
	
	

}
