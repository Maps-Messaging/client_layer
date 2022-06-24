package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;

public interface MessageFormatter {

  default String getName() {
    return "RAW";
  }

  default Object parse(byte[] payload) throws IOException {
    return payload;
  }

  default byte[] pack(Object object) throws IOException {
    if (object instanceof byte[]) {
      return (byte[]) object;
    }
    throw new IOException("Unexpected object to be packed");
  }

  MessageFormatter getInstance(SchemaConfig config) throws IOException;

}
