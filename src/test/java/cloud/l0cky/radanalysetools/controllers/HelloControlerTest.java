package cloud.l0cky.radanalysetools.controllers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class HelloControlerTest {
	
	@LocalServerPort
	private int port;

	private URL base;
	
	@Autowired
	private TestRestTemplate template;
	
	@BeforeEach
	public void setUp() throws Exception{
		this.base = new URL("http://localhost:" + port + "/");
	}
	
	@Test
	public void testResponse() throws Exception {
		ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);
        assertThat(response.getBody()).isEqualTo("Greetings from Spring Boot!");
	}
	
	@Test
	public void testLeafletMarkerResponse() throws Exception {

	}

}
