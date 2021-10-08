package unit.iot;

import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import wappsto.iot.*;
import wappsto.iot.ssl.*;

import java.io.*;
import java.time.*;

import static org.junit.jupiter.api.Assertions.*;

public class InputReaderTest {
    public static final int WAIT_FOR_INPUT = 100;
    public static final int MESSAGE_TIMEOUT = 10;
    private static ByteArrayInputStream input;
    private static ByteArrayOutputStream toInput;
    private static MessageCallbackMock messageCallback;
    private static ErrorCallbackMock errorCallback;
    private InputReader handler;
    private final String ERROR_CALLBACK_NOT_CALLED = "Error callback not called";


    @BeforeEach
    public void resetHandler() {
        toInput = new ByteArrayOutputStream();
        messageCallback = new MessageCallbackMock();
        errorCallback = new ErrorCallbackMock();
    }

    @Nested
    class a_message {

        @Test
        public void starts_with_an_opening_bracket() throws Exception {
            toInput.write(
                "Message does not start with {".getBytes()
            );
            ByteArrayInputStream input = new ByteArrayInputStream(
                toInput.toByteArray()
            );

            handler = new InputReader(
                input,
                messageCallback,
                errorCallback
            );
            handler.start();
            assert callbackWasCalled(errorCallback)
                : ERROR_CALLBACK_NOT_CALLED;
        }

        @Test
        @Ignore
        public void terminates_with_a_closing_bracket() throws Exception {
            toInput.write("{ does not close".getBytes());
            input = new ByteArrayInputStream(toInput.toByteArray());
            handler = new InputReader(
                input,
                messageCallback,
                errorCallback,
                MESSAGE_TIMEOUT
            );

            handler.start();
            assert callbackWasCalled(errorCallback) :
                ERROR_CALLBACK_NOT_CALLED;
        }

        @Test
        public void has_an_equal_number_of_opening_and_closing_brackets()
            throws IOException
        {
            String message = "{data: 'valid message'}";
            toInput.write(message.getBytes());
            input = new ByteArrayInputStream(toInput.toByteArray());

            handler = new InputReader(
                input,
                messageCallback,
                errorCallback,
                MESSAGE_TIMEOUT
            );
            handler.start();

            assert callbackWasCalled(messageCallback);
            assertEquals(message, messageCallback.message);
        }
    }

    private boolean callbackWasCalled(CallbackMock callback) {
        Duration timeout = Duration.ofMillis(WAIT_FOR_INPUT);
        Instant messageSent = Instant.now();
        while (!callback.wasCalled) {
            if (Instant.now().isAfter(messageSent.plus(timeout))) {
                return false;
            }
        }
        return true;
    }

    @AfterEach
    public void stopHandlerThread() throws InterruptedException {
        handler.interrupt();
        handler.join();
    }

    static class MessageCallbackMock extends CallbackMock {

        @Override
        public void call(String message) {
            this.message = message;
            wasCalled = true;
        }
    }

    static class ErrorCallbackMock extends CallbackMock {
        @Override
        public void call(String message) {
            try {
                toInput.close();
                this.message = message;
                wasCalled = true;
            } catch (IOException e) {
                fail("Couldn't close stream");
            }
        }
    }

    static abstract class CallbackMock implements Callback {
        public String message;
        public boolean wasCalled;
    }

}
