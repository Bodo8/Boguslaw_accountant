package pl.coderstrust.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

  /**
   * Swagger configuration for Controller package.
   */
  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage("pl.coderstrust"))
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo());
  }

  /**
   * API Info as it appears on the swagger-ui page
   *
   * @return new API information with application details.
   */
  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Smart Personal Accountant")
        .description("Application to keep your invoices and taxes organized.")
        .contact("hello@coderstrust.pl")
        .license("SmartTax License Version 1.0")
        .version("1.0")
        .build();
  }
}