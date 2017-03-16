package com.poc.service;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class IgniteGetService {
	private static final Logger LOG = LoggerFactory.getLogger(IgniteGetService.class);

	@Autowired
	@Qualifier("ignite")
	Ignite ignite;

	public void igniteGet(String key, String cacheName) {
		LOG.debug("igniteGet called with key: {} and cacheName: {}", key, cacheName);

		IgniteCache<Object, Boolean> cache = ignite.cache(cacheName);

		cache.get(key);
	}
}
