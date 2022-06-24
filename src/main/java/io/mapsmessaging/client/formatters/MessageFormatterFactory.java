package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.ProtoBufSchemaConfig;
import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;

public class MessageFormatterFactory {

  private static final MessageFormatterFactory instance = new MessageFormatterFactory();

  public static MessageFormatterFactory getInstance(){
    return instance;
  }

  public MessageFormatter getFormatter(SchemaConfig config)throws IOException {
    switch(config.getFormat()){
      case "JSON":
        return new JsonFormatter();

      case "XML":
        return new XmlFormatter();

      case "ProtoBuf":
        ProtoBufSchemaConfig protoBufSchemaConfig = (ProtoBufSchemaConfig) config;
        return new ProtoBufFormatter(protoBufSchemaConfig.getMessageName(), protoBufSchemaConfig.getDescriptor());

      case "RAW":
        return new RawFormatter();

      default:
        throw new IOException("Unknown format config received");
    }
  }

}

