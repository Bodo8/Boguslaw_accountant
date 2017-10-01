package pl.coderstrust.database.impl;

import static pl.coderstrust.configuration.FileConfigurationProvider.DATABASE_PATH;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.coderstrust.database.Database;

@Configuration
public class DatabaseProvider {

  @Bean
  @ConditionalOnProperty(name = {"active.database"}, havingValue = "file")
  public Database inFileDatabase() {
    return new InFileDatabase(DATABASE_PATH);
  }
}
