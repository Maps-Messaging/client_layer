package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;
import org.json.JSONObject;

public interface MessageFormatter {

 String getName();

  default Object parse(byte[] payload) throws IOException {
    return payload;
  }

  JSONObject parseToJson(byte[] payload) throws IOException;

  default byte[] pack(Object object) throws IOException {
    if (object instanceof byte[]) {
      return (byte[]) object;
    }
    throw new IOException("Unexpected object to be packed");
  }

  MessageFormatter getInstance(SchemaConfig config) throws IOException;

}
