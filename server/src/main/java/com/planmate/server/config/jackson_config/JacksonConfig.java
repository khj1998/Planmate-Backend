package com.planmate.server.config.jackson_config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.planmate.server.config.jackson_config.year_month.YearMonthDeserializer;
import com.planmate.server.config.jackson_config.year_month.YearMonthSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();

        builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        builder.serializers(new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
        builder.deserializers(new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));

        SimpleModule module = new SimpleModule();
        module.addSerializer(YearMonth.class,new YearMonthSerializer());
        module.addDeserializer(YearMonth.class,new YearMonthDeserializer());

        builder.modulesToInstall(new JavaTimeModule());

        return builder.build();
    }
}
