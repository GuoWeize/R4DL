// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: requirement.proto

package grpc;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;

/**
 * @author protocol buffer compiler
 */
public final class Requirement {
    private Requirement() {}

    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistryLite registry) {}
    public static void registerAllExtensions(com.google.protobuf.ExtensionRegistry registry) {
        registerAllExtensions((com.google.protobuf.ExtensionRegistryLite) registry);
    }

    static final Descriptors.Descriptor internal_static_Request_descriptor;
    static final GeneratedMessageV3.FieldAccessorTable internal_static_Request_fieldAccessorTable;
    static final Descriptors.Descriptor internal_static_Response_descriptor;
    static final GeneratedMessageV3.FieldAccessorTable internal_static_Response_fieldAccessorTable;
    private static final Descriptors.FileDescriptor descriptor;

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = {
            "\n\021requirement.proto\",\n\007Request\022\014\n\004type\030\001" +
                " \001(\t\022\023\n\013description\030\002 \001(\t\")\n\010Response\022\014\n" +
                "\004type\030\001 \001(\t\022\017\n\007reqJson\030\002 \001(\t26\n\rStructur" +
                "ation\022%\n\014reqStructure\022\010.Request\032\t.Respon" +
                "se\"\000B\010\n\004grpcP\001b\006proto3"
        };
        descriptor = 
            Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(
                descriptorData,
                new Descriptors.FileDescriptor[] {}
            );
        
        internal_static_Request_descriptor = getDescriptor().getMessageTypes().get(0);
        internal_static_Request_fieldAccessorTable = 
            new GeneratedMessageV3.FieldAccessorTable(
                internal_static_Request_descriptor,
                new String[] {"Type", "Description",}
            );
        
        internal_static_Response_descriptor = getDescriptor().getMessageTypes().get(1);
        internal_static_Response_fieldAccessorTable = 
            new GeneratedMessageV3.FieldAccessorTable(
                internal_static_Response_descriptor,
                new String[] {"Type", "ReqJson",}
            );
    }

    // @@protoc_insertion_point(outer_class_scope)
}
