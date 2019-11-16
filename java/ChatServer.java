import java.net.*;
import java.io.*;
import java.nio.channels.*;
import java.lang.*;
import java.util.*;
import java.nio.*;

public class ChatServer implements Runnable
{
    private ChatServerThread clients[] = new ChatServerThread[50];
    private ServerSocketChannel server = null;
    private Thread       thread = null;
    private int clientCount = 0;
    private Selector selector;

   public ChatServer()
   {
        int[] ports = {5000, 5001};

       try {
            selector = Selector.open();
       }
       catch(IOException e) {
           e.printStackTrace();
       }

       for (int port : ports) {
            try {
                server = ServerSocketChannel.open();
                server.configureBlocking(false);
                server.socket().bind(new InetSocketAddress(port));
                server.register(selector, SelectionKey.OP_ACCEPT); 
                // System.out.println("Binding to port " + port + ", please wait  ...");
                // server = new ServerSocket(port); 
                // System.out.println("Server started: " + server);
                // start();
            }
            catch(IOException ioe) {
                System.out.println("Can not bind to port " + port + ": " + ioe.getMessage());
            }
            start();
       }
   }
   public void run()
   {  
    //    while (thread != null)
    //     {  try
    //         {  System.out.println("Waiting for a client ..."); 
    //             addThread(server.accept());
    //         }
    //         catch(IOException ioe)
    //         { 
    //             System.out.println("Server accept error: " + ioe);
    //             stop();
    //         }
    //     }
while (thread != null) {
    try {
        System.out.println("Waiting for a client ..."); 
        while (selector.isOpen()) {
            selector.select();
            Set readyKeys = selector.selectedKeys();
            Iterator iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
               SelectionKey key = (SelectionKey) iterator.next();
               if (key.isAcceptable()) {
                  addThread(server.accept());
               }
            }
         }
    }
    catch(IOException e) {
        e.printStackTrace();
    }
}

    
   }
   public void start()  { 
       if (thread == null)
        {  
            thread = new Thread(this); 
            thread.start();
        }
    }

   public void stop()   {
        if (thread != null)
        {
            thread.stop(); 
            thread = null;
        }
    }
   private int findClient(int ID)
   {  for (int i = 0; i < clientCount; i++)
         if (clients[i].getID() == ID)
            return i;
      return -1;
   }
   public synchronized void handle(int ID, int markID, String input)
   {
        if (input.equals(".bye"))
        {
            clients[findClient(ID)].send(".bye");
            remove(ID);
        }
        if (markID != -1) {
                clients[findClient(markID)].send(ID + ": " + input); 
        }
        else {
            if (input.contains("setMarketId")) {
                String[] str = input.split(" ");
                if (str.length == 2) {
                    if (Debug.isInteger(str[1])) {
                        markID = Integer.parseInt(str[1]);
                        if (findClient(markID) != -1) {
                            clients[findClient(ID)].setMarketID(markID);
                        }
                    }
                }
            }
        }       
   }
   public synchronized void remove(int ID)
   {  int pos = findClient(ID);
      if (pos >= 0)
      {  ChatServerThread toTerminate = clients[pos];
         System.out.println("Removing client thread " + ID + " at " + pos);
         if (pos < clientCount-1)
            for (int i = pos+1; i < clientCount; i++)
               clients[i-1] = clients[i];
         clientCount--;
         try
         {  toTerminate.close(); }
         catch(IOException ioe)
         {  System.out.println("Error closing thread: " + ioe); }
         toTerminate.stop(); }
   }
   private void addThread(SocketChannel socket)
   {  if (clientCount < clients.length)
        {
            System.out.println("Client accepted: " + socket);
            clients[clientCount] = new ChatServerThread(this, socket);
            clients[clientCount].start();  
            clientCount++;
        }
      else
         System.out.println("Client refused: maximum " + clients.length + " reached.");
   }
   public static void main(String args[]) {
        ChatServer server = null;
        server = new ChatServer();
    }
}