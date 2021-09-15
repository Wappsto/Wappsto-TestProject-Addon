package wappsto.iot.ssl;

import wappsto.iot.Callback;

import java.io.IOException;
import java.io.InputStream;

public class IncomingDataHandler extends Thread{
    private Callback callback;
    private InputStream incomingData;

    public IncomingDataHandler(InputStream incomingData) {
        this.incomingData = incomingData;
    }

    public void setCallBack(Callback callback) {
        this.callback = callback;
    }

    public void run() {
        int openBraces = 0;
        String data = "";
        while (true) {
            char readChar = 0;
            try {
                readChar = (char) incomingData.read();
            } catch (IOException e) {
                e.printStackTrace();
            }
            data += readChar;
            if (readChar == '{') {
                openBraces++;
            } else if (readChar == '}') {
                openBraces--;
            }
            if (openBraces == 0) {
                callback.parse(data);
                data = "";
            }
        }
    }

}
