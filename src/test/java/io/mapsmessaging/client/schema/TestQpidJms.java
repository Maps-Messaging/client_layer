package io.mapsmessaging.client.schema;

import java.io.IOException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestQpidJms {

  @Test
  void testJsonSchemaConfig() {
    SchemaConfig config = new QpidJmsSchemaConfig();
    Assertions.assertEquals("QPID-JMS", config.getFormat());
    String packed = config.pack();
    JSONObject jsonObject = new JSONObject(packed);
    Assertions.assertEquals("QPID-JMS", jsonObject.getJSONObject("schema").get("format"));
  }

  @Test
  void testSchemaReload() throws IOException {
    SchemaConfig config = new QpidJmsSchemaConfig();
    Assertions.assertEquals("QPID-JMS", config.getFormat());
    String packed = config.pack();
    SchemaConfig parsed = SchemaConfigFactory.getInstance().parse(packed);
    Assertions.assertEquals("QPID-JMS", parsed.getFormat());
  }
}
