package com.example.hellohttp.hellohttp.services;

import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;
import org.example.hellogrpc.GreeterGrpc;
import org.example.hellogrpc.HelloReply;
import org.example.hellogrpc.HelloRequest;
import org.springframework.stereotype.Service;

@Service
public class HelloServiceClient {

  private final ManagedChannel channel;

  public HelloServiceClient() {
    this.channel =
      Grpc
        .newChannelBuilder(
          "localhost:50051",
          InsecureChannelCredentials.create()
        )
        .build();
  }

  public String sayHello(String name) throws Exception {
    System.out.println("NAMENAMENAME=======" + name);
    GreeterGrpc.GreeterBlockingStub blockingStub = GreeterGrpc.newBlockingStub(
      channel
    );

    Metadata headers = new Metadata();
    headers.put(
      Metadata.Key.of("dapr-app-id", Metadata.ASCII_STRING_MARSHALLER),
      "hello-grpc"
    );

    blockingStub =
      blockingStub.withInterceptors(
        MetadataUtils.newAttachHeadersInterceptor(headers)
      );

    System.out.println(
      "will try to greet " + name + "-=-=-=-=-=-=-=-=-=-=-=-="
    );

    HelloRequest request = HelloRequest.newBuilder().setName(name).build();
    HelloReply response = blockingStub.sayHello(request);

    return response.getMessage();
  }

  @PreDestroy
  public void cleanup() {
    try {
      channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
