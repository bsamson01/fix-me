package com.main.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

public class Market {

    public static void main(String args[]) throws IOException{

        InetAddress address = InetAddress.getLocalHost();
        Socket s1 = null;
        String line = null;
        BufferedReader br = null;
        BufferedReader in = null;
        PrintWriter out = null;

        try {
            s1 = new Socket(address, 5001);
            br = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(s1.getInputStream()));
            out = new PrintWriter(s1.getOutputStream());
        }
        catch (IOException e){
            e.printStackTrace();
            System.err.print("IO Exception");
        }

        System.out.println("Market Address : " + address);
        String response = null;
        try {
            line = br.readLine(); 
            while (line.compareToIgnoreCase("quit") != 0) {
                out.println(line);
                out.flush();
                response = in.readLine();
                System.out.println("Server Response : " + response);
                line = br.readLine();
            }
        }

        catch(IOException e) {
            e.printStackTrace();
            System.out.println("Socket read Error");
        }
        finally{
            in.close();out.close();br.close();s1.close();
            System.out.println("Connection Closed");
        }
    }
}