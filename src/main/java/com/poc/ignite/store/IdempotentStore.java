package com.poc.ignite.store;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.cache.Cache.Entry;
import javax.cache.integration.CacheLoaderException;
import javax.sql.DataSource;

import org.apache.ignite.cache.store.CacheStoreAdapter;
import org.apache.ignite.lang.IgniteBiInClosure;
import org.apache.ignite.resources.SpringResource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IdempotentStore extends CacheStoreAdapter<Object,Boolean> {
	private static final Logger LOG = LoggerFactory.getLogger(IdempotentStore.class);

	@SpringResource(resourceName = "dataSource")
	private DataSource dataSource;

	@Override
	public void loadCache(IgniteBiInClosure<Object,Boolean> clo, Object... args) {
		LOG.debug("loadCache() called");

		try (Connection conn = dataSource.getConnection();
				PreparedStatement st = conn.prepareStatement("SELECT * FROM SCHEMA1.IDEMPOTENT");) {
			ResultSet rs = st.executeQuery();
			while (rs.next()) {
				clo.apply(rs.getString(1), Boolean.TRUE);
			}
		} catch (SQLException e) {
			throw new CacheLoaderException("Failed to load database records from the Idempotent table.", e);
		}
	}

	@Override
	public void write(Entry<? extends Object, ? extends Boolean> entry) {
		LOG.debug("write() called with key: {}, value: {}", entry.getKey(), entry.getValue());

		try (Connection conn = dataSource.getConnection();
			PreparedStatement st = conn.prepareStatement("INSERT INTO SCHEMA1.IDEMPOTENT VALUES (?, ?)");) {
			st.setString(1, entry.getKey().toString());
			st.setInt(2, 1);
			st.executeUpdate();
		} catch (SQLException e) {
			throw new CacheLoaderException("Failed to write Idempotent record into the Idempotent table.", e);
		}
	}

	@Override
	public Boolean load(Object key) {
		LOG.debug("load() called with key: {}", key);

		try (Connection conn = dataSource.getConnection();
				PreparedStatement st = conn.prepareStatement("SELECT * FROM SCHEMA1.IDEMPOTENT WHERE key = ?");) {
			st.setString(1, key.toString());
			ResultSet rs = st.executeQuery();

			return rs.next() ? Boolean.TRUE : null;
		} catch (SQLException e) {
			throw new CacheLoaderException("Failed to load Idempotent record from the Idempotent table.", e);
		}
	}

	@Override
	public void delete(Object arg0) {
		LOG.debug("delete() called");
		// TODO Auto-generated method stub

	}

	@Override
	public void sessionEnd(boolean commit) {
		LOG.debug("sessionEnd() called");
		// TODO Auto-generated method stub

	}
}