package com.poc;

import org.apache.camel.component.jcache.processor.idempotent.JCacheIdempotentRepository;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class IdempotentRepositoryConfig {
	private static final Logger LOG = LoggerFactory.getLogger(IdempotentRepositoryConfig.class);

	@Bean
	Ignite ignite() {
		LOG.debug("INSTANTIATING Ignite");

		return Ignition.start("ignite-config.xml");
	}

	@Bean
	JCacheIdempotentRepository jCacheIdempotentRepository(Ignite ignite) {
		LOG.debug("INSTANTIATING JCacheIdempotentRepository");

		IgniteCache<Object,Boolean> igniteCache = ignite.cache("idempotentRepositoryCache");

		igniteCache.loadCache(null);

		JCacheIdempotentRepository jcacheIdempotentRepository = new JCacheIdempotentRepository();

		jcacheIdempotentRepository.setCache(igniteCache);

		return jcacheIdempotentRepository;
	}
}