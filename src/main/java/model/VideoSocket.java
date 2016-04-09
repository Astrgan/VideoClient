package model;


import java.io.*;
import java.net.Socket;

/**
 * Created by vasilev_av on 21.02.2016.
 */
public abstract class VideoSocket{

    Socket socket = null;
    BufferedReader inputServer;
    DataOutputStream out;
    Answers answers;
    BufferedOutputStream bos;
    //2 -push
    //1-get

    abstract public void callback(String path);

    public VideoSocket(String ip, int port) throws IOException  {

        System.out.println("Welcome to Client side");
        System.out.println("Connecting to... "+ip);

        socket = new Socket(ip,port);
        inputServer = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-16"));
        
        bos = new BufferedOutputStream(socket.getOutputStream());
        out = new DataOutputStream(bos);

        answers = new Answers();
        answers.start();

    }

    public void pushPath(String message){

        try {
            message += "\n";
            out.writeInt(message.length()+1);
            out.writeChar('2');
            out.writeChars(message);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void getPaths(){

        try {
            out.writeInt(1);
            out.writeChar('1');
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void stop(){

        try {
            System.out.println("stopSocket");
            answers.interrupt();
            answers.join();
            System.out.println("stopSocket LAST join");
            bos.close();
            out.close();
            inputServer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Answers extends Thread {

        @Override
        public void run() {

            while (!this.isInterrupted()) try {
                String str = inputServer.readLine();
                System.out.println("***********************"+str);
                if (!str.isEmpty() && !str.equals("\n")) callback(str.substring(2, str.length()).trim());

            } catch (Exception e) {
                System.out.println("Answers: " + e);
                break;
            }

        }
    }

}
