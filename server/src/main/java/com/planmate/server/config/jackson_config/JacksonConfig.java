package com.planmate.server.config.jackson_config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.planmate.server.config.jackson_config.year_month.YearMonthDeserializer;
import com.planmate.server.config.jackson_config.year_month.YearMonthSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.YearMonth;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        SimpleModule module = new SimpleModule();
        module.addSerializer(YearMonth.class,new YearMonthSerializer());
        module.addDeserializer(YearMonth.class,new YearMonthDeserializer());

        return builder.modules(module).build();
    }
}
