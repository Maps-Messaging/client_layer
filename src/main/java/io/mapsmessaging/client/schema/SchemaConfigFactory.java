package io.mapsmessaging.client.schema;

import java.io.IOException;
import org.json.JSONObject;

public class SchemaConfigFactory {

  public static SchemaConfig parse(byte[] rawPayload) throws IOException {
    return parse(new String(rawPayload));
  }

  public static SchemaConfig parse(String payload) throws IOException {
    JSONObject schemaJson = new JSONObject(payload);
    if(!schemaJson.has("schema")) throw new IOException("Not a valid schema config");

    schemaJson = schemaJson.getJSONObject("schema");
    if(!schemaJson.has("format")) throw new IOException("Not a valid schema config");

    String format = schemaJson.getString("format");
    switch(format){
      case "JSON":
        return new JsonSchemaConfig();

      case "XML":
        return new XmlSchemaConfig();

      case "ProtoBuf":
        return new ProtoBufSchemaConfig(schemaJson.getString("messageName"), schemaJson.getString("descriptor"));

      case "RAW":
        return new RawSchemaConfig();

      default:
        throw new IOException("Unknown schema config found");
    }
  }


}
