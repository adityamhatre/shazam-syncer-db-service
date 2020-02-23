package com.adityamhatre.db_service.rest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class DBRestControllerTest {
	@Autowired
	DBRestController dbRestController;

	@BeforeEach
	void setUp() {
	}

	@Test
	void getLatestSongsPerUser() {
		dbRestController.getLatestSongsPerUser().forEach(System.out::println);
	}

	@Test
	void userExistTest() {
		assertFalse(dbRestController.doesUserExist(""));
		assertTrue(dbRestController.doesUserExist("-M0dcFAayxJ6YAY3JUvA"));
	}

	@Test
	void songLinkTest() {
		assertTrue((Boolean) dbRestController.getSongLink("65629131").get("link_exists"));
		assertFalse((Boolean) dbRestController.getSongLink("65629131a").get("link_exists"));
	}
}