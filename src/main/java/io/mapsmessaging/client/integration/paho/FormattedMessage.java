package io.mapsmessaging.client.integration.paho;

import io.mapsmessaging.client.formatters.MessageFormatter;
import java.io.IOException;
import lombok.Getter;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

public class FormattedMessage {

  @Getter
  private final String topic;
  @Getter
  private final MqttMessage mqttMessage;
  private final MessageFormatter formatter;

  protected FormattedMessage(String topic, MqttMessage message, MessageFormatter formatter){
    this.topic = topic;
    this.mqttMessage = message;
    this.formatter = formatter;
  }

  public Object getParsedMessage() throws IOException {
    return formatter.parse(mqttMessage.getPayload());
  }

  public JSONObject getParsedMessageAsJson() throws IOException {
    return formatter.parseToJson(mqttMessage.getPayload());
  }
}
