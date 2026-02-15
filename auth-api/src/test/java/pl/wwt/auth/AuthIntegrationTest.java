package pl.wwt.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import pl.wwt.auth.dto.LoginRequest;
import pl.wwt.auth.dto.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource(
        properties = {
                "spring.kafka.consumer.auto-offset-reset=earliest"
        }
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class AuthIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17");

    @Container
    static KafkaContainer kafka = new KafkaContainer("apache/kafka-native:3.8.0");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", postgres::getJdbcUrl);
        r.add("spring.datasource.username", postgres::getUsername);
        r.add("spring.datasource.password", postgres::getPassword);
        r.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
    }

    @Autowired
    TestRestTemplate rest;

    @Test
    void registerAndLogin() {
        RegisterRequest registerRequest =
                new RegisterRequest("test@test.com", "pass", "login");

        ResponseEntity<Void> register =
                rest.postForEntity(
                        "/api/v1/auth/register",
                        registerRequest,
                        Void.class);

        assertEquals(HttpStatusCode.valueOf(201), register.getStatusCode());

        LoginRequest loginRequest = new LoginRequest("test@test.com", "pass");
        ResponseEntity<Void> login =
                rest.postForEntity(
                        "/api/v1/auth/login",
                        loginRequest,
                        Void.class);

        assertEquals(HttpStatusCode.valueOf(200), login.getStatusCode());
    }
}
