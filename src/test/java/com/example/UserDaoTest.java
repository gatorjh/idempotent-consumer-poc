package com.example;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserDaoTest {

	/*private EmbeddedDatabase db;
	UserDao userDao;

	@Before
	public void setUp() {
		// db = new EmbeddedDatabaseBuilder().addDefaultScripts().build();
		db = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
				.addScript("sql/create-db.sql")
				.addScript("sql/insert-data.sql").build();
	}

	@Test
	public void testFindByname() {
		NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(db);
		UserDaoImpl userDao = new UserDaoImpl();
		userDao.setNamedParameterJdbcTemplate(template);

		User user = userDao.findByName("mkyong");

		Assert.assertNotNull(user);
		Assert.assertEquals(1, user.getId().intValue());
		Assert.assertEquals("mkyong", user.getName());
		Assert.assertEquals("mkyong@gmail.com", user.getEmail());

	}

	@After
	public void tearDown() {
		db.shutdown();
	}*/

}