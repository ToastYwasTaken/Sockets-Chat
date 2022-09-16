package pgdp.net;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.http.WebSocket;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class WebServer {

    public static int port = 80;
    public static PinguDatabase pinguDatabase;
    public static HtmlGenerator htmlGenerator;
    public static ServerSocket serverSocket;
    public static ExecutorService executorService = Executors.newCachedThreadPool();
    private static boolean serveronline = false;
    public static Slave slave;

    public static void main(String[] args) throws IOException {
//        HttpRequest httpreq = new HttpRequest("GET /find?sexualOrientation=any&minAge=19&maxAge=45&hobbies=swimming HTTP/1.1");
//        System.out.println("Path: " + httpreq.getPath());
//        System.out.println("Method: " + httpreq.getMethod());
//        System.out.println("Parameters: " + httpreq.getParameters());
//        PinguDatabase base = new PinguDatabase();
//        DatingPingu pingu = new DatingPingu(3, "Pingu D", "gay", 34, Set.of("fighting, skiing, meeting"), "Hi I'm a gay Penguin");
//        System.out.println("Database; add(): " + base.add(pingu));
//        System.out.println("Database; lookupById(): " + base.lookupById(3));
//        SearchRequest searchRequest = new SearchRequest("gay", 12, 100, Set.of("fighting, sucking, nutting"));
//        System.out.println("Database; findMatchesFor(): " + base.findMatchesFor(searchRequest));


//        WebServer webServer = new WebServer(port);
        htmlGenerator = new HtmlGenerator();

        new Thread(() -> {
            try {
                startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }).start();   //startServer()
        readConsoleInput();   //readConsoleInput() // Main Thread
//        executorService.shutdown();
    }


    public static void startServer() throws IOException {
        System.out.println("server");
        serveronline = true;
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new IOException("Something went wrong starting the server");
        }
        while (serveronline) {
            Socket sock = serverSocket.accept();

            executorService.execute(
                () -> {
                    try {
                        readSocket(sock);
                    } catch (IOException e) {
                        System.out.println("error");
                    }
                }
                );
            if (!serveronline) {
                break;
            }
        }
    }

    public static String readConsoleInput() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = null;
        try {
            str = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static void readSocket(Socket sock) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        PrintWriter pr = new PrintWriter(sock.getOutputStream(), true);
        String str = br.readLine();
        System.out.println(str);
        HttpRequest req = new HttpRequest(str);

        if (req.getPath().equals("/")) {
            HttpResponse response = new HttpResponse(HttpStatus.OK, htmlGenerator.generateStartPage());
            pr.write(response.toString());
        }
        if (req.getPath().startsWith("/find")) {
            SearchRequest sreq = new SearchRequest(req.getParameters().get("sexualOrientation"),
                    Integer.parseInt(req.getParameters().get("minAge")),
                    Integer.parseInt(req.getParameters().get("maxAge")),
                    Set.of(req.getParameters().get("hobbies")));
            HttpResponse response = new HttpResponse(HttpStatus.OK, htmlGenerator.generateFindPage(sreq, pinguDatabase.findMatchesFor(sreq)));
        }
        if (req.getPath().startsWith("/user/")) {
//            String[] split = req.getPath().split("/");
//            long splitlong = Long.parseLong(split[2]);
//            DatingPingu pengu = new DatingPingu(splitlong);
//            HttpResponse response = new HttpResponse(HttpStatus.OK, htmlGenerator.generateProfilePage(pengu));
        }
    }
    // TODO
}
