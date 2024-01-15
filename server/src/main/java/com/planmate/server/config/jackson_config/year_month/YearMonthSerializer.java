package com.planmate.server.config.jackson_config.year_month;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.YearMonth;

public class YearMonthSerializer extends StdSerializer<YearMonth> {

    public YearMonthSerializer() {
        super(YearMonth.class);
    }

    @Override
    public void serialize(YearMonth yearMonth, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeString(yearMonth.toString());
    }
}
