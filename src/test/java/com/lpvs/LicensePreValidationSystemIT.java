/**
 * Copyright (c) 2024, Samsung Electronics Co., Ltd. All rights reserved.
 *
 * Use of this source code is governed by a MIT license that can be
 * found in the LICENSE file.
 */

 package com.lpvs;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource("classpath:application.properties")
public class LicensePreValidationSystemIT {

    @Container
    private static final MySQLContainer<?> mysqlContainer = new MySQLContainer<>("mysql:8.0.11")
            .withDatabaseName("lpvs")
            .withUsername("root")
            .withPassword("test")
            .withInitScript("database_dump.sql")
            .waitingFor(Wait.forListeningPort());

    @Value("${spring.datasource.url}")
    private String dataSourceUrl;

    @Value("${spring.datasource.username}")
    private String dataSourceUsername;

    @Value("${spring.datasource.password}")
    private String dataSourcePassword;

    @BeforeAll
    static void setUp() {
        mysqlContainer.start();
        System.err.println("initialized MySQL container");


        String jdbcUrl = mysqlContainer.getJdbcUrl();
        String username = mysqlContainer.getUsername();
        String password = mysqlContainer.getPassword();

        System.err.println("jdbcUrl = " + jdbcUrl);
        System.err.println("username = " + username);
        System.err.println("password = " + password);

    }

    // Debug test to ensure properties set properly
    @Test
    void printLoadedProperties() {
        System.out.println("DataSource URL: " + dataSourceUrl);
        System.out.println("DataSource Username: " + dataSourceUsername);
        System.out.println("DataSource Password: " + dataSourcePassword);
    }

    //Test successfull service running with test container database
    @Test
    void tesstMySQLConnectionLoad() {
        String jdbcUrl = mysqlContainer.getJdbcUrl();
        String username = mysqlContainer.getUsername();
        String password = mysqlContainer.getPassword();

        assertTrue(jdbcUrl.equals("jdbc:mysql://localhost:3306"), "incorrect database URL");
        assertTrue(username.equals("root"), "incorrect database username");
        assertTrue(password.equals("test"), "incorrect database pass");
        assertTrue(mysqlContainer.isRunning(), "MySQL container should be running.");
    }
}
