package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;

public class RawFormatter implements MessageFormatter {

  @Override
  public MessageFormatter getInstance(SchemaConfig config) throws IOException {
    return this;
  }

}
