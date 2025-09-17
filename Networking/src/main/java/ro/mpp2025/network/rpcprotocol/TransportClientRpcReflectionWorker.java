package ro.mpp2025.network.rpcprotocol;

import ro.mpp2025.domain.Admin;
import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.services.IObserver;
import ro.mpp2025.services.IServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TransportClientRpcReflectionWorker implements Runnable, IObserver {
    private IServices server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public TransportClientRpcReflectionWorker(IServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request) {
        Response response = null;
        RuntimeException e;
        if (request.type() == RequestType.LOGIN_TESTER) {
            System.out.println("Login request ..." + request.type());
            Tester user = (Tester) request.data();
            try {
                server.loginTester(user,this);
                return okResponse;
            } catch (RuntimeException var7) {
                e = var7;
                this.connected = false;
                return (new Response.Builder()).type(ResponseType.ERROR).data(e.getMessage()).build();
            }

        } if (request.type() == RequestType.LOGIN_ADMIN) {
            System.out.println("Login request ..." + request.type());
            Admin user = (Admin) request.data();
            try {
                server.loginAdmin(user,this);
                return okResponse;
            } catch (RuntimeException var7) {
                e = var7;
                this.connected = false;
                return (new Response.Builder()).type(ResponseType.ERROR).data(e.getMessage()).build();
            }

        } else if (request.type() == RequestType.LOGIN_PROGRAMMER) {
            System.out.println("Login request ..." + request.type());
            Programmer user = (Programmer) request.data();
            try {
                server.loginProgrammer(user,this);
                return okResponse;
            } catch (RuntimeException var7) {
                e = var7;
                this.connected = false;
                return (new Response.Builder()).type(ResponseType.ERROR).data(e.getMessage()).build();
            }

        } else if (request.type() == RequestType.LOGOUT_TESTER) {
            System.out.println("Login request ..." + request.type());
            Tester user = (Tester) request.data();

            try {
                this.server.logoutTester(user,this);
                return okResponse;
            } catch (RuntimeException var7) {
                e = var7;
                this.connected = false;
                return (new Response.Builder()).type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }else if (request.type() == RequestType.LOGOUT_ADMIN) {
            System.out.println("Login request ..." + request.type());
            Admin user = (Admin) request.data();

            try {
                this.server.logoutAdmin(user,this);
                return okResponse;
            } catch (RuntimeException var7) {
                e = var7;
                this.connected = false;
                return (new Response.Builder()).type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }else if (request.type() == RequestType.LOGOUT_PROGRAMMER) {
            System.out.println("Login request ..." + request.type());
            Programmer user = (Programmer) request.data();

            try {
                this.server.logoutProgrammer(user,this);
                return okResponse;
            } catch (RuntimeException var7) {
                e = var7;
                this.connected = false;
                return (new Response.Builder()).type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        } else if (request.type() == RequestType.GET_PROGRAMMERS) {
            System.out.println("Handling GET_PROGRAMMERS request");
            try {
                Iterable<Programmer> programmers = server.getAllProgrammers();
                System.out.println("Found programmers: " + programmers);
                return new Response.Builder()
                        .type(ResponseType.GET_PROGRAMMERS)
                        .data(programmers)
                        .build();
            } catch (Exception e1) {
                System.err.println("Error in GET_PROGRAMMERS: " + e1.getMessage());
                return new Response.Builder()
                        .type(ResponseType.ERROR)
                        .data(e1.getMessage())
                        .build();
            }
        }else if (request.type() == RequestType.GET_TESTERS) {
            System.out.println("Handling GET_TESTERS request");
            try {
                Iterable<Tester> testers = server.getAllTesters();
                System.out.println("Found testers: " + testers);
                return new Response.Builder()
                        .type(ResponseType.GET_TESTERS)
                        .data(testers)
                        .build();
            } catch (Exception e1) {
                System.err.println("Error in GET_TESTERS: " + e1.getMessage());
                return new Response.Builder()
                        .type(ResponseType.ERROR)
                        .data(e1.getMessage())
                        .build();
            }
        } else if (request.type() == RequestType.GET_BUGS) {
            System.out.println("Handling GET_BUGS request");
            try {
                Iterable<Bug> bugs = server.getAllBugs();
                System.out.println("Found bugs: " + bugs);
                return new Response.Builder()
                        .type(ResponseType.GET_BUGS)
                        .data(bugs)
                        .build();
            } catch (Exception e1) {
                System.err.println("Error in GET_BUGS: " + e1.getMessage());
                return new Response.Builder()
                        .type(ResponseType.ERROR)
                        .data(e1.getMessage())
                        .build();
            }
        }else if (request.type() == RequestType.ADD_TESTER) {
            System.out.println("Add tester request");
            try {
                Tester tester = (Tester) request.data();
                Tester addedtester = server.addTester(tester);
                return new Response.Builder().type(ResponseType.OK).data(addedtester).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            }
        } else if (request.type() == RequestType.ADD_PROGRAMMER) {
            System.out.println("Add programmer request");
            try {
                Programmer programmer = (Programmer) request.data();
                Programmer addedprogrammer = server.addProgrammer(programmer);
                return new Response.Builder().type(ResponseType.OK).data(addedprogrammer).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            }
        }else if (request.type() == RequestType.ADD_BUG) {
            System.out.println("Add bug request");
            try {
                Bug bug = (Bug) request.data();
                Bug addedbug = server.addBug(bug.getName(), bug.getDescription(),bug.getTester(),bug.getProgrammer(),bug.getDate());
                return new Response.Builder().type(ResponseType.OK).data(addedbug).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            }
        }else if (request.type() == RequestType.UPDATE_TESTER) {
            System.out.println("Update tester request");
            try {
                Tester t = (Tester) request.data();
                Thread.sleep(1000L);
                return new Response.Builder().type(ResponseType.OK).data(t).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        } else if (request.type() == RequestType.UPDATE_PROGRAMMER) {
            System.out.println("Update programmer request");
            try {
                Programmer p = (Programmer) request.data();
                Thread.sleep(1000L);
                return new Response.Builder().type(ResponseType.OK).data(p).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }else if (request.type() == RequestType.UPDATE_BUG) {
            System.out.println("Update bug request");
            try {
                Bug b = (Bug) request.data();
                Bug bug = server.updateBug(b);
                Thread.sleep(1000L);
                return new Response.Builder().type(ResponseType.OK).data(bug).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }else if (request.type() == RequestType.DELETE_PROGRAMMER) {
            System.out.println("Delete programmer request");
            try {
                Long p = (Long) request.data();
                Programmer prog = server.deleteProgrammer(p);
                Thread.sleep(1000L);
                return new Response.Builder().type(ResponseType.OK).data(prog).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }else if (request.type() == RequestType.DELETE_TESTER) {
            System.out.println("Delete tester request");
            try {
                Long id = (Long) request.data();
                Tester test = server.deleteTester(id);
                Thread.sleep(1000L);
                return new Response.Builder().type(ResponseType.OK).data(test).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }
        else if (request.type() == RequestType.DELETE_BUG) {
            System.out.println("Delete bug request");
            try {
                Long p = (Long) request.data();
                Bug bug = server.deleteBug(p);
                Thread.sleep(1000L);
                return new Response.Builder().type(ResponseType.OK).data(bug).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
        }else if (request.type() == RequestType.FIND_BY_EMAIL_PROGRAMMER) {
            try {
                String email = (String) request.data();
                Programmer programmer = server.findProgrammerByEmail(email);
                return new Response.Builder().type(ResponseType.FIND_BY_EMAIL_PROGRAMMER).data(programmer).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            }
        }
        else if (request.type() == RequestType.FIND_BY_EMAIL_ADMIN) {
            try {
                String email = (String) request.data();
                Admin admin = server.findAdminByEmail(email);
                return new Response.Builder().type(ResponseType.FIND_BY_EMAIL_ADMIN).data(admin).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            }
        }
        else if (request.type() == RequestType.FIND_BY_EMAIL_TESTER) {
            try {
                String email = (String) request.data();
                Tester tester = server.findTesterByEmail(email);
                return new Response.Builder().type(ResponseType.FIND_BY_EMAIL_TESTER).data(tester).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            }
        }
        else if (request.type() == RequestType.FIND_BY_NAME_BUG) {
            try {
                String email = (String) request.data();
                Bug bug = server.searchBugByName(email);
                return new Response.Builder().type(ResponseType.FIND_BY_NAME_BUG).data(bug).build();
            } catch (RuntimeException e1) {
                return new Response.Builder().type(ResponseType.ERROR).data(e1.getMessage()).build();
            }
        }

        else {
            return (Response)response;
        }
    }


    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }

    static {
        okResponse = (new Response.Builder()).type(ResponseType.OK).build();
    }

    @Override
    public void updatedBug(Bug bug) {
        Response resp = new Response.Builder().type(ResponseType.UPDATED_BUG).data(bug).build();
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addedBug(Bug bug) {
        Response resp = new Response.Builder().type(ResponseType.REPORTED_BUG).data(bug).build();
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deletedBug(Bug data) {
        Response resp = new Response.Builder().type(ResponseType.DELETED_BUG).data(data).build();
        try {
            sendResponse(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
