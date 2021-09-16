package wappsto.iot.ssl;

import wappsto.iot.Callback;

import java.io.IOException;
import java.io.InputStream;

import static wappsto.iot.exceptions.InvalidMessage.*;

public class MessageHandler extends Thread{
    private Callback messageCallBack;
    private Callback errorCallback;
    private InputStream incomingData;

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
        while (!Thread.interrupted()) {
            read();
        }
    }

    //so basically i hate this code and i need to come back to it
    private void read() {
        int openBraces = 0;
        String msg = "";
        int input;
        try {
            input = incomingData.read();
            char readChar = (char) input;

            while (!inputReceived(input)) {
                readChar = (char) input;

                if (msg.isEmpty() && readChar != '{') {
                    incomingData.readAllBytes();
                    errorCallback.call(MISSING_OPENING_BRACKET);
                    return;
                } else {
                    switch (readChar) {
                        case '{':
                            openBraces++;
                            break;
                        case '}':
                            openBraces--;
                            break;
                        default:
                            break;
                    }
                    msg += readChar;
                    if (openBraces == 0) break;
                }

                input = incomingData.read();
            }

            if (!msg.isEmpty() && readChar != '}') {
                errorCallback.call(MISSING_CLOSING_BRACKET);
                return;
            }

            if (openBraces != 0) {
                errorCallback.call(MISMATCHED_BRACKET);
                return;
            }

            messageCallBack.call(msg);

        } catch (IOException e) {
            errorCallback.call("IO Exception");
        }

    }

    private boolean inputReceived(int input) {
        return input == -1;
    }


}
