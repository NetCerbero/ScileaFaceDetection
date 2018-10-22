package com.universidad.bluenet.opencvagain;

import android.os.AsyncTask;

import java.io.DataOutput;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Created by Pc on 21/10/2018.
 */

public class ClientTcp extends AsyncTask<String,Void,Void>{
    Socket st;
    DataOutput buffer;
    PrintWriter pw;
    @Override
    protected Void doInBackground(String... voids) {
        String msg = voids[0];
        try{
            String IP = "192.168.1.219";
            int PORT = 10369;
            InetAddress inetAddress = InetAddress.getByAddress(IP.getBytes());
            st = new Socket();
            st.connect(new InetSocketAddress(inetAddress,PORT),5000);
            pw = new PrintWriter(st.getOutputStream());
            pw.write(msg);
            pw.flush();
            st.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
