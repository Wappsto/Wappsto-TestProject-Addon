package iot;

import org.junit.jupiter.api.*;
import wappsto.iot.Callback;
import wappsto.iot.ssl.MessageHandler;

import java.io.*;

import static wappsto.iot.exceptions.InvalidMessage.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageHandlerTest {
    public static final int WAIT_FOR_INPUT = 100;
    private static PipedInputStream input;
    private static PipedOutputStream toInput;
    private static CallbackMock messageCallback;
    private static CallbackMock errorCallback;
    private MessageHandler handler;
    private final String ERROR_CALLBACK_NOT_CALLED = "Error callback not called";


    @BeforeEach
    public void resetHandler() throws InterruptedException {
        input = new PipedInputStream();
        toInput = new PipedOutputStream();
        messageCallback = new CallbackMock();
        errorCallback = new CallbackMock();
        handler = new MessageHandler(
            input,
            messageCallback,
            errorCallback
        );
        Thread.sleep(WAIT_FOR_INPUT);
    }

    @Nested
    class a_message {


        @Test
        public void starts_with_an_opening_bracket() throws Exception {
            handler.start();

            writeToInput("Message does not start with {");

            Thread.sleep(WAIT_FOR_INPUT);
            assert errorCallback.wasCalled : ERROR_CALLBACK_NOT_CALLED;
            assertEquals(MISSING_OPENING_BRACKET, errorCallback.message);
        }

        @Test
        public void terminates_with_a_closing_bracket() throws Exception {
            handler.start();

            writeToInput("{ does not close");
            Thread.sleep(WAIT_FOR_INPUT);
            assert errorCallback.wasCalled : ERROR_CALLBACK_NOT_CALLED;
            assertEquals(MISSING_CLOSING_BRACKET, errorCallback.message);

        }

        @Test
        public void has_an_equal_number_of_opening_and_closing_brackets()
            throws InterruptedException
        {
            handler.start();
            String message = "{data: 'valid message'}";
            writeToInput(message);
            Thread.sleep(WAIT_FOR_INPUT);
            assertEquals(message, messageCallback.message);
        }
    }

    private void writeToInput(String request) {
        new Thread(
            () -> {
                try {
                    toInput.connect(input);
                    toInput.write(request.getBytes());
                    toInput.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        ).start();
    }

    @AfterEach
    public void stopHandlerThread() throws InterruptedException {
        handler.interrupt();
        handler.join();
    }

    class CallbackMock implements Callback {
        public String message;
        public boolean wasCalled;

        @Override
        public void call(String message) {
            this.message = message;
            wasCalled = true;
        }
    }
}
