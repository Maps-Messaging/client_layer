package io.mapsmessaging.client.formatters;

import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.IOException;
import lombok.NonNull;
import org.apache.qpid.proton.codec.DroppingWritableBuffer;
import org.apache.qpid.proton.codec.WritableBuffer;
import org.apache.qpid.proton.message.Message;
import org.apache.qpid.proton.message.Message.Factory;

public class QpidJmsFormatter implements MessageFormatter {

  public String getName(){
    return "QPID-JMS";
  }

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
  public byte[] pack(Object object) throws IOException {
    if (object instanceof Message) {
      Message protonMsg = (Message) object;
      WritableBuffer sizingBuffer = new DroppingWritableBuffer();
      protonMsg.encode(sizingBuffer);
      byte[] data = new byte[sizingBuffer.position()+10];
      int size = protonMsg.encode(data, 0, data.length);
      if(size != data.length){
        byte[] tmp = new byte[size];
        System.arraycopy(data, 0, tmp, 0, size);
        data = tmp;
      }
      return data;
    }
    throw new IOException("Unexpected object to be packed");
  }

}