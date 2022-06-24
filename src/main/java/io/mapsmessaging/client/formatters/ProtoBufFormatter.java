package io.mapsmessaging.client.formatters;

import com.google.protobuf.DescriptorProtos;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Descriptors.DescriptorValidationException;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import io.mapsmessaging.client.schema.ProtoBufSchemaConfig;
import io.mapsmessaging.client.schema.SchemaConfig;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ProtoBufFormatter  implements MessageFormatter {

  private final String messageName;
  private final FileDescriptor descriptor;

  ProtoBufFormatter(String messageName, byte[] descriptorImage) throws IOException {
    try {
      this.descriptor = loadDescFile(descriptorImage);
      this.messageName = messageName;
    } catch (DescriptorValidationException e) {
      throw new IOException(e);
    }
  }

  public String getName(){
    return "ProtoBuf";
  }

  public Object parse(byte[] payload) throws InvalidProtocolBufferException {
    return DynamicMessage.parseFrom(descriptor.findMessageTypeByName(messageName), payload);
  }

  public byte[] pack(Object object) throws IOException {
    if(object instanceof com.google.protobuf.GeneratedMessageV3){
      return ((com.google.protobuf.GeneratedMessageV3)object).toByteArray();
    }
    throw new IOException("Unexpected object received");
  }

  @Override
  public MessageFormatter getInstance(SchemaConfig config) throws IOException {
    ProtoBufSchemaConfig protoBufSchemaConfig = (ProtoBufSchemaConfig) config;
    return new ProtoBufFormatter(protoBufSchemaConfig.getMessageName(), protoBufSchemaConfig.getDescriptor());
  }

  private FileDescriptor loadDescFile(byte[] descriptorImage) throws IOException, DescriptorValidationException {
    InputStream fin = new ByteArrayInputStream(descriptorImage);
    DescriptorProtos.FileDescriptorSet set;
    List<FileDescriptor> dependencyFileDescriptorList;
    try {
      set = DescriptorProtos.FileDescriptorSet.parseFrom(fin);
      dependencyFileDescriptorList = new ArrayList<>();
      for(int i=0; i<set.getFileCount()-1;i++) {
        dependencyFileDescriptorList.add(FileDescriptor.buildFrom(set.getFile(i), dependencyFileDescriptorList.toArray(new FileDescriptor[i])));
      }
    } finally {
      fin.close();
    }
    return Descriptors.FileDescriptor.buildFrom(set.getFile(set.getFileCount()-1), dependencyFileDescriptorList.toArray(new FileDescriptor[0]));
  }
}