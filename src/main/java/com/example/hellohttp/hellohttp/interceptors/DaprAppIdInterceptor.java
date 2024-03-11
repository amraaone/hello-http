package com.example.hellohttp.hellohttp.interceptors;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;

public class DaprAppIdInterceptor implements ClientInterceptor {

  private final String appId;

  public DaprAppIdInterceptor(String appId) {
    this.appId = appId;
  }

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
    MethodDescriptor<ReqT, RespT> method,
    CallOptions callOptions,
    Channel next
  ) {
    ClientCall<ReqT, RespT> call = next.newCall(method, callOptions);
    return new ForwardingClientCall.SimpleForwardingClientCall<>(call) {
      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        headers.put(
          Metadata.Key.of("dapr-app-id", Metadata.ASCII_STRING_MARSHALLER),
          appId
        );
        super.start(responseListener, headers);
      }
    };
  }
}
