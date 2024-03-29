// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: requirement.proto

package grpc;

/**
 * Protobuf type {@code Response}
 * @author protocol buffer compiler
 */
public final class Response extends
    com.google.protobuf.GeneratedMessageV3 implements
    // @@protoc_insertion_point(message_implements:Response)
    ResponseOrBuilder {
    private static final long serialVersionUID = 0L;
    // Use Response.newBuilder() to construct.
    private Response(com.google.protobuf.GeneratedMessageV3.Builder<?> builder) {
        super(builder);
    }
    private Response() {
        type_ = "";
        reqJson_ = "";
    }

    @Override
    @SuppressWarnings({"unused"})
    protected Object newInstance(UnusedPrivateParameter unused) {
        return new Response();
    }

    @Override
    public com.google.protobuf.UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }
    private Response(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry
    ) throws com.google.protobuf.InvalidProtocolBufferException {
        this();
        if (extensionRegistry == null) {
            throw new java.lang.NullPointerException();
        }
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
            com.google.protobuf.UnknownFieldSet.newBuilder();
        try {
            boolean done = false;
            while (!done) {
                int tag = input.readTag();
                switch (tag) {
                    case 0:
                        done = true;
                        break;
                    case 10: {
                        type_ = input.readStringRequireUtf8();
                        break;
                    }
                    case 18: {
                        reqJson_ = input.readStringRequireUtf8();
                        break;
                    }
                    default: {
                        if (!parseUnknownField(
                            input, unknownFields, extensionRegistry, tag)) {
                            done = true;
                        }
                        break;
                    }
                }
            }
        } catch (com.google.protobuf.InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (java.io.IOException e) {
            throw new com.google.protobuf.InvalidProtocolBufferException(
                e).setUnfinishedMessage(this);
        } finally {
            this.unknownFields = unknownFields.build();
            makeExtensionsImmutable();
        }
    }
    public static com.google.protobuf.Descriptors.Descriptor getDescriptor() {
        return grpc.Requirement.internal_static_Response_descriptor;
    }

    @Override
    protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return grpc.Requirement.internal_static_Response_fieldAccessorTable
                .ensureFieldAccessorsInitialized(grpc.Response.class, grpc.Response.Builder.class);
    }

    public static final int TYPE_FIELD_NUMBER = 1;
    private volatile java.lang.Object type_;
    /**
     * <code>string type = 1;</code>
     * @return The type.
     */
    @Override
    public String getType() {
        java.lang.Object ref = type_;
        if (ref instanceof String) {
            return (String) ref;
        } else {
            com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
            String s = bs.toStringUtf8();
            type_ = s;
            return s;
        }
    }
    /**
     * <code>string type = 1;</code>
     * @return The bytes for type.
     */
    @Override
    public com.google.protobuf.ByteString getTypeBytes() {
        java.lang.Object ref = type_;
        if (ref instanceof String) {
            com.google.protobuf.ByteString b =
                com.google.protobuf.ByteString.copyFromUtf8(
                    (String) ref);
            type_ = b;
            return b;
        } else {
            return (com.google.protobuf.ByteString) ref;
        }
    }

    public static final int REQJSON_FIELD_NUMBER = 2;
    private volatile java.lang.Object reqJson_;
    /**
     * <code>string reqJson = 2;</code>
     * @return The reqJson.
     */
    @Override
    public String getReqJson() {
        java.lang.Object ref = reqJson_;
        if (ref instanceof String) {
            return (String) ref;
        } else {
            com.google.protobuf.ByteString bs = (com.google.protobuf.ByteString) ref;
            String s = bs.toStringUtf8();
            reqJson_ = s;
            return s;
        }
    }
    /**
     * <code>string reqJson = 2;</code>
     * @return The bytes for reqJson.
     */
    @Override
    public com.google.protobuf.ByteString getReqJsonBytes() {
        java.lang.Object ref = reqJson_;
        if (ref instanceof String) {
            com.google.protobuf.ByteString b =
                com.google.protobuf.ByteString.copyFromUtf8((String) ref);
            reqJson_ = b;
            return b;
        } else {
            return (com.google.protobuf.ByteString) ref;
        }
    }

    private byte memoizedIsInitialized = -1;
    @Override
    public boolean isInitialized() {
        byte isInitialized = memoizedIsInitialized;
        if (isInitialized == 1) {
            return true;
        }
        if (isInitialized == 0) {
            return false;
        }
        memoizedIsInitialized = 1;
        return true;
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output)
        throws java.io.IOException {
        if (!getTypeBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 1, type_);
        }
        if (!getReqJsonBytes().isEmpty()) {
            com.google.protobuf.GeneratedMessageV3.writeString(output, 2, reqJson_);
        }
        unknownFields.writeTo(output);
    }

    @Override
    public int getSerializedSize() {
        int size = memoizedSize;
        if (size != -1) {
            return size;
        }
        size = 0;
        if (!getTypeBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(1, type_);
        }
        if (!getReqJsonBytes().isEmpty()) {
            size += com.google.protobuf.GeneratedMessageV3.computeStringSize(2, reqJson_);
        }
        size += unknownFields.getSerializedSize();
        memoizedSize = size;
        return size;
    }

    @Override
    public boolean equals(final java.lang.Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof grpc.Response)) {
            return super.equals(obj);
        }
        grpc.Response other = (grpc.Response) obj;
        if (!getType()
            .equals(other.getType())) {
            return false;
        }
        if (!getReqJson()
            .equals(other.getReqJson())) {
            return false;
        }
        return unknownFields.equals(other.unknownFields);
    }

    @Override
    public int hashCode() {
        if (memoizedHashCode != 0) {
            return memoizedHashCode;
        }
        int hash = 41;
        hash = (19 * hash) + getDescriptor().hashCode();
        hash = (37 * hash) + TYPE_FIELD_NUMBER;
        hash = (53 * hash) + getType().hashCode();
        hash = (37 * hash) + REQJSON_FIELD_NUMBER;
        hash = (53 * hash) + getReqJson().hashCode();
        hash = (29 * hash) + unknownFields.hashCode();
        memoizedHashCode = hash;
        return hash;
    }

    public static grpc.Response parseFrom(java.nio.ByteBuffer data)
            throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }
    public static grpc.Response parseFrom(
        java.nio.ByteBuffer data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }
    public static grpc.Response parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }
    public static grpc.Response parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }
    public static grpc.Response parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }
    public static grpc.Response parseFrom(
        byte[] data, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }
    public static grpc.Response parseFrom(java.io.InputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    public static grpc.Response parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }
    public static grpc.Response parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }
    public static grpc.Response parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }
    public static grpc.Response parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input);
    }
    public static grpc.Response parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
        return com.google.protobuf.GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }
    public static Builder newBuilder(grpc.Response prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }
    @Override
    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    @Override
    protected Builder newBuilderForType(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
        return new Builder(parent);
    }
    /**
     * Protobuf type {@code Response}
     */
    public static final class Builder extends
        com.google.protobuf.GeneratedMessageV3.Builder<Builder> implements
        // @@protoc_insertion_point(builder_implements:Response)
        grpc.ResponseOrBuilder {
        public static com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
            return grpc.Requirement.internal_static_Response_descriptor;
        }

        @Override
        protected com.google.protobuf.GeneratedMessageV3.FieldAccessorTable
        internalGetFieldAccessorTable() {
            return grpc.Requirement.internal_static_Response_fieldAccessorTable
                .ensureFieldAccessorsInitialized(grpc.Response.class, grpc.Response.Builder.class);
        }

        // Construct using grpc.Response.newBuilder()
        private Builder() {
            maybeForceBuilderInitialization();
        }

        private Builder(com.google.protobuf.GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            maybeForceBuilderInitialization();
        }
        private void maybeForceBuilderInitialization() {
        }
        @Override
        public Builder clear() {
            super.clear();
            type_ = "";
            reqJson_ = "";
            return this;
        }

        @Override
        public com.google.protobuf.Descriptors.Descriptor
        getDescriptorForType() {
            return grpc.Requirement.internal_static_Response_descriptor;
        }

        @Override
        public grpc.Response getDefaultInstanceForType() {
            return grpc.Response.getDefaultInstance();
        }

        @Override
        public grpc.Response build() {
            grpc.Response result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException(result);
            }
            return result;
        }

        @Override
        public grpc.Response buildPartial() {
            grpc.Response result = new grpc.Response(this);
            result.type_ = type_;
            result.reqJson_ = reqJson_;
            onBuilt();
            return result;
        }

        @Override
        public Builder clone() {
            return super.clone();
        }
        @Override
        public Builder setField(com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
            return super.setField(field, value);
        }
        @Override
        public Builder clearField(com.google.protobuf.Descriptors.FieldDescriptor field) {
            return super.clearField(field);
        }
        @Override
        public Builder clearOneof(com.google.protobuf.Descriptors.OneofDescriptor oneof) {
            return super.clearOneof(oneof);
        }
        @Override
        public Builder setRepeatedField(com.google.protobuf.Descriptors.FieldDescriptor field, int index, java.lang.Object value) {
            return super.setRepeatedField(field, index, value);
        }
        @Override
        public Builder addRepeatedField(com.google.protobuf.Descriptors.FieldDescriptor field, java.lang.Object value) {
            return super.addRepeatedField(field, value);
        }
        @Override
        public Builder mergeFrom(com.google.protobuf.Message other) {
            if (other instanceof grpc.Response) {
                return mergeFrom((grpc.Response)other);
            } else {
                super.mergeFrom(other);
                return this;
            }
        }

        public Builder mergeFrom(grpc.Response other) {
            if (other == grpc.Response.getDefaultInstance()) {
                return this;
            }
            if (!other.getType().isEmpty()) {
                type_ = other.type_;
                onChanged();
            }
            if (!other.getReqJson().isEmpty()) {
                reqJson_ = other.reqJson_;
                onChanged();
            }
            this.mergeUnknownFields(other.unknownFields);
            onChanged();
            return this;
        }

        @Override
        public boolean isInitialized() {
            return true;
        }

        @Override
        public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry)
            throws java.io.IOException {
            grpc.Response parsedMessage = null;
            try {
                parsedMessage = PARSER.parsePartialFrom(input, extensionRegistry);
            } catch (com.google.protobuf.InvalidProtocolBufferException e) {
                parsedMessage = (grpc.Response) e.getUnfinishedMessage();
                throw e.unwrapIOException();
            } finally {
                if (parsedMessage != null) {
                    mergeFrom(parsedMessage);
                }
            }
            return this;
        }

        private java.lang.Object type_ = "";
        /**
         * <code>string type = 1;</code>
         * @return The type.
         */
        @Override
        public String getType() {
            java.lang.Object ref = type_;
            if (!(ref instanceof String)) {
                com.google.protobuf.ByteString bs =
                    (com.google.protobuf.ByteString) ref;
                String s = bs.toStringUtf8();
                type_ = s;
                return s;
            } else {
                return (String) ref;
            }
        }
        /**
         * <code>string type = 1;</code>
         * @return The bytes for type.
         */
        @Override
        public com.google.protobuf.ByteString getTypeBytes() {
            java.lang.Object ref = type_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b =
                    com.google.protobuf.ByteString.copyFromUtf8(
                        (String) ref);
                type_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }
        /**
         * <code>string type = 1;</code>
         * @param value The type to set.
         * @return This builder for chaining.
         */
        public Builder setType(
            String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            type_ = value;
            onChanged();
            return this;
        }
        /**
         * <code>string type = 1;</code>
         * @return This builder for chaining.
         */
        public Builder clearType() {
            type_ = getDefaultInstance().getType();
            onChanged();
            return this;
        }
        /**
         * <code>string type = 1;</code>
         * @param value The bytes for type to set.
         * @return This builder for chaining.
         */
        public Builder setTypeBytes(
            com.google.protobuf.ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            checkByteStringIsUtf8(value);
            type_ = value;
            onChanged();
            return this;
        }

        private java.lang.Object reqJson_ = "";
        /**
         * <code>string reqJson = 2;</code>
         * @return The reqJson.
         */
        @Override
        public String getReqJson() {
            java.lang.Object ref = reqJson_;
            if (!(ref instanceof String)) {
                com.google.protobuf.ByteString bs =
                    (com.google.protobuf.ByteString) ref;
                String s = bs.toStringUtf8();
                reqJson_ = s;
                return s;
            } else {
                return (String) ref;
            }
        }
        /**
         * <code>string reqJson = 2;</code>
         * @return The bytes for reqJson.
         */
        @Override
        public com.google.protobuf.ByteString
        getReqJsonBytes() {
            java.lang.Object ref = reqJson_;
            if (ref instanceof String) {
                com.google.protobuf.ByteString b =
                    com.google.protobuf.ByteString.copyFromUtf8(
                        (String) ref);
                reqJson_ = b;
                return b;
            } else {
                return (com.google.protobuf.ByteString) ref;
            }
        }
        /**
         * <code>string reqJson = 2;</code>
         * @param value The reqJson to set.
         * @return This builder for chaining.
         */
        public Builder setReqJson(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            reqJson_ = value;
            onChanged();
            return this;
        }
        /**
         * <code>string reqJson = 2;</code>
         * @return This builder for chaining.
         */
        public Builder clearReqJson() {
            reqJson_ = getDefaultInstance().getReqJson();
            onChanged();
            return this;
        }
        /**
         * <code>string reqJson = 2;</code>
         * @param value The bytes for reqJson to set.
         * @return This builder for chaining.
         */
        public Builder setReqJsonBytes(com.google.protobuf.ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            checkByteStringIsUtf8(value);
            reqJson_ = value;
            onChanged();
            return this;
        }
        @Override
        public Builder setUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
            return super.setUnknownFields(unknownFields);
        }

        @Override
        public Builder mergeUnknownFields(final com.google.protobuf.UnknownFieldSet unknownFields) {
            return super.mergeUnknownFields(unknownFields);
        }

        // @@protoc_insertion_point(builder_scope:Response)
    }

    // @@protoc_insertion_point(class_scope:Response)
    private static final grpc.Response DEFAULT_INSTANCE;
    static {
        DEFAULT_INSTANCE = new grpc.Response();
    }

    public static grpc.Response getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    private static final com.google.protobuf.Parser<Response>
        PARSER = new com.google.protobuf.AbstractParser<>() {
        @Override
        public Response parsePartialFrom(
                com.google.protobuf.CodedInputStream input,
                com.google.protobuf.ExtensionRegistryLite extensionRegistry)
                throws com.google.protobuf.InvalidProtocolBufferException {
            return new Response(input, extensionRegistry);
        }
    };

    public static com.google.protobuf.Parser<Response> parser() {
        return PARSER;
    }

    @Override
    public com.google.protobuf.Parser<Response> getParserForType() {
        return PARSER;
    }

    @Override
    public grpc.Response getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }

}

