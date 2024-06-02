package com.example.foodx_be.ulti;

import com.example.foodx_be.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ErrorCodeSerializer extends StdSerializer<ErrorCode> {

    public ErrorCodeSerializer() {
        this(null);
    }

    public ErrorCodeSerializer(Class<ErrorCode> t) {
        super(t);
    }

    @Override
    public void serialize(ErrorCode errorCode, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("code", errorCode.getCode());
        gen.writeStringField("message", errorCode.getMessage());
        gen.writeEndObject();
    }
}
