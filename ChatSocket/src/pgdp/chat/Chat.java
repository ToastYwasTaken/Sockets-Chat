package pgdp.chat;

import javax.sound.sampled.Port;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.nio.Buffer;
import java.util.Arrays;

public class Chat extends MiniJava {
    public static void main(String[] args) {
        Socket sock = null;
        boolean server = false;
        while (true) {
            String input = readString(
                    "Geben Sie <port> an, um den Chatserver zu starten "
                            + "oder <host>:<port>, um sich mit einem laufenden Server "
                            + "zu verbinden. Geben Sie zum Beenden exit ein\n");
            if (input.equals("exit")) {
                System.out.println("Beenden.");
                return;
            }
            if(!input.contains(":")){        //case: USER
                server = false;
                String ip = input.substring(0, input.indexOf(":"));
                int port = Integer.parseInt(input.substring(input.indexOf(":") +1));
                try {
                    sock = new Socket(InetAddress.getByName(ip), port);
                }   catch (UnknownHostException e) {
                    write("UnknownHostException: Host unknown, try again");
                }   catch (NumberFormatException e){
                    write("NumberFormatException: Port invalid, try again");
                }   catch (IOException e) {
                    write("IOException: In-/Output error, try again");
                }
            }else{                       //case: SERVER
                try {
                    server = true;
                    int port = Integer.parseInt(input.split(":")[1]);
                    ServerSocket serverSocket = null;
                    serverSocket = new ServerSocket(port);
                    sock = serverSocket.accept();
                    serverSocket.close();
                    break;
                }   catch (UnknownHostException e) {
                    write("UnknownHostException: Host unknown, try again");
                }   catch (ConnectException e){
                    write("ConnectException: Connection declined, try again");
                }   catch (NumberFormatException e){
                    write("NumberFormatException: Port invalid, try again");
                }   catch (IOException e) {
                    e.printStackTrace();
                    write("IOException: In-/Output error, try again");
                }
            }

        }
        try (BufferedReader bufferedReader = new BufferedReader(
                new InputStreamReader(sock.getInputStream()));
             PrintWriter out = new PrintWriter(sock.getOutputStream(),
                     true)) {
            boolean running = true;
            if (server) {
                write("Verbindung hergestellt! Sie können nun etwas senden.");
                String input = readString("> ");
                if ("exit".equals(input))
                    running = false;
                else
                    out.println(input);
            }
            while (running) {
                String recieved = bufferedReader.readLine();
                if ("exit".equals(recieved)) {
                    write("exit empfangen.");
                    break;
                }
                write(recieved);
                String input = readString("> ");
                if ("exit".equals(input))
                    running = false;
                out.println(input);
            }
            write("Beenden.");
        } catch (IOException e1) {
            write("Verbindungsfehler, Beenden.");
        } finally {
            try {
                sock.close();
            } catch (IOException e) {
                write("Verbindungsabbau fehlgeschlagen.");
            }
        }
    }
}