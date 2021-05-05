package grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.37.0)",
    comments = "Source: test.proto")
public final class RequirementGrpc {

  private RequirementGrpc() {}

  public static final String SERVICE_NAME = "Requirement";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.Value,
      grpc.Value> getGetJSONMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "getJSON",
      requestType = grpc.Value.class,
      responseType = grpc.Value.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.Value,
      grpc.Value> getGetJSONMethod() {
    io.grpc.MethodDescriptor<grpc.Value, grpc.Value> getGetJSONMethod;
    if ((getGetJSONMethod = RequirementGrpc.getGetJSONMethod) == null) {
      synchronized (RequirementGrpc.class) {
        if ((getGetJSONMethod = RequirementGrpc.getGetJSONMethod) == null) {
          RequirementGrpc.getGetJSONMethod = getGetJSONMethod =
              io.grpc.MethodDescriptor.<grpc.Value, grpc.Value>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "getJSON"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.Value.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.Value.getDefaultInstance()))
              .setSchemaDescriptor(new RequirementMethodDescriptorSupplier("getJSON"))
              .build();
        }
      }
    }
    return getGetJSONMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RequirementStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RequirementStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RequirementStub>() {
        @java.lang.Override
        public RequirementStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RequirementStub(channel, callOptions);
        }
      };
    return RequirementStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RequirementBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RequirementBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RequirementBlockingStub>() {
        @java.lang.Override
        public RequirementBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RequirementBlockingStub(channel, callOptions);
        }
      };
    return RequirementBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RequirementFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<RequirementFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<RequirementFutureStub>() {
        @java.lang.Override
        public RequirementFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new RequirementFutureStub(channel, callOptions);
        }
      };
    return RequirementFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class RequirementImplBase implements io.grpc.BindableService {

    /**
     */
    public void getJSON(grpc.Value request,
        io.grpc.stub.StreamObserver<grpc.Value> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetJSONMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetJSONMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                grpc.Value,
                grpc.Value>(
                  this, METHODID_GET_JSON)))
          .build();
    }
  }

  /**
   */
  public static final class RequirementStub extends io.grpc.stub.AbstractAsyncStub<RequirementStub> {
    private RequirementStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RequirementStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RequirementStub(channel, callOptions);
    }

    /**
     */
    public void getJSON(grpc.Value request,
        io.grpc.stub.StreamObserver<grpc.Value> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetJSONMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class RequirementBlockingStub extends io.grpc.stub.AbstractBlockingStub<RequirementBlockingStub> {
    private RequirementBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RequirementBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RequirementBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpc.Value getJSON(grpc.Value request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetJSONMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RequirementFutureStub extends io.grpc.stub.AbstractFutureStub<RequirementFutureStub> {
    private RequirementFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected RequirementFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new RequirementFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.Value> getJSON(
        grpc.Value request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetJSONMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_JSON = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RequirementImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RequirementImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_JSON:
          serviceImpl.getJSON((grpc.Value) request,
              (io.grpc.stub.StreamObserver<grpc.Value>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RequirementBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RequirementBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.Test.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Requirement");
    }
  }

  private static final class RequirementFileDescriptorSupplier
      extends RequirementBaseDescriptorSupplier {
    RequirementFileDescriptorSupplier() {}
  }

  private static final class RequirementMethodDescriptorSupplier
      extends RequirementBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RequirementMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RequirementGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RequirementFileDescriptorSupplier())
              .addMethod(getGetJSONMethod())
              .build();
        }
      }
    }
    return result;
  }
}
