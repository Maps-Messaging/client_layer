package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;
import java.util.Base64;
import java.util.Map;
import java.util.Map.Entry;
import lombok.NonNull;
import org.apache.qpid.proton.amqp.Symbol;
import org.apache.qpid.proton.amqp.messaging.ApplicationProperties;
import org.apache.qpid.proton.amqp.messaging.Data;
import org.apache.qpid.proton.amqp.messaging.DeliveryAnnotations;
import org.apache.qpid.proton.amqp.messaging.Footer;
import org.apache.qpid.proton.amqp.messaging.MessageAnnotations;
import org.apache.qpid.proton.amqp.messaging.Properties;
import org.apache.qpid.proton.amqp.messaging.Section;
import org.apache.qpid.proton.codec.DroppingWritableBuffer;
import org.apache.qpid.proton.codec.WritableBuffer;
import org.apache.qpid.proton.message.Message;
import org.apache.qpid.proton.message.Message.Factory;
import org.json.JSONObject;

public class QpidJmsFormatter implements MessageFormatter {

  public String getName() {
    return "QPID-JMS";
  }
  public QpidJmsFormatter(){}

  @Override
  public MessageFormatter getInstance(SchemaConfig config) throws IOException {
    return this;
  }


  @Override
  public Object parse(@NonNull byte[] payload) {
    Message protonMsg = Factory.create();
    return protonMsg.decode(payload, 0, payload.length);
  }

  @Override
  public JSONObject parseToJson(byte[] payload) throws IOException {
    Message protonMsg = (Message) parse(payload);
    JSONObject jsonObject = new JSONObject();
    pack(jsonObject, "address", protonMsg.getAddress());
    pack(jsonObject, "content-type", protonMsg.getContentType());
    pack(jsonObject, "creation-time", protonMsg.getCreationTime());
    pack(jsonObject, "content-encoding", protonMsg.getContentEncoding());
    pack(jsonObject, "delivery-count", protonMsg.getDeliveryCount());
    pack(jsonObject, "expiry-time", protonMsg.getExpiryTime());
    pack(jsonObject, "priority", protonMsg.getPriority());
    pack(jsonObject, "reply-to", protonMsg.getReplyTo());
    pack(jsonObject, "reply-to-group-id", protonMsg.getReplyToGroupId());
    pack(jsonObject, "subject", protonMsg.getSubject());
    pack(jsonObject, "TTL", protonMsg.getTtl());
    pack(jsonObject, "group-id", protonMsg.getGroupId());
    pack(jsonObject, "group-sequence", protonMsg.getGroupSequence());
    pack(jsonObject, "user-id", protonMsg.getUserId());
    pack(jsonObject, "message-id", protonMsg.getMessageId());
    pack(jsonObject, "correlation-id", protonMsg.getCorrelationId());

    pack(jsonObject, "error", protonMsg.getError());
    pack(jsonObject, "footer", protonMsg.getFooter());
    pack(jsonObject, "application-properties", protonMsg.getApplicationProperties());
    pack(jsonObject, "delivery-annotations", protonMsg.getDeliveryAnnotations());
    pack(jsonObject, "message-annotations", protonMsg.getMessageAnnotations());

    Section body = protonMsg.getBody();
    if(body instanceof Data){
      Data data = (Data) body;
      jsonObject.put("data", new String(Base64.getEncoder().encode(data.getValue().getArray())));
    }
    return jsonObject;
  }

  private void pack(JSONObject jsonObject, String key, Object val){
    if(val == null) return;
    if(val instanceof String){
      jsonObject.put(key, val);
    }
    else if(val instanceof Number){
      jsonObject.put( key, val);
    }
    else if(val instanceof Footer){
      Footer footer = (Footer) val;
      JSONObject jsonFooter = new JSONObject();
      for(Object lookup:footer.getValue().entrySet()){
        Entry entry= (Entry)lookup;
        pack(jsonFooter, entry.getKey().toString(), entry.getValue());
      }
      jsonObject.put(key, jsonFooter);
    }
    else if(val instanceof ApplicationProperties){
      ApplicationProperties applicationProperties = (ApplicationProperties) val;
      JSONObject jsonApplication = new JSONObject();
      for(Entry<String, Object> entry:applicationProperties.getValue().entrySet()){
        pack(jsonApplication, entry.getKey(), entry.getValue());
      }
      jsonObject.put(key, jsonApplication);
    }
    else if(val instanceof DeliveryAnnotations){
      DeliveryAnnotations deliveryAnnotations = (DeliveryAnnotations) val;
      JSONObject jsonDeliveryAnnotations = new JSONObject();
      for(Entry<Symbol, Object> entry:deliveryAnnotations.getValue().entrySet()){
        pack(jsonDeliveryAnnotations, entry.getKey().toString(), entry.getValue());
      }
      jsonObject.put(key, jsonDeliveryAnnotations);
    }
    else if(val instanceof MessageAnnotations){
      MessageAnnotations messageAnnotations = (MessageAnnotations) val;
      JSONObject jsonMessageAnnotations = new JSONObject();
      for(Entry<Symbol, Object> entry:messageAnnotations.getValue().entrySet()){
        pack(jsonMessageAnnotations, entry.getKey().toString(), entry.getValue());
      }
      jsonObject.put(key, jsonMessageAnnotations);
    }
    else{
      jsonObject.put( key, val.toString());
    }
  }
  
  @Override
  public byte[] pack(Object object) throws IOException {
    if (object instanceof Message) {
      Message protonMsg = (Message) object;
      WritableBuffer sizingBuffer = new DroppingWritableBuffer();
      protonMsg.encode(sizingBuffer);
      byte[] data = new byte[sizingBuffer.position() + 10];
      int size = protonMsg.encode(data, 0, data.length);
      if (size != data.length) {
        byte[] tmp = new byte[size];
        System.arraycopy(data, 0, tmp, 0, size);
        data = tmp;
      }
      return data;
    }
    throw new IOException("Unexpected object to be packed");
  }

}