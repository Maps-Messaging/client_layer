package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;
import java.util.ServiceLoader;

public class MessageFormatterFactory {

  private static final MessageFormatterFactory instance = new MessageFormatterFactory();

  public static MessageFormatterFactory getInstance() {
    return instance;
  }

  private final ServiceLoader<MessageFormatter> messageFormatterServiceLoader;

  public MessageFormatter getFormatter(SchemaConfig config) throws IOException {
    for (MessageFormatter formatter : messageFormatterServiceLoader) {
      if (formatter.getName().equalsIgnoreCase(config.getFormat())) {
        return formatter.getInstance(config);
      }
    }
    throw new IOException("Unknown format config received");
  }

  private MessageFormatterFactory() {
    messageFormatterServiceLoader = ServiceLoader.load(MessageFormatter.class);
  }


}

