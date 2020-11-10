package com.astutate.selfieapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import java.net.URI;
import java.net.URISyntaxException;
import tech.gusavila92.websocketclient.WebSocketClient;

public class captureActivity extends AppCompatActivity {

    /* global var. for the webSocket client */
    private WebSocketClient webSocketClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);
        createWebSocketClient(Appdata.getEncodedData());

    }

    private void createWebSocketClient(final String code) {

        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.70.204:4500/");
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                webSocketClient.send(code);

                //lets give it at least 2 seconds to process it to avoid spam
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //close the websocket
                webSocketClient.close();

                //captured, sent. start the camera again?
                startActivity(new Intent(getApplication(), MainActivity.class));

            }

            //urghhh must implement these
            @Override
            public void onTextReceived(String s) {
            }
            @Override
            public void onBinaryReceived(byte[] data) {
            }
            @Override
            public void onPingReceived(byte[] data) {
            }
            @Override
            public void onPongReceived(byte[] data) {
            }
            @Override
            public void onException(final Exception e) {

                //running the toast to show any possible errors on the main thread
                runOnUiThread(new   Runnable() {
                    public void run() {
                        Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

            }
            @Override
            public void onCloseReceived() {
            }
        };

        //settings
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

}
