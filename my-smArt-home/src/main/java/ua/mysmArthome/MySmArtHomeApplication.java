package ua.mysmArthome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableSwagger2
public class MySmArtHomeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MySmArtHomeApplication.class, args);
	}

	@Bean	
	public Docket mysmArthhomeAPI()	{		
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.basePackage("ua.mysmArthome.controller")).build();	
	}

}
