package ro.mpp2025.network.utils;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class AbstractServer {
    private int port;
    private ServerSocket server=null;
    public AbstractServer(int port){
        this.port=port;
    }

    public void start() throws RuntimeException {
        try {
            server=new ServerSocket(port);
            while (true){
                System.out.println("Waiting for clients ...");
                Socket client = server.accept();
                System.out.println("Client connected ...");
                processRequest(client);
            }
        } catch (IOException e) {
            throw new RuntimeException("Starting server error ",e);
        } finally {
            stop();
        }
    }

    protected abstract void processRequest(Socket client);
    public void stop() throws RuntimeException {
        try {
            server.close();
        } catch (IOException e) {
            throw new RuntimeException("Closing server error ", e);
        }
    }
}