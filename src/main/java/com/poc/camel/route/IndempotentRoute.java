package com.poc.camel.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jcache.processor.idempotent.JCacheIdempotentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class IndempotentRoute extends RouteBuilder {
	private static final Logger LOGGER = LoggerFactory.getLogger(IndempotentRoute.class);

	@Autowired
	@Qualifier("jCacheIdempotentRepository")
	JCacheIdempotentRepository jCacheIdempotentRepository;

	@Override
	public void configure() throws Exception {
		LOGGER.debug("Configuring IndempotentRoute");

		from("file://src/main/resources/?fileName=schemas.txt&noop=true&idempotent=false&delay={{timer.period}}")
		.split(body().tokenize("\r\n|\n"))
		.setHeader("SCHEMA", simple("${body.trim()}"))
		.log("Processing SCHEMA: ${header.SCHEMA}")
		.to("bean:userService?method=findByNativeQuery(${header.SCHEMA})") // Query the database
		.split(simple("${body}")) //Split the primary keys.
		//.to("bean:igniteGetService?method=igniteGet(${header.SCHEMA}::${body}, ${header.CACHENAME})") // (A hack) call get() on Ignite cache to move record from db into cache.
		.idempotentConsumer(simple("${header.SCHEMA}::${body}"), jCacheIdempotentRepository)
		.log("Received message for 'idempotentConsumer'. body: ${body}");
	}
}
