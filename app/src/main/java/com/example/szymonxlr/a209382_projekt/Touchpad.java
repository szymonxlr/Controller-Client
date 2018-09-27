package com.example.szymonxlr.a209382_projekt;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Touchpad extends MainActivity implements Button.OnClickListener {


    private Button button;
    TextView mousePad;

    private boolean mouseMoved=false;
    private float initX =0;
    private float initY =0;
    private float disX =0;
    private float disY =0;
    private Socket client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_touchpad);

        mousePad = findViewById(R.id.mousePad);
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

        mousePad.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final Sender messageSender = new Sender();
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            initX =event.getX();
                            initY =event.getY();
                            mouseMoved=false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            disX = event.getX()- initX;
                            disY = event.getY()- initY;
                            initX = event.getX();
                            initY = event.getY();
                            if(disX !=0|| disY !=0){
                                InputText.setText("#2!"+disX +","+ disY); //send mouse movement to server
                                messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                            }
                            mouseMoved=true;
                            break;
                    }
                return true;
            }
        });

    }


    public void onClick(View view) {

        final Sender messageSender = new Sender();
        button = findViewById(view.getId());
        InputText.setText("#2!"+button.getTag());
        messageSender.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

}
