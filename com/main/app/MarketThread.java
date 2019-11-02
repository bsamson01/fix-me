package com.main.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class MarketThread extends Thread {

    String line = null;
    BufferedReader  is = null;
    PrintWriter os = null;
    Socket socket = null;
    Connection cn = null;

    public MarketThread(Socket s, Connection connection) {
        super(" " + connection.getId());
        this.socket = s;
        this.cn = connection;
        System.out.println(this.getName());
    }

    public void run() {
        try {
            is = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            os = new PrintWriter(socket.getOutputStream());
        }
        catch(IOException e) {
            System.out.println("IO error in server thread");
        }

        try {
            line = is.readLine();
            this.cn.setCommand(line);
            while(line.compareToIgnoreCase("quit") != 0) {
                os.println(line);
                os.flush();
                System.out.println("Response to Client (#"+this.cn.getId()+")  :  " + line);
                line = is.readLine();
            }
        }
        catch (IOException e) {
            line=this.getName();
            System.out.println("IO Error/ Client " + line + " terminated abruptly");
        }
        catch (NullPointerException e) {
            line = this.getName();
            System.out.println("Client " + line + " Closed");
        }

        finally {
            try {
                System.out.println("Connection Closing..");
                if (is != null) {
                    is.close(); 
                    System.out.println(" Socket Input Stream Closed");
                }

                if(os != null) {
                    os.close();
                    System.out.println("Socket Out Closed");
                }
                if (socket != null) {
                    socket.close();
                    System.out.println("Socket Closed");
                }
            }
            catch (IOException ie) {
                System.out.println("Socket Close Error");
            }
        }
    }
}