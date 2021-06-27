package grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Guo Weize
 * @date 2021/5/5
 */
public final class gRPCServer {
    private Server server;
    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50051;
        server = ServerBuilder.forPort(port)
            .addService(new HelloIml())
            .build()
            .start();
        System.out.println("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                gRPCServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }

    private void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        final gRPCServer server = new gRPCServer();
        server.start();
        server.blockUntilShutdown();
    }

    private static class HelloIml extends RequirementGrpc.RequirementImplBase{
        @Override
        public void getJSON(Value request, StreamObserver<Value> responseObserver) {
            String response = "Hello " + request.getValue() + ", I'm Java grpc Server";
            Value helloResponse = Value.newBuilder().setValue(response).build();

            responseObserver.onNext(helloResponse);
            responseObserver.onCompleted();
        }
    }
}
