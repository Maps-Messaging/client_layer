package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;
import org.json.JSONObject;

public class RawFormatter implements MessageFormatter {

  @Override
  public String getName() {
    return "RAW";
  }
  @Override
  public JSONObject parseToJson(byte[] payload) {
    return new JSONObject();
  }

  @Override
  public MessageFormatter getInstance(SchemaConfig config) throws IOException {
    return this;
  }

}
