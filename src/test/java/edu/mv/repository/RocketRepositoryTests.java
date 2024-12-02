package edu.mv.repository;

import edu.mv.mv.db.models.Rocket;
import edu.mv.mv.repository.RocketRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig
@ContextConfiguration(classes = RocketRepositoryTests.TestConfig.class)
@SpringBootTest
public class RocketRepositoryTests {

    @Autowired
    private RocketRepository rocketRepository;

    @Test
    public void testSaveAndFind() {
        Rocket rocket = new Rocket(1, "Apollo", "Lunar");
        rocketRepository.save(rocket);

        Rocket found = rocketRepository.findById(1).orElse(null);
        assertNotNull(found);
        assertEquals("Apollo", found.getName());
    }

    @Configuration
    @EnableJpaRepositories(basePackageClasses = RocketRepository.class)
    static class TestConfig {

        @Bean
        public DataSource dataSource() {
            return org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
                    .create()
                    .setType(org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.H2)
                    .build();
        }

        @Bean
        public EntityManagerFactory entityManagerFactory(DataSource dataSource) {
            return new org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean() {{
                setDataSource(dataSource);
                setPackagesToScan("edu.mv.db.models");
                setJpaVendorAdapter(new org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter());
                afterPropertiesSet();
            }}.getObject();
        }
    }
}
