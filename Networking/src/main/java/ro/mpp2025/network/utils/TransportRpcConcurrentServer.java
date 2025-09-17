package ro.mpp2025.network.utils;

import ro.mpp2025.network.rpcprotocol.TransportClientRpcReflectionWorker;
import ro.mpp2025.services.IServices;
import java.net.Socket;

public class TransportRpcConcurrentServer extends AbsConcurrentServer {
    private IServices Server;
    public TransportRpcConcurrentServer(int port, IServices transportServer) {
        super(port);
        this.Server = transportServer;
        System.out.println("Transport - TransportRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TransportClientRpcReflectionWorker worker = new TransportClientRpcReflectionWorker(Server, client);

        Thread tw = new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}