package com.main.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.lang.*;


public class Router {
    public static void main(String args[]) {

        Socket brSocket = null;
        Socket mkSocket = null;
        ServerSocket brokerSocket = null;
        ServerSocket marketSocket = null;
        System.out.println("Server Listening......");
        try {
            brokerSocket = new ServerSocket(5000);
        }
        catch(IOException e) {
            e.printStackTrace();
            System.out.println("Server error");
        }
        try {
            marketSocket = new ServerSocket(5001);
        }
        catch(IOException e) {
            e.printStackTrace();
            System.out.println("Server error");
        }

        int i = 0;
        int j = 0;
        while(true) {
            try {
                i++;
                brSocket = brokerSocket.accept();
                Connection cn = new Connection(10000 + i);
                System.out.println("Connection Established by Broker(#" + cn.getId() + ")" );
                ServerThread st = new ServerThread(brSocket, cn);
                st.start();
            }
            catch(Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
                break;
            }

            try {
                j++;
                mkSocket = marketSocket.accept();
                Connection cn = new Connection(10000 + j);
                System.out.println("Connection Established by Market(#" + cn.getId() + ")" );
                ServerThread st = new ServerThread(mkSocket, cn);
                st.start();
            }
            catch(Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
                break;
            }
        }
        try {
            marketSocket.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        try {
            brokerSocket.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}