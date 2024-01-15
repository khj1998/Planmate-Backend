package com.planmate.server.config.jackson_config.year_month;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.YearMonth;

public class YearMonthDeserializer extends StdDeserializer<YearMonth> {

    public YearMonthDeserializer() {
        super(YearMonth.class);
    }

    @Override
    public YearMonth deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        int year = node.get("year").asInt();
        int month = node.get("month").asInt();
        return YearMonth.of(year, month);
    }
}
