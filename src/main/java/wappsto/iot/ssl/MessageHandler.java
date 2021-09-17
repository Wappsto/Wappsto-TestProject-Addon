package wappsto.iot.ssl;

import wappsto.iot.Callback;

import java.io.*;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Consumer;

import static wappsto.iot.exceptions.InvalidMessage.*;

public class MessageHandler extends Thread{
    public static final int MESSAGE_TIMOUT = 500;
    private final Callback messageCallBack;
    private final Callback errorCallback;
    private final InputStream incomingData;

    public MessageHandler(
        InputStream incomingData,
        Callback messageCallback,
        Callback errorCallback
    ) {
        this.incomingData = incomingData;
        this.messageCallBack = messageCallback;
        this.errorCallback = errorCallback;
    }


    public void run() {
        try {
            while (!Thread.interrupted()) {
                read();
            }
        } catch (Exception e) {
            return;
        }
    }

    //so basically i hate this code and i need to come back to it
    private void read() throws Exception {
        int openBraces = 0;
        String message = "";

        MessageCompleted messageCompleted = callback();

        try {
            char readChar = (char) incomingData.read();;
            if (readChar != '{') {
                errorCallback.call(MISSING_OPENING_BRACKET);
                throw new Exception("IO Exception");
            }
            openBraces++;
            message += readChar;

            new MessageBuilder(
                incomingData,
                message,
                openBraces,
                messageCompleted
            ).start();

            if(!waitFor(messageCompleted)) {
                errorCallback.call(MISSING_CLOSING_BRACKET);
                throw new Exception("IO Exception");
            }

            message = messageCompleted.message();
            messageCallBack.call(message);

        } catch (IOException e) {
            throw e;
        }

    }

    private boolean waitFor(MessageCompleted messageCompleted) {
        Duration timeout = Duration.ofMillis(MESSAGE_TIMOUT);
        Instant messageStarted = Instant.now();
        while (!messageCompleted.completed()) {
            if (Instant.now().isAfter(messageStarted.plus(timeout))) {
                errorCallback.call("Timout: Message never completed");
                return false;
            }
        }
        return true;
    }

    private MessageCompleted callback() {
        return new MessageCompleted() {
            private boolean completed = false;
            private String message;
            @Override
            public void accept(String message) {
                this.message = message;
                completed = true;
            }
            public boolean completed() {
                return completed;
            }

            public String message() {
                return message;
            }
        };
    }

    private interface MessageCompleted extends Consumer<String> {
        String message();
        boolean completed();
    }

    private static class MessageBuilder extends Thread {
        private final InputStream stream;
        private String message;
        private int openBraces;
        private final Consumer<String> messageCompleted;

        public MessageBuilder(
            InputStream stream,
            String message,
            int openBraces,
            Consumer<String> messageCompleted
        ) {
            this.stream = stream;
            this.message = message;
            this.openBraces = openBraces;
            this.messageCompleted = messageCompleted;
        }

        public void run() {
            try {
                while (!Thread.interrupted()) {
                    char read = (char)stream.read();
                    if (read == '{') openBraces++;
                    else if (read == '}') openBraces--;
                    message += read;
                    if (openBraces == 0) {
                        messageCompleted.accept(message);
                        break;
                    }
                }
            } catch (IOException e) {
                return;
            }
        }
    }
}
