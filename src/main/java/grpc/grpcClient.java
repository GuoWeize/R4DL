package grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Guo Weize
 * @date 2021/6/26
 */
public class grpcClient {

    private static final Logger logger = Logger.getLogger(grpcClient.class.getName());

    private final RequirementGrpc.RequirementBlockingStub blockingStub;

    /** Construct client for accessing HelloWorld server using the existing channel. */
    public grpcClient(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to shut it down.
        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = RequirementGrpc.newBlockingStub(channel);
    }

    /** Say hello to server. */
    public void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        Value request = Value.newBuilder().setValue(name).build();
        Value response;
        try {
            response = blockingStub.getJSON(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getValue());
    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting. The second argument is the target server.
     */
    public static void main(String[] args) throws Exception {
        String user = "Guo Weize";
        // Access a service running on the specific machine on the specific port
        String ip = "localhost";
        String port = "50051";
        String target = ip + ":" + port;

        // Create a communication channel to the server, known as a Channel.
        // Channels are thread-safe and reusable.
        // It is common to create channels at the beginning of your application and reuse them until the application shuts down.
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
            // Channels are secure by default (via SSL/TLS).
            // For the example we disable TLS to avoid needing certificates.
            .usePlaintext()
            .build();
        try {
            grpcClient client = new grpcClient(channel);
            client.greet(user);
        } finally {
            // ManagedChannels use resources like threads and TCP connections.
            // To prevent leaking these resources, the channel should be shut down when it will no longer be used.
            // If it may be used again, leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

}
