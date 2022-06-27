package net.bncloud.logging.support;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import net.bncloud.logging.LogManager;
import net.bncloud.logging.context.LogContext;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class AbstractStringLogManager implements LogManager {

    public enum LogFormats {
        DEFAULT, JSON
    }

    private LogFormats logFormat = LogFormats.DEFAULT;

    private boolean useSingleLine;

    private String entrySeparator = ",";

    public AbstractStringLogManager setLogFormat(LogFormats logFormat) {
        this.logFormat = logFormat;
        return this;
    }

    public AbstractStringLogManager setUseSingleLine(boolean useSingleLine) {
        this.useSingleLine = useSingleLine;
        return this;
    }

    public AbstractStringLogManager setEntrySeparator(String entrySeparator) {
        this.entrySeparator = entrySeparator;
        return this;
    }

    public String getEntrySeparator() {
        return entrySeparator;
    }

    protected String toString(final LogContext context) {
        if (logFormat == LogFormats.JSON) {
            final StringBuilder builder = new StringBuilder();

            if (this.useSingleLine) {
                builder.append(getJsonObjectForLog(context).toJSONString());
            } else {
                builder.append(JSON.toJSONString(getJsonObjectForLog(context), SerializerFeature.PrettyFormat))
                        .append("\n");
            }
            return builder.toString();
        }
        if (this.useSingleLine) {
            return getSingleLineLogString(context);
        }
        return getMultiLineLogString(context);
    }

    protected String getMultiLineLogString(LogContext context) {
        final StringBuilder builder = new StringBuilder();
        builder.append("Log record BEGIN\n")
                .append("==========================================================================================\n")
                .append("requestId: ").append(context.getRequestId()).append("\n")
                .append("user_id: ").append(context.getPrincipal().getUserId()).append("\n")
                .append("name: ").append(context.getPrincipal().getName()).append("\n")
                .append("resource: ").append(context.getResource()).append("\n")
                .append("module: ").append(context.getModule()).append("\n")
                .append("action: ").append(context.getAction()).append("\n")
                .append("uri: ").append(context.getUri()).append("\n")
                .append("httpMethod: ").append(context.getHttpMethod()).append("\n")
                .append("className: ").append(context.getClassName()).append("\n")
                .append("method: ").append(context.getMethod()).append("\n")
                .append("application: ").append(context.getApplication()).append("\n")
                .append("clientIp: ").append(context.getClientIp()).append("\n")
                .append("serverIp: ").append(context.getServerIp()).append("\n")
                .append("requestAt: ").append(context.getRequestAt().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n")
                .append("responseAt: ").append(context.getResponseAt().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))).append("\n")
                .append("duration: ").append(context.getDuration().toMillis()).append(" 毫秒\n")
                .append("==========================================================================================\n")
                .append("\n");
        return builder.toString();
    }

    protected String getSingleLineLogString(LogContext context) {
        final StringBuilder builder = new StringBuilder();
        builder.append(context.getRequestId()).append(getEntrySeparator())
                .append(context.getPrincipal().getUserId()).append(getEntrySeparator())
                .append(context.getPrincipal().getName()).append(getEntrySeparator())
                .append(context.getResource()).append(getEntrySeparator())
                .append(context.getModule()).append(getEntrySeparator())
                .append(context.getAction()).append(getEntrySeparator())
                .append(context.getUri()).append(getEntrySeparator())
                .append(context.getHttpMethod()).append(getEntrySeparator())
                .append(context.getClassName()).append(getEntrySeparator())
                .append(context.getMethod()).append(getEntrySeparator())
                .append(context.getApplication()).append(getEntrySeparator())
                .append(context.getClientIp()).append(getEntrySeparator())
                .append(context.getServerIp()).append(getEntrySeparator())
                .append(context.getRequestAt()).append(getEntrySeparator())
                .append(context.getResponseAt()).append(getEntrySeparator())
                .append(context.getPrincipal()).append(getEntrySeparator())
                .append(context.getDuration().toMillis()).append(getEntrySeparator());
        return builder.toString();
    }


    protected JSONObject getJsonObjectForLog(final LogContext context) {
        final JSONObject json = new JSONObject();
        json.put("requestId", context.getRequestId());
        json.put("user_id", context.getPrincipal().getUserId());
        json.put("name", context.getPrincipal().getName());
        json.put("resource", context.getResource());
        json.put("module", context.getModule());
        json.put("action", context.getAction());
        json.put("uri", context.getUri());
        json.put("httpMethod", context.getHttpMethod());
        json.put("className", context.getClassName());
        json.put("method", context.getMethod());
        json.put("application", context.getApplication());
        json.put("clientIp", context.getClientIp());
        json.put("serverIp", context.getServerIp());
        json.put("requestAt", context.getRequestAt().toString());
        json.put("responseAt", context.getResponseAt().toString());
        json.put("duration", context.getDuration().toMillis());
        return json;
    }
}
