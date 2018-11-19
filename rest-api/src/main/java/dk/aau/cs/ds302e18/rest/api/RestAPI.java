package dk.aau.cs.ds302e18.rest.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication(scanBasePackages = "dk.aau.cs.ds302e18.shared")
public class RestAPI
{

	public static void main(String[] args) {
		SpringApplication.run(RestAPI.class, args);
	}
}
