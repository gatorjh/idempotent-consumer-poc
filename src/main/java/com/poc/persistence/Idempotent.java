package com.poc.persistence;

public class Idempotent {
	private String key;

	private Integer value;

	public Idempotent(String key, Integer value) {
		this.setKey(key);
		this.setValue(value);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "IdempotentTest [key=" + key + ", value=" + value + "]";
	}
}
