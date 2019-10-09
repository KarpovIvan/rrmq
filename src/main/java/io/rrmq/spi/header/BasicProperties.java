package io.rrmq.spi.header;

import io.netty.buffer.ByteBuf;
import io.rrmq.spi.method.BaseFrame;
import io.rrmq.spi.utils.AmqpBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static io.rrmq.spi.method.AmqpWriteUtils.*;

public class BasicProperties extends BaseFrame {
    private long bodySize;
    private String contentType;
    private String contentEncoding;
    private Map<String,Object> headers;
    private Integer deliveryMode;
    private Integer priority;
    private String correlationId;
    private String replyTo;
    private String expiration;
    private String messageId;
    private Date timestamp;
    private String type;
    private String userId;
    private String appId;
    private String clusterId;

    private BasicProperties(BasicPropertiesBuilder<?> builder) {
        super(builder.getType(), builder.getChannel());
        this.bodySize = builder.bodySize;
        this.contentType = builder.contentType;
        this.contentEncoding = builder.contentEncoding;
        this.headers = builder.headers;
        this.deliveryMode = builder.deliveryMode;
        this.priority = builder.priority;
        this.correlationId = builder.correlationId;
        this.replyTo = builder.replyTo;
        this.expiration = builder.expiration;
        this.messageId = builder.messageId;
        this.timestamp = builder.timestamp;
        this.type = builder.type;
        this.userId = builder.userId;
        this.appId = builder.appId;
        this.clusterId = builder.clusterId;
    }

    public String getContentType() {
        return contentType;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public Integer getDeliveryMode() {
        return deliveryMode;
    }

    public Integer getPriority() {
        return priority;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public String getExpiration() {
        return expiration;
    }

    public String getMessageId() {
        return messageId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public short getProtocolClassId() {
        return 60;
    }

    @Override
    public short getProtocolMethodId() {
        return 0;
    }

    public String getBasicType() {
        return type;
    }

    public String getUserId() {
        return userId;
    }

    public String getAppId() {
        return appId;
    }

    public String getClusterId() {
        return clusterId;
    }

    @Override
    public void writeValues(ByteBuf out, AtomicInteger counter) {
        writeShort(getProtocolClassId(), out, counter);
        writeShort((short) 0, out, counter);
        writeLonglong(bodySize, out, counter);
        writePresences(
                out,
                counter,
                this.contentType != null,
                this.contentEncoding != null,
                this.headers != null,
                this.deliveryMode != null,
                this.priority != null,
                this.correlationId != null,
                this.replyTo != null,
                this.expiration != null,
                this.messageId != null,
                this.timestamp != null,
                this.type != null,
                this.userId != null,
                this.appId != null,
                this.clusterId != null
        );

        if (this.contentType != null) writeShortstr(this.contentType, out, counter);
        if (this.contentEncoding != null) writeShortstr(this.contentEncoding, out, counter);
        if (this.headers != null) writeTable(this.headers, out, counter);
        if (this.deliveryMode != null) writeOctet(this.deliveryMode, out, counter);
        if (this.priority != null) writeOctet(this.priority, out, counter);
        if (this.correlationId != null) writeShortstr(this.correlationId, out, counter);
        if (this.replyTo != null) writeShortstr(this.replyTo, out, counter);
        if (this.expiration != null) writeShortstr(this.expiration, out, counter);
        if (this.messageId != null) writeShortstr(this.messageId, out, counter);
        if (this.timestamp != null) writeTimestamp(this.timestamp, out, counter);
        if (this.type != null) writeShortstr(this.type, out, counter);
        if (this.userId != null) writeShortstr(this.userId, out, counter);
        if (this.appId != null) writeShortstr(this.appId, out, counter);
        if (this.clusterId != null) writeShortstr(this.clusterId, out, counter);

    }

    @Override
    public String toString() {
        return "BasicProperties{" +
                "bodySize=" + bodySize +
                ", contentType='" + contentType + '\'' +
                ", contentEncoding='" + contentEncoding + '\'' +
                ", headers=" + headers +
                ", deliveryMode=" + deliveryMode +
                ", priority=" + priority +
                ", correlationId='" + correlationId + '\'' +
                ", replyTo='" + replyTo + '\'' +
                ", expiration='" + expiration + '\'' +
                ", messageId='" + messageId + '\'' +
                ", timestamp=" + timestamp +
                ", type='" + type + '\'' +
                ", userId='" + userId + '\'' +
                ", appId='" + appId + '\'' +
                ", clusterId='" + clusterId + '\'' +
                "} " + super.toString();
    }

    public static BasicPropertiesBuilder<?> builder() {
        return new BasicPropertiesBuilder<>();
    }

    public static class BasicPropertiesBuilder<T extends BasicPropertiesBuilder<T>> extends AmqpBuilder<T, BasicProperties> {

        private long bodySize;
        private String contentType;
        private String contentEncoding;
        private Map<String,Object> headers;
        private Integer deliveryMode;
        private Integer priority;
        private String correlationId;
        private String replyTo;
        private String expiration;
        private String messageId;
        private Date timestamp;
        private String type;
        private String userId;
        private String appId;
        private String clusterId;

        public T setBodySize(long bodySize) {
            this.bodySize = bodySize;
            return self();
        }

        public T setContentType(String contentType) {
            this.contentType = contentType;
            return self();
        }

        public T setContentEncoding(String contentEncoding) {
            this.contentEncoding = contentEncoding;
            return self();
        }

        public T setHeaders(Map<String, Object> headers) {
            this.headers = headers;
            return self();
        }

        public T addHeader(String key, Object value) {
            if (headers ==null) {
                headers = new HashMap<>();
            }
            headers.put(key, value);
            return self();
        }

        public T setDeliveryMode(Integer deliveryMode) {
            this.deliveryMode = deliveryMode;
            return self();
        }

        public T setPriority(Integer priority) {
            this.priority = priority;
            return self();
        }

        public T setCorrelationId(String correlationId) {
            this.correlationId = correlationId;
            return self();
        }

        public T setReplyTo(String replyTo) {
            this.replyTo = replyTo;
            return self();
        }

        public T setExpiration(String expiration) {
            this.expiration = expiration;
            return self();
        }

        public T setMessageId(String messageId) {
            this.messageId = messageId;
            return self();
        }

        public T setTimestamp(Date timestamp) {
            this.timestamp = timestamp;
            return self();
        }

        public T setType(String type) {
            this.type = type;
            return self();
        }

        public T setUserId(String userId) {
            this.userId = userId;
            return self();
        }

        public T setAppId(String appId) {
            this.appId = appId;
            return self();
        }

        public T setClusterId(String clusterId) {
            this.clusterId = clusterId;
            return self();
        }

        @Override
        public BasicProperties build() {
            return new BasicProperties(self());
        }
    }
}
