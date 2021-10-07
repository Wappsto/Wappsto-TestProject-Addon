package unit.iot;

import org.junit.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.*;
import wappsto.iot.rpc.*;
import wappsto.iot.ssl.*;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static wappsto.iot.exceptions.InvalidMessage.*;

public class InputReaderTest {
    public static final int WAIT_FOR_INPUT = 100;
    public static final int MESSAGE_TIMEOUT = 50;
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
            Thread.sleep(WAIT_FOR_INPUT);
            assert errorCallback.wasCalled : ERROR_CALLBACK_NOT_CALLED;
            assertEquals(MISSING_OPENING_BRACKET, errorCallback.message);
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
            Thread.sleep(MESSAGE_TIMEOUT + 100);
            assert errorCallback.wasCalled : ERROR_CALLBACK_NOT_CALLED;
            assertEquals(MISSING_CLOSING_BRACKET, errorCallback.message);

        }

        @Test
        public void has_an_equal_number_of_opening_and_closing_brackets()
            throws InterruptedException, IOException {
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

            Thread.sleep(WAIT_FOR_INPUT);
            assertEquals(message, messageCallback.message);
        }
    }

    @AfterEach
    public void stopHandlerThread() throws InterruptedException {
        handler.interrupt();
        handler.join();
    }

    class MessageCallbackMock implements Callback {
        public String message;
        public boolean wasCalled;

        @Override
        public void call(String message) {
            this.message = message;
            wasCalled = true;
        }
    }

    class ErrorCallbackMock implements Callback {
        public String message;
        public boolean wasCalled;

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
}
