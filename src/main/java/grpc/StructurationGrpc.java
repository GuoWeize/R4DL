package grpc;

import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.stub.ServerCalls;
import io.grpc.stub.StreamObserver;

@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.37.0)",
    comments = "Source: requirement.proto")
public final class StructurationGrpc {

    private StructurationGrpc() {}

    public static final String SERVICE_NAME = "Structuration";

    // Static method descriptors that strictly reflect the proto.
    private static volatile io.grpc.MethodDescriptor<Request, Response> getReqStructureMethod;

    @io.grpc.stub.annotations.RpcMethod(
        fullMethodName = SERVICE_NAME + '/' + "reqStructure",
        requestType = Request.class,
        responseType = Response.class,
        methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
    public static io.grpc.MethodDescriptor<Request, Response> getReqStructureMethod() {
        io.grpc.MethodDescriptor<Request, Response> getReqStructureMethod;
        if ((getReqStructureMethod = StructurationGrpc.getReqStructureMethod) == null) {
            synchronized (StructurationGrpc.class) {
                if ((getReqStructureMethod = StructurationGrpc.getReqStructureMethod) == null) {
                    StructurationGrpc.getReqStructureMethod = getReqStructureMethod =
                        io.grpc.MethodDescriptor.<Request, Response>newBuilder()
                            .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
                            .setFullMethodName(io.grpc.MethodDescriptor.generateFullMethodName(SERVICE_NAME, "reqStructure"))
                            .setSampledToLocalTracing(true)
                            .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(Request.getDefaultInstance()))
                            .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(Response.getDefaultInstance()))
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
    public static StructurationStub newStub(Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<StructurationStub> factory =
            new io.grpc.stub.AbstractStub.StubFactory<StructurationStub>() {
                @Override
                public StructurationStub newStub(Channel channel, CallOptions callOptions) {
                    return new StructurationStub(channel, callOptions);
                }
            };
        return StructurationStub.newStub(factory, channel);
    }

    /**
     * Creates a new blocking-style stub that supports unary and streaming output calls on the service
     */
    public static StructurationBlockingStub newBlockingStub(Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<StructurationBlockingStub> factory =
            new io.grpc.stub.AbstractStub.StubFactory<StructurationBlockingStub>() {
                @Override
                public StructurationBlockingStub newStub(Channel channel, CallOptions callOptions) {
                    return new StructurationBlockingStub(channel, callOptions);
                }
            };
        return StructurationBlockingStub.newStub(factory, channel);
    }

    /**
     * Creates a new ListenableFuture-style stub that supports unary calls on the service
     */
    public static StructurationFutureStub newFutureStub(Channel channel) {
        io.grpc.stub.AbstractStub.StubFactory<StructurationFutureStub> factory =
            new io.grpc.stub.AbstractStub.StubFactory<StructurationFutureStub>() {
                @Override
                public StructurationFutureStub newStub(Channel channel, CallOptions callOptions) {
                    return new StructurationFutureStub(channel, callOptions);
                }
            };
        return StructurationFutureStub.newStub(factory, channel);
    }

    /**
     * Abstract Class for Structuration, should be implemented in Server.
     */
    public static abstract class StructurationImplBase implements io.grpc.BindableService {

        /**
         * The specific rpc, should be override in Server.
         */
        public void reqStructure(Request request, StreamObserver<Response> responseObserver) {
            ServerCalls.asyncUnimplementedUnaryCall(getReqStructureMethod(), responseObserver);
        }

        @Override
        public final io.grpc.ServerServiceDefinition bindService() {
            return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
                .addMethod(
                    getReqStructureMethod(),
                    ServerCalls.asyncUnaryCall(new MethodHandlers<Request, Response>(this, METHODID_REQ_STRUCTURE)))
                .build();
        }
    }

    /**
     */
    public static final class StructurationStub extends io.grpc.stub.AbstractAsyncStub<StructurationStub> {
        private StructurationStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected StructurationStub build(Channel channel, CallOptions callOptions) {
            return new StructurationStub(channel, callOptions);
        }

        /**
         */
        public void reqStructure(Request request, StreamObserver<Response> responseObserver) {
            io.grpc.stub.ClientCalls.asyncUnaryCall(
                getChannel().newCall(getReqStructureMethod(), getCallOptions()), request, responseObserver);
        }
    }

    /**
     */
    public static final class StructurationBlockingStub extends io.grpc.stub.AbstractBlockingStub<StructurationBlockingStub> {
        private StructurationBlockingStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected StructurationBlockingStub build(Channel channel, CallOptions callOptions) {
            return new StructurationBlockingStub(channel, callOptions);
        }

        /**
         */
        public Response reqStructure(Request request) {
            return io.grpc.stub.ClientCalls.blockingUnaryCall(getChannel(), getReqStructureMethod(), getCallOptions(), request);
        }
    }

    /**
     */
    public static final class StructurationFutureStub extends io.grpc.stub.AbstractFutureStub<StructurationFutureStub> {
        private StructurationFutureStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected StructurationFutureStub build(Channel channel, CallOptions callOptions) {
            return new StructurationFutureStub(channel, callOptions);
        }

        /**
         */
        public com.google.common.util.concurrent.ListenableFuture<Response> reqStructure(Request request) {
            return io.grpc.stub.ClientCalls.futureUnaryCall(getChannel().newCall(getReqStructureMethod(), getCallOptions()), request);
        }
    }

    private static final int METHODID_REQ_STRUCTURE = 0;

    private static final class MethodHandlers<Req, Resp> implements
        ServerCalls.UnaryMethod<Req, Resp>,
        ServerCalls.ServerStreamingMethod<Req, Resp>,
        ServerCalls.ClientStreamingMethod<Req, Resp>,
        ServerCalls.BidiStreamingMethod<Req, Resp> {
        private final StructurationImplBase serviceImpl;
        private final int methodId;

        MethodHandlers(StructurationImplBase serviceImpl, int methodId) {
            this.serviceImpl = serviceImpl;
            this.methodId = methodId;
        }

        @Override
        @SuppressWarnings("unchecked")
        public void invoke(Req request, StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                case METHODID_REQ_STRUCTURE:
                    serviceImpl.reqStructure((Request) request, (StreamObserver<Response>) responseObserver);
                    break;
                default:
                    throw new AssertionError();
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public io.grpc.stub.StreamObserver<Req> invoke(StreamObserver<Resp> responseObserver) {
            switch (methodId) {
                default:
                    throw new AssertionError();
            }
        }
    }

    private static abstract class StructurationBaseDescriptorSupplier
        implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
        StructurationBaseDescriptorSupplier() {}

        @Override
        public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
            return grpc.Requirement.getDescriptor();
        }

        @Override
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

        @Override
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
