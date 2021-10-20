package wappsto.iot.ssl;

import wappsto.iot.*;

import java.io.*;
import java.time.*;
import java.util.function.*;

import static wappsto.iot.exceptions.InvalidMessage.*;

/**
 * Reads incoming data from the SSL socket and parses them into distinct strings
 */
public class InputReader extends Thread{
    public static final int DEFAULT_MESSAGE_TIMOUT = 500;
    private final Callback messageCallback;
    private final Callback errorCallback;
    private final InputStream incomingData;
    private final int timeout;

    /**
     * Instantiate the reader with the default timeout
     * @param incomingData stream of incoming data from the socket
     * @param messageCallback method to call on a completed message
     * @param errorCallback method to call if an incoming message is malformed
     */
    public InputReader(
        InputStream incomingData,
        Callback messageCallback,
        Callback errorCallback
    ) {
        this(
            incomingData,
            messageCallback,
            errorCallback,
            DEFAULT_MESSAGE_TIMOUT
        );
    }

    /**
     * Instantiate the reader with a defined timeout
     * @param incomingData stream of incoming data from the socket
     * @param messageCallback method to call on a completed message
     * @param errorCallback method to call if an incoming message is malformed
     * @param timeout timeout from receiving the start of a message before the
     *                message is deemed malformed
     */
    public InputReader(
        InputStream incomingData,
        Callback messageCallback,
        Callback errorCallback,
        int timeout
    ) {
        this.incomingData = incomingData;
        this.messageCallback = messageCallback;
        this.errorCallback = errorCallback;
        this.timeout = timeout;
    }

    /**
     * Start the thread
     */
    public void run() {
        try {
            while (!Thread.interrupted()) {
                read();
            }
        } catch (Exception e) {
            return;
        }
    }
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
            messageCallback.call(message);

        } catch (IOException e) {
            throw e;
        }

    }

    private boolean waitFor(MessageCompleted messageCompleted) {
        Duration timeout = Duration.ofMillis(this.timeout);
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
