package grpc;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.37.0)",
    comments = "Source: requirement.proto")
public final class StructurationGrpc {

  private StructurationGrpc() {}

  public static final String SERVICE_NAME = "Structuration";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<grpc.Request,
      grpc.Response> getReqStructureMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "reqStructure",
      requestType = grpc.Request.class,
      responseType = grpc.Response.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<grpc.Request,
      grpc.Response> getReqStructureMethod() {
    io.grpc.MethodDescriptor<grpc.Request, grpc.Response> getReqStructureMethod;
    if ((getReqStructureMethod = StructurationGrpc.getReqStructureMethod) == null) {
      synchronized (StructurationGrpc.class) {
        if ((getReqStructureMethod = StructurationGrpc.getReqStructureMethod) == null) {
          StructurationGrpc.getReqStructureMethod = getReqStructureMethod =
              io.grpc.MethodDescriptor.<grpc.Request, grpc.Response>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "reqStructure"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.Request.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  grpc.Response.getDefaultInstance()))
              .setSchemaDescriptor(new StructurationMethodDescriptorSupplier("reqStructure"))
              .build();
        }
      }
    }
    return getReqStructureMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static StructurationStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StructurationStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StructurationStub>() {
        @java.lang.Override
        public StructurationStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StructurationStub(channel, callOptions);
        }
      };
    return StructurationStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static StructurationBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StructurationBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StructurationBlockingStub>() {
        @java.lang.Override
        public StructurationBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StructurationBlockingStub(channel, callOptions);
        }
      };
    return StructurationBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static StructurationFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<StructurationFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<StructurationFutureStub>() {
        @java.lang.Override
        public StructurationFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new StructurationFutureStub(channel, callOptions);
        }
      };
    return StructurationFutureStub.newStub(factory, channel);
  }

  /**
   */
  public static abstract class StructurationImplBase implements io.grpc.BindableService {

    /**
     */
    public void reqStructure(grpc.Request request,
        io.grpc.stub.StreamObserver<grpc.Response> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReqStructureMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getReqStructureMethod(),
            io.grpc.stub.ServerCalls.asyncUnaryCall(
              new MethodHandlers<
                grpc.Request,
                grpc.Response>(
                  this, METHODID_REQ_STRUCTURE)))
          .build();
    }
  }

  /**
   */
  public static final class StructurationStub extends io.grpc.stub.AbstractAsyncStub<StructurationStub> {
    private StructurationStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StructurationStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StructurationStub(channel, callOptions);
    }

    /**
     */
    public void reqStructure(grpc.Request request,
        io.grpc.stub.StreamObserver<grpc.Response> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReqStructureMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class StructurationBlockingStub extends io.grpc.stub.AbstractBlockingStub<StructurationBlockingStub> {
    private StructurationBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StructurationBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StructurationBlockingStub(channel, callOptions);
    }

    /**
     */
    public grpc.Response reqStructure(grpc.Request request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReqStructureMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class StructurationFutureStub extends io.grpc.stub.AbstractFutureStub<StructurationFutureStub> {
    private StructurationFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected StructurationFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new StructurationFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<grpc.Response> reqStructure(
        grpc.Request request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReqStructureMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQ_STRUCTURE = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final StructurationImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(StructurationImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQ_STRUCTURE:
          serviceImpl.reqStructure((grpc.Request) request,
              (io.grpc.stub.StreamObserver<grpc.Response>) responseObserver);
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

  private static abstract class StructurationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    StructurationBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return grpc.Requirement.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("Structuration");
    }
  }

  private static final class StructurationFileDescriptorSupplier
      extends StructurationBaseDescriptorSupplier {
    StructurationFileDescriptorSupplier() {}
  }

  private static final class StructurationMethodDescriptorSupplier
      extends StructurationBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    StructurationMethodDescriptorSupplier(String methodName) {
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
      synchronized (StructurationGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new StructurationFileDescriptorSupplier())
              .addMethod(getReqStructureMethod())
              .build();
        }
      }
    }
    return result;
  }
}
