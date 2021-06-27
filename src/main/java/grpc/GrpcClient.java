package grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Guo Weize
 * @date 2021/6/26
 */
@Slf4j
public class GrpcClient {

    private static final int AWAIT_TERMINATION_TIME = 3;
    private final List<ManagedChannel> channels = new ArrayList<>();
    private final Map<String, StructurationGrpc.StructurationBlockingStub> stubs = new HashMap<>();

    public void registerChannel(String type, String port) {
        ManagedChannel channel = ManagedChannelBuilder.forTarget(port).usePlaintext().build();
        channels.add(channel);
        StructurationGrpc.StructurationBlockingStub blockingStub = StructurationGrpc.newBlockingStub(channel);
        stubs.put(type, blockingStub);
    }

    /** Say hello to server. */
    public void structure(String type, String description) {
        Request request = Request.newBuilder().setType(type).setDescription(description).build();
        Response response;
        StructurationGrpc.StructurationBlockingStub stub = stubs.getOrDefault(type, null);
        if (stub == null) {
            log.warn("RPC failed: Can not find type \"" + type + "\"");
            return;
        }
        try {
            response = stub.reqStructure(request);
        } catch (StatusRuntimeException e) {
            log.warn("RPC failed: " + e.getStatus());
            return;
        }
        log.warn("type: " + response.getType());
        log.warn("response: " + response.getReqJson());
    }

    public void shutdown() {
        for (ManagedChannel channel: channels) {
            try {
                channel.shutdownNow().awaitTermination(AWAIT_TERMINATION_TIME, TimeUnit.SECONDS);
            }
            catch (InterruptedException e) {
                log.warn("Can not shut down channel.");
            }
        }
    }

    public static GrpcClient start(Map<String, String> ports) {
        GrpcClient client = new GrpcClient();
        ports.forEach(client::registerChannel);
        return client;
    }

    public static void main(String[] args) {
        Map<String, String> ports = Map.ofEntries(
            Map.entry("functional", "localhost:50051")
        );
        GrpcClient client = GrpcClient.start(ports);
        client.structure("functional", "blablabla...");
        client.shutdown();
    }

}
