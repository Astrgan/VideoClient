package model;

import com.sun.tracing.dtrace.FunctionAttributes;
import com.sun.tracing.dtrace.FunctionName;
import jdk.nashorn.internal.objects.annotations.Function;
import jdk.nashorn.internal.objects.annotations.SpecializedFunction;

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
//            System.out.println("stopSocket");
            answers.setStop();
            answers.join();
            bos.close();
            out.close();
            inputServer.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class Answers extends Thread {

        private boolean stop = false;
        public void setStop() {
            stop = true;
        }

        @Override
        public void run() {
            try {
                while (!stop) {
                    String str = inputServer.readLine();
                    System.out.println(str.substring(2,str.length()));
                    if(!str.isEmpty() | !str.equals("\n")) callback(str.substring(2,str.length()-2));
                }

            } catch (IOException e) {
                System.err.println("Ошибка при получении сообщения.");
                e.printStackTrace();
            }
        }
    }

}
