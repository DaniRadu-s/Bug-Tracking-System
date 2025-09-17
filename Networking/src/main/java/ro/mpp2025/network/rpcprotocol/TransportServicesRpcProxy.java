package ro.mpp2025.network.rpcprotocol;

import ro.mpp2025.domain.Admin;
import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.services.IObserver;
import ro.mpp2025.services.IServices;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TransportServicesRpcProxy implements IServices {
    private String host;
    private int port;

    private  List<IObserver> clients;
    private IObserver client;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;
    public TransportServicesRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses=new LinkedBlockingQueue<Response>();
        this.clients = new ArrayList<>();
    }

    public void loginTester(Tester tester, IObserver client) throws RuntimeException {
        this.initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN_TESTER).data(tester).build();
        this.sendRequest(req);
        Response response=this.readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            this.closeConnection();
            throw new RuntimeException(err);
        }
        else{
            this.client = client;
            this.clients.add(client);
        }
    }

    public void loginAdmin(Admin admin, IObserver client) throws RuntimeException {
        this.initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN_ADMIN).data(admin).build();
        this.sendRequest(req);
        Response response=this.readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            this.closeConnection();
            throw new RuntimeException(err);
        }
        else{
            this.client = client;
            this.clients.add(client);
        }
    }

    public void loginProgrammer(Programmer programmer, IObserver client) throws RuntimeException {
        this.initializeConnection();
        Request req = new Request.Builder().type(RequestType.LOGIN_PROGRAMMER).data(programmer).build();
        this.sendRequest(req);
        Response response=this.readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            this.closeConnection();
            throw new RuntimeException(err);
        }
        else{
            this.client = client;
            this.clients.add(client);
        }
    }

    public void logoutTester(Tester tester, IObserver client) throws RuntimeException {
        Request req = new Request.Builder().type(RequestType.LOGOUT_TESTER).data(tester).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new RuntimeException(err);
        }
    }

    public void logoutAdmin(Admin admin, IObserver client) throws RuntimeException {
        Request req = new Request.Builder().type(RequestType.LOGOUT_ADMIN).data(admin).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new RuntimeException(err);
        }
    }

    public void logoutProgrammer(Programmer programmer, IObserver client) throws RuntimeException {
        Request req = new Request.Builder().type(RequestType.LOGOUT_PROGRAMMER).data(programmer).build();
        sendRequest(req);
        Response response=readResponse();
        if (response.type()== ResponseType.ERROR){
            String err=response.data().toString();
            closeConnection();
            throw new RuntimeException(err);
        }
    }

    @Override
    public Iterable<Programmer> getAllProgrammers() {
        Request req = new Request.Builder().type(RequestType.GET_PROGRAMMERS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            throw new RuntimeException(err);
        }
        System.out.println("Response data: " + response.data().getClass());
        return (Iterable<Programmer>) response.data();
    }
    @Override
    public Iterable<Tester> getAllTesters() {
        Request req = new Request.Builder().type(RequestType.GET_TESTERS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            throw new RuntimeException(err);
        }
        System.out.println("Response data: " + response.data().getClass());
        return (Iterable<Tester>) response.data();
    }
    @Override
    public Iterable<Bug> getAllBugs() {
        Request req = new Request.Builder().type(RequestType.GET_BUGS).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            throw new RuntimeException(err);
        }
        System.out.println("Response data: " + response.data().getClass());
        return (Iterable<Bug>) response.data();
    }


    @Override
    public Tester addTester(Tester tester) {
        Request req = new Request.Builder().type(RequestType.ADD_TESTER).data(tester).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            throw new RuntimeException(err);
        }
        return (Tester) response.data();
    }

    @Override
    public Programmer addProgrammer(Programmer programmer) {
        Request req = new Request.Builder().type(RequestType.ADD_PROGRAMMER).data(programmer).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            throw new RuntimeException(err);
        }
        return (Programmer) response.data();
    }

    @Override
    public Bug addBug(String name, String description, Tester tester, Programmer programmer, LocalDateTime date) {
        Bug bug = new Bug(name,description,tester,programmer,date);
        Request req = new Request.Builder().type(RequestType.ADD_BUG).data(bug).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR){
            String err=response.data().toString();
            throw new RuntimeException(err);
        }
        return (Bug) response.data();
    }

    @Override
    public Tester updateTester(Tester tester) {
        Request req = new Request.Builder()
                .type(RequestType.UPDATE_TESTER)
                .data(tester)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }
        return (Tester) response.data();
    }

    @Override
    public Programmer updateProgrammer(Programmer programmer) {
        Request req = new Request.Builder()
                .type(RequestType.UPDATE_PROGRAMMER)
                .data(programmer)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }
        return (Programmer) response.data();
    }

    @Override
    public Bug updateBug(Bug bug) {
        Request req = new Request.Builder()
                .type(RequestType.UPDATE_BUG)
                .data(bug)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }
        return (Bug) response.data();
    }

    @Override
    public Tester deleteTester(Long id) {
        Request req = new Request.Builder()
                .type(RequestType.DELETE_TESTER)
                .data(id)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }
        return (Tester) response.data();
    }
    @Override
    public Programmer deleteProgrammer(Long id) {
        Request req = new Request.Builder()
                .type(RequestType.DELETE_PROGRAMMER)
                .data(id)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }
        return (Programmer) response.data();
    }
    @Override
    public Bug deleteBug(Long id) {
        Request req = new Request.Builder()
                .type(RequestType.DELETE_BUG)
                .data(id)
                .build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new RuntimeException(err);
        }
        return (Bug) response.data();
    }


    @Override
    public Tester findTesterByEmail(String email) {
        Request req = new Request.Builder()
                .type(RequestType.FIND_BY_EMAIL_TESTER)
                .data(email)
                .build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data() != null ? response.data().toString() : "Unknown error";
            throw new RuntimeException(err);
        }


        if (response.data() == null) {
            return null;
        }

        return (Tester) response.data();
    }

    @Override
    public Programmer findProgrammerByEmail(String email) {
        Request req = new Request.Builder()
                .type(RequestType.FIND_BY_EMAIL_PROGRAMMER)
                .data(email)
                .build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data() != null ? response.data().toString() : "Unknown error";
            throw new RuntimeException(err);
        }

        if (response.data() == null) {
            return null;
        }

        return (Programmer) response.data();
    }

    @Override
    public Admin findAdminByEmail(String email) {
        Request req = new Request.Builder()
                .type(RequestType.FIND_BY_EMAIL_ADMIN)
                .data(email)
                .build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data() != null ? response.data().toString() : "Unknown error";
            throw new RuntimeException(err);
        }

        if (response.data() == null) {
            return null;
        }

        return (Admin) response.data();
    }

    @Override
    public Bug searchBugByName(String name) {
        Request req = new Request.Builder()
                .type(RequestType.FIND_BY_NAME_BUG)
                .data(name)
                .build();
        sendRequest(req);
        Response response = readResponse();

        if (response.type() == ResponseType.ERROR) {
            String err = response.data() != null ? response.data().toString() : "Unknown error";
            throw new RuntimeException(err);
        }

        if (response.data() == null) {
            return null; // No participant found
        }

        return (Bug) response.data();
    }

    private void closeConnection() {
        finished=true;
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (connection != null) connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest(Request request) throws RuntimeException {
        if (output == null) {
            initializeConnection();
        }
        try {
            System.out.println("[CLIENT] Sending request: " + request.type());
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("Error sending object: " + e.getMessage(), e);
        }
    }


    private Response readResponse() throws RuntimeException {
        Response response=null;
        try{
            response=qresponses.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws RuntimeException {
        if (connection == null || connection.isClosed()) {
            try {
                System.out.println("Connecting to server on port " + port + " and host " + host);
                connection = new Socket(host, port);
                output = new ObjectOutputStream(connection.getOutputStream());
                output.flush();
                input = new ObjectInputStream(connection.getInputStream());
                finished = false;
                startReader();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Error connecting to server: " + e.getMessage());
            }
        }
    }

    private void startReader(){
        Thread tw=new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdate(Response response){
        if (response.type() == ResponseType.UPDATED_BUG) {
            System.out.println("S-a adaugat ceva");

            Bug bug = (Bug) response.data();
            try {
                for(IObserver c:this.clients) {
                    c.updatedBug(bug);
                }
            } catch (RuntimeException exc) {
                System.out.println("Error " + exc);
            }
        }
        if (response.type() == ResponseType.REPORTED_BUG) {
            System.out.println("S-a adaugat ceva");

            Bug bug = (Bug) response.data();
            try {
                for(IObserver c:this.clients) {
                    c.addedBug(bug);
                }
            } catch (RuntimeException exc) {
                System.out.println("Error " + exc);
            }
        }
        if (response.type() == ResponseType.DELETED_BUG) {
            System.out.println("S-a adaugat ceva");

            Bug bug = (Bug) response.data();
            try {
                for(IObserver c:this.clients) {
                    c.deletedBug(bug);
                }
            } catch (RuntimeException exc) {
                System.out.println("Error " + exc);
            }
        }
    }

    private boolean isUpdate(Response response) {
        return response.type() == ResponseType.UPDATED_BUG || response.type() == ResponseType.REPORTED_BUG || response.type() == ResponseType.DELETED_BUG;
    }

    private class ReaderThread implements Runnable{
        private ReaderThread() {
        }
        public void run() {
            while(!TransportServicesRpcProxy.this.finished) {
                try {
                    Object response = TransportServicesRpcProxy.this.input.readObject();
                    System.out.println("response received " + response);
                    if (TransportServicesRpcProxy.this.isUpdate((Response)response)) {
                        TransportServicesRpcProxy.this.handleUpdate((Response)response);
                    } else {
                        try {
                            TransportServicesRpcProxy.this.qresponses.put((Response)response);
                        } catch (InterruptedException var3) {
                            InterruptedException exx = var3;
                            exx.printStackTrace();
                        }
                    }
                } catch (IOException | ClassNotFoundException e) {
                    System.out.println("Reader thread error: " + e.getMessage());
                    break;
                }
            }

        }
    }
}
