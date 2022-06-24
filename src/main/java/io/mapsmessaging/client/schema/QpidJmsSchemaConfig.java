package io.mapsmessaging.client.schema;

import org.json.JSONObject;

public class QpidJmsSchemaConfig extends SimpleSchemaConfig {
  public QpidJmsSchemaConfig() {
    super("QPID-JMS");
  }

  protected SchemaConfig getInstance(JSONObject config){
    return this;
  }
}