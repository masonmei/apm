package com.baidu.oped.apm.config.log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;

/**
 * class WebDebugAppender
 *
 * @author meidongxu@baidu.com
 */

public class WebDebugAppender extends AppenderBase<ILoggingEvent> {

    static PatternLayoutEncoder encoder;

    static Map<String, PatternLayoutEncoder> userEncoderMap = new ConcurrentHashMap<>();

    public static void addWebLog(String userName, OutputStream stream) throws IOException {
        PatternLayoutEncoder userEncoder = new PatternLayoutEncoder();
        userEncoder.setContext(encoder.getContext());
        userEncoder.setCharset(encoder.getCharset());
        userEncoder.setPattern(encoder.getPattern());
        userEncoder.setImmediateFlush(true);
        try {
            userEncoder.init(stream);
            userEncoder.start();
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        userEncoderMap.put(userName, userEncoder);
    }

    @Override
    public void start() {
        if (this.encoder == null) {
            addError(String.format("No encoder set for the appender named [%s].", name));
            return;
        }

        super.start();
    }

    public void append(ILoggingEvent event) {
        if (event.getLevel().levelInt < Level.INFO_INT) {
            return;
        }

        String currentUser = event.getMDCPropertyMap().get("currentUser");
        if (currentUser == null) {
            return;
        }

        PatternLayoutEncoder encoder = userEncoderMap.get(currentUser);
        if (encoder == null) {
            return;
        }

        try {
            encoder.doEncode(event);
        } catch (IOException e) {
            encoder.stop();
            userEncoderMap.remove(currentUser);
        }
    }

    public PatternLayoutEncoder getEncoder() {
        return encoder;
    }

    public void setEncoder(PatternLayoutEncoder encoder) {
        this.encoder = encoder;
    }
}