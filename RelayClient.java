


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

class User{
    private static String name;
}
class Listener extends Thread{
    private Socket socket;
    public Listener(Socket s ) {
        socket = s;
    }
    @Override
    public void run(){
         try{
             while(socket.isConnected()) {

                DataInputStream in = new DataInputStream(socket.getInputStream());

                System.out.println("Client:" + in.readUTF());
            }
        }catch (Exception e) {
             e.printStackTrace();
         }finally {
             try{
                 socket.close();
             }catch (Exception e){

             }
         }
    }

}

class Sender extends Thread{
    private Socket socket;
    public Sender(Socket s){
        socket = s;
    }
    @Override
    public void run() {
         try{
             while (socket.isConnected()) {

                DataOutputStream out = new DataOutputStream(socket.getOutputStream());
                Scanner scanner = new Scanner(System.in);
                String mssg = scanner.nextLine();


                out.writeUTF(User.name +": "+mssg);
            }
        }catch(Exception e ){
             e.printStackTrace();
        }finally {
             try{
                 socket.close();
             }catch (Exception e){

             }
         }
    }


}


public class Main {

    public static void main(String[] args) {
        try{
            System.out.println("Enter the IP address to connect:");
            Scanner in = new Scanner(System.in);

            String ipAddress = in.nextLine();

            Socket socket = new Socket(ipAddress,8090);
            System.out.println("Connected to "+socket.getLocalSocketAddress());

            Thread t1 = new Listener(socket);
            Thread t2 = new Sender(socket);

            t1.start();
            t2.start();

        //   socket.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
