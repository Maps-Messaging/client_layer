package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.json.JSONObject;

public class JsonFormatter implements MessageFormatter {

  public JsonFormatter(){}

  public Object parse(byte[] payload) {
    return new JSONObject(new String(payload));
  }

  @Override
  public JSONObject parseToJson(byte[] payload) throws IOException {
    return (JSONObject) parse(payload);
  }

  public byte[] pack(Object object) throws IOException {
    String toPack = null;
    if (object instanceof String) {
      toPack = (String) object;
    }
    if (object instanceof JSONObject) {
      toPack = ((JSONObject) object).toString(2);
    }
    if (toPack != null) {
      return toPack.getBytes(StandardCharsets.UTF_8);
    }
    throw new IOException("Unexpected object to be packed");
  }

  @Override
  public MessageFormatter getInstance(SchemaConfig config) throws IOException {
    return this;
  }

  @Override
  public String getName() {
    return "JSON";
  }

}
