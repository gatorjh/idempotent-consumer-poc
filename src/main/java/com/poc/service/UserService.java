package com.poc.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.ignite.resources.SpringResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Service;

import com.poc.persistence.User;
import com.poc.repository.UserRepository;

@Service
public class UserService {
	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	@Qualifier("entityManagerFactory")
	@SpringResource(resourceName = "entityManagerFactory")
	LocalContainerEntityManagerFactoryBean entityManagerFactory;

	@SuppressWarnings("unchecked")
	public List<User> findByNativeQuery(String schema) {
		List<User> results = new ArrayList<>();

		if(schema == null || schema.isEmpty()) {
			return results;
		}

		LOG.debug("Processing findByNativeQuery with schema: {}", schema);

		EntityManager entityManager = entityManagerFactory.getNativeEntityManagerFactory().createEntityManager();
		Query sqlQuery;

		// Build the SQL query
		StringBuilder queryStringBuffer = new StringBuilder("SELECT id FROM ");
		queryStringBuffer.append(schema).append(".USER");
		String queryString = queryStringBuffer.toString();
		
		sqlQuery = entityManager.createNativeQuery(queryString, User.class);
		results = sqlQuery.getResultList();

		LOG.debug("Processed findByNativeQuery. Found {} results.", results.size());

		return results;
	}

	public User findOne(Long id) {
		LOG.debug("Processing findOne with id: {}", id);

		return userRepository.findOne(id);
	}
}
