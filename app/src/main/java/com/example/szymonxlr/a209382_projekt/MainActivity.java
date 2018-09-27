package com.example.szymonxlr.a209382_projekt;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    public Button ButtonSend, ButtonDisconnect, ButtonConnect;
    public Button Keyboard, Touchpad;
    public EditText InputText, EditText_IP, EditText_PORT;
    public TextView OutputText;

    private Socket client;
    public PrintWriter printwriter;
    public BufferedReader bufferedReader;

    private static int SERVER_PORT = 8080;
    private static String SERVER_IP = "192.168.1.192";

    private boolean isConnected=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InputText = findViewById(R.id.EditText_IN);
        OutputText = findViewById(R.id.output);
        EditText_IP = findViewById(R.id.EditText_IP);
        EditText_PORT = findViewById(R.id.EditText_PORT);

        ButtonSend = findViewById(R.id.Button_SEND);
        ButtonConnect = findViewById(R.id.connect);
        ButtonDisconnect = findViewById(R.id.disconnect);

        Keyboard = findViewById(R.id.button_Keyboard);
        Touchpad = findViewById(R.id.button_Touchpad);

        ButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected==false) {
                    SERVER_IP = EditText_IP.getText().toString();
                    SERVER_PORT = Integer.parseInt(EditText_PORT.getText().toString());
                    ConnectToServer chat = new ConnectToServer();
                    chat.execute();
                    //if(isConnected==false) OutputText.setText("Connected.");
                }
            }
        });

        ButtonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected == true) {
                    Sender messageSender = new Sender();
                    InputText.setText("EXIT");
                    messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    isConnected=false;
                }
            }
        });

        Keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected==true) {
                    Intent k = new Intent(MainActivity.this, Keyboard.class);
                    startActivity(k);
                }
            }
        });

        Touchpad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isConnected==true) {
                    Intent t = new Intent(MainActivity.this, Touchpad.class);
                    startActivity(t);
                }
            }
        });

    }

    public class ConnectToServer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                client = new Socket(SERVER_IP,SERVER_PORT);
                SocketHandler.setSocket(client);
                if (client != null) {
                    printwriter = new PrintWriter(client.getOutputStream(), true);
                    InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
                    bufferedReader = new BufferedReader(inputStreamReader);
                    isConnected = true;

                } else {
                    OutputText.setText("Server has not bean started on port" + SERVER_PORT);
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
                OutputText.setText("Server has not bean started on port" + SERVER_PORT);
            } catch (IOException e) {
                e.printStackTrace();
                OutputText.setText("Server has not bean started on port" + SERVER_PORT);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            ButtonSend.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    final Sender messageSender = new Sender();
                    messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                }
            });
        }
    }


    public class Sender extends AsyncTask<Void, Void, Void> {

        public String message;
        @Override
        protected Void doInBackground(Void... params) {
            message = InputText.getText().toString();
            printwriter.write(message + "\n");
            printwriter.flush();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            InputText.setText("");
            //OutputText.setText("Client: " + message);
        }
    }
}


