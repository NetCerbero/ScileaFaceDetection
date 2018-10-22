package com.universidad.bluenet.opencvagain;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Pc on 22/10/2018.
 */

public class ClientSocket implements Runnable {
    String IP = "192.168.1.219";
    int PORT = 10369;
    String TAG = "ACTIVITYSOCKET";
    private long startTime = 0l;
    private Socket connectionSocket;
    @Override
    public void run() {
        try{
            Log.i(TAG,"Conectando");
            InetAddress serverAddr = InetAddress.getByName(IP);
            startTime = System.currentTimeMillis();
            //creando una nueva instancia de socket
            connectionSocket = new Socket();
            //comenzando la conexion con el servidor con 5000ms timeout
            //bloqueara el hilo hasta que se establezca la conexion
            connectionSocket.connect(new InetSocketAddress(serverAddr,PORT),5000);
            long time = System.currentTimeMillis() - startTime;
            Log.i(TAG,"conectado! durancion " + time);
        } catch (UnknownHostException e) {
            //e.printStackTrace();
            Log.i(TAG,e.toString());
        } catch (IOException e) {
            //e.printStackTrace();
            Log.i(TAG,e.toString());
        }
    }

    public boolean isConnected(){
        return connectionSocket.isConnected();
    }
    public void sendData(byte[] data){
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
            //send output msg
            out.write(data.toString());
            out.flush();
            Log.i("TcpClient", "sent: " + data[1]);
            //accept server response
            byte rsp[] = in.readLine().getBytes();
            Log.i("TcpClient", "received: " + rsp);
        } catch (IOException e) {
            //e.printStackTrace();
            Log.i(TAG,e.toString());
        }
    }
}
