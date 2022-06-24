package io.mapsmessaging.client.schema;

import java.io.IOException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class TestRaw {

  @Test
  void testRaw() {
    SchemaConfig config = new RawSchemaConfig();
    Assertions.assertEquals("RAW", config.getFormat());
    String packed = config.pack();
    JSONObject jsonObject = new JSONObject(packed);
    Assertions.assertEquals("RAW", jsonObject.getJSONObject("schema").get("format"));
  }

  @Test
  void testSchemaReload() throws IOException {
    SchemaConfig config = new RawSchemaConfig();
    Assertions.assertEquals("RAW", config.getFormat());
    String packed = config.pack();
    SchemaConfig parsed = SchemaConfigFactory.parse(packed);
    Assertions.assertEquals("RAW", parsed.getFormat());
  }

}
