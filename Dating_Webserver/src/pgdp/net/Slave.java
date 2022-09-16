package pgdp.net;


import java.io.IOException;

public class Slave implements Runnable {

    public int counter;

    public Slave(int counter){
        this.counter = counter;
    }

    @Override
    public void run() {
        System.out.println("Current Thread: " + Thread.currentThread().getName() + " - startcounter: " + counter);

            switch (counter) {
                case 0:
                    try {
                        WebServer.startServer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                case 1:

                case 2:
            }
        System.out.println("Current Thread: " + Thread.currentThread().getName() + " - endcount: " + counter);

    }
}
