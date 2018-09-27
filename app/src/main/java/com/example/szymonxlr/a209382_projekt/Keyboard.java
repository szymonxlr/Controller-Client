package com.example.szymonxlr.a209382_projekt;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Keyboard extends MainActivity implements Button.OnClickListener {

    private Button button;
    private Socket client;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard);

        client=SocketHandler.getSocket();
        try {
            printwriter = new PrintWriter(client.getOutputStream(), true);
            InputStreamReader inputStreamReader = new InputStreamReader(client.getInputStream());
            bufferedReader = new BufferedReader(inputStreamReader);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //ConnectToServer chat = new ConnectToServer();
        //chat.execute();
    }


    public void onClick(View view) {

        final Sender messageSender = new Sender();
        button = findViewById(view.getId());
        InputText.setText("#1!"+button.getTag().toString());
        messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


}
