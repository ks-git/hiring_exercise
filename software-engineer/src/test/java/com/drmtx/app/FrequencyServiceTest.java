package com.drmtx.app;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.BeforeClass;
import org.junit.Assert;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.mockito.Mockito.*;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class FrequencyServiceTest {
	
	  @Mock
	  private static FrequencyRepository mockedRepository;
	  private static FrequencyEntity freqEntity;
	  
	 @BeforeClass
	  public static void setUp(){
	    //Create mock object of repository
		 //mockedRepository = mock(FrequencyRepository.class);
		 TreeMap<String, Integer> mockMap = new TreeMap<String, Integer>();
		 mockMap.put("the", 2);
		 mockMap.put("ant", 1);
		 long id = 1;
	    //Create few instances of frequency entity class.
		 freqEntity = new FrequencyEntity(id,mockMap);
	 
	    //Stubbing the methods of mocked repository with mocked data. 
	    
	  }
	 
	 @Test
	 public void testGetFrequencyResourceById() {
		 
		 long id = 1;
		 int counter = 2;
		 FrequencyServiceImpl service = new FrequencyServiceImpl();
		 service.setRepository(mockedRepository);
		 List<FrequencyResource> expectedFrequencyResourceList = new ArrayList<FrequencyResource>();
			TreeMap<String, Integer> map = freqEntity.getWordCounter();
			int resultCounter = 0;
			for (Map.Entry<String, Integer> entry : map.entrySet()) {
				if (resultCounter >= counter) {
					break;
				}
				String key = entry.getKey();
				Integer value = entry.getValue();
				expectedFrequencyResourceList.add(new FrequencyResource(key, value));
				resultCounter++;
			}

			when(mockedRepository.findById(id)).thenReturn(freqEntity);
			
			when(mockedRepository.exists(id)).thenReturn(true);
			
			List<FrequencyResource> actualFrequencyResourceList = service.getFrequencyResourceById(id, counter);
		 
		 assert actualFrequencyResourceList == expectedFrequencyResourceList;
	 }

}
