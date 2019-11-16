import java.net.*;
import java.nio.channels.*;
import java.io.*;
import java.nio.*;

public class ChatServerThread extends Thread
{  private ChatServer       server    = null;
   private SocketChannel    socket    = null;
   private int              ID        = -1;
   private DataInputStream  streamIn  =  null;
   private DataOutputStream streamOut = null;
   private int              marketID = -1;
   private String           clientType = "";

   public ChatServerThread(ChatServer _server, SocketChannel _socket)
   {  super();
      server = _server;
      socket = _socket;
      try {
        InetSocketAddress address = (InetSocketAddress)socket.getRemoteAddress();
        ID     = address.getPort();
      }
      catch(IOException e) {
          e.printStackTrace();
      }
   }
   public void send(String msg)
   {   try
       {  streamOut.writeUTF(msg);
          streamOut.flush();
       }
       catch(IOException ioe)
       {  System.out.println(ID + " ERROR sending: " + ioe.getMessage());
          server.remove(ID);
          stop();
       }
   }
   public int getID()
   { 
       return ID;
   }

   public void setMarketID(int markID) {
       marketID = markID;
   }

   public String getClientType() {
       return clientType;
   }

   public void run()
   {
       System.out.println("Server Thread " + ID + " running.");
        while (true)
        { 
            ByteBuffer buf = ByteBuffer.allocate(100000);
            ReadableByteChannel channel = Channels.newChannel(System.in);
            try {
                while (channel.read(buf) >= 0) {
                    String result = new String(buf.array()).trim();
                    server.handle(ID, marketID, result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
   }

//    public void open() throws IOException
//    {  streamIn = socket;
//       streamOut = \
//    }
   public void close() throws IOException
   {  if (socket != null)
        socket.close();
   }
}