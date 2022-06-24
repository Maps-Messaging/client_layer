package io.mapsmessaging.client.integration.paho;

import org.eclipse.paho.client.mqttv3.MqttCallback;

public interface FormattedCallback extends MqttCallback {

  void formattedMessage(FormattedMessage formattedMessage) throws Exception;

}
