package io.mapsmessaging.client.formatters;

import java.io.IOException;

public interface MessageFormatter {

  default Object parse(byte[] payload) throws IOException {
    return payload;
  }

  default byte[] pack(Object object) throws IOException{
    if(object instanceof byte[]){
      return (byte[]) object;
    }
    throw new IOException("Unexpected object to be packed");
  }

}
