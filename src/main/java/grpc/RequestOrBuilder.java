// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: requirement.proto

package grpc;

import com.google.protobuf.ByteString;

/**
 * @author protocol buffer compiler
 */
public interface RequestOrBuilder extends
    // @@protoc_insertion_point(interface_extends:Request)
    com.google.protobuf.MessageOrBuilder {

    /**
    * <code>string type = 1;</code>
    * @return The type.
    */
    String getType();
    /**
    * <code>string type = 1;</code>
    * @return The bytes for type.
    */
    ByteString getTypeBytes();

    /**
    * <code>string description = 2;</code>
    * @return The description.
    */
    String getDescription();
    /**
    * <code>string description = 2;</code>
    * @return The bytes for description.
    */
    ByteString getDescriptionBytes();
}
