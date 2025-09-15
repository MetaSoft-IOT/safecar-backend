package com.safecar.platform.shared.infrastructure.documentation.openapi.configuration;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * OpenAPI configuration
 * 
 * <p>
 * This class configures the OpenAPI documentation for the application using 
 * the springdoc-openapi library. It sets up the API information such as title,
 * description, version, and license. It also provides a link to external
 * documentation.
 * </p>
 * 
 * @version 1.0
 * @since 2025
 * @author GonzaloQu3dena
 */
@Configuration
public class OpenApiConfiguration {
  @Value("${spring.application.name}")
  String applicationName;

  @Value("${documentation.application.description}")
  String applicationDescription;

  @Value("${documentation.application.version}")
  String applicationVersion;

  /**
   * Creates the OpenAPI bean with the application information.
   * @return the OpenAPI bean configured with application details.
   */
  @Bean
  public OpenAPI learningPlatformOpenApi() {
    var openApi = new OpenAPI();

    openApi.info(new Info()
        .title(applicationName)
        .description(applicationDescription)
        .version(applicationVersion)
        .license(new License().name("Apache 2.0").url("https://springdoc.org")))
        .externalDocs(new ExternalDocumentation()
            .description("SafeCar Platform Documentation")
            .url("https://safe-car.wiki.github.io/docs"));

    // TODO: Security definitions


    return openApi;
  }

}
