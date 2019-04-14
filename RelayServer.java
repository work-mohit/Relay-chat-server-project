
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.lang.Thread;

class clients{
    public static ArrayList<Socket> list = new ArrayList<Socket>();
}

class ChatServer extends Thread{

    public void run(){
        try{
            ServerSocket ss = new ServerSocket(8090);
            ss.setSoTimeout(1000000);

            while(true){
                try{
                    System.out.println("Listening on port:"+ss.getLocalPort());

                    Socket client = ss.accept();
                    clients.list.add(client);
                    System.out.println("Connected to" +client.getRemoteSocketAddress());
                    Thread tChat = new Chat(client);
                    tChat.start();
                }catch (Exception e){
                        break;
                }
            }
        }catch (IOException e){
                e.printStackTrace();
        }
    }

}

class Chat extends Thread{
    private Socket client;
    public Chat(Socket client){
        this.client = client;
    }

    public void run(){
        try{
            while(true){
                DataInputStream in = new DataInputStream(client.getInputStream());
                String mssg = in.readUTF();

                for(Socket cl: clients.list){
                    if(cl.isConnected()){
                        if(cl!=client){
                            DataOutputStream out = new DataOutputStream(client.getOutputStream());
                            out.writeUTF(mssg);
                        }
                        else{
                            cl.close();
                            clients.list.remove(cl);
                        }
                    }
                }
            }
        }catch(Exception e){
            try{
                clients.list.remove(client);
                client.close();
                System.out.println("Client connection Reset.");
            }catch (Exception exp){
                System.out.println("Error closing connection to client");
            }

        }
    }
}


public class RelayServer {
    public static void main(String[] args) {
        Thread tServer = new ChatServer ();
        tServer.start();

    }
}
