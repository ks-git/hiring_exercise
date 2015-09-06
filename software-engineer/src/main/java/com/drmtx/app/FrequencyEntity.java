package com.drmtx.app;

import java.util.TreeMap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class FrequencyEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(columnDefinition="LONGVARCHAR")
	private TreeMap<String, Integer> wordCounter;
	
	public FrequencyEntity() {};
	
	public FrequencyEntity(long id, TreeMap<String, Integer> wordCounter) {
		this.id = id;
		this.wordCounter = wordCounter;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TreeMap<String, Integer> getWordCounter() {
		return wordCounter;
	}

	public void setWordCounter(TreeMap<String, Integer> wordCounter) {
		this.wordCounter = wordCounter;
	}
	
	
}
