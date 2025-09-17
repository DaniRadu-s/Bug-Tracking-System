package ro.mpp2025;

import java.io.IOException;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ro.mpp2025.network.utils.AbstractServer;
import ro.mpp2025.network.utils.TransportRpcConcurrentServer;
import ro.mpp2025.repository.database.*;
import ro.mpp2025.server.Service;
import ro.mpp2025.services.IServices;

public class StartRpcServer {
    private static int defaultPort = 55555;

    public StartRpcServer() {
    }

    public static void main(String[] args) {
        Properties serverProps = new Properties();

        try {
            serverProps.load(StartRpcServer.class.getResourceAsStream("/transportserver.properties"));
            System.out.println("Server properties set. ");
            serverProps.list(System.out);
        } catch (IOException var23) {
            IOException e = var23;
            System.err.println("Cannot find transportserver.properties " + e);
            return;
        }

        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure("hibernate.cfg.xml")
                .build();
        final SessionFactory sessionFactory = new MetadataSources(registry)
                .buildMetadata()
                .buildSessionFactory();

        String url = serverProps.getProperty("db.url");
        String username = serverProps.getProperty("db.username");
        String password = serverProps.getProperty("db.password");

        //RepoDBTester dbTester = new RepoDBTester(url, username, password);
        RepoHibernateTester dbTester = new RepoHibernateTester(sessionFactory);
        //RepoDBProgrammer dbProgrammer = new RepoDBProgrammer(url, username, password);
        RepoHibernateProgrammer dbProgrammer = new RepoHibernateProgrammer(sessionFactory);
        //RepoDBBug dbBug = new RepoDBBug(url, username, password);
        RepoHibernateBug dbBug = new RepoHibernateBug(sessionFactory);
        RepoHibernateAdmin dbAdmin = new RepoHibernateAdmin(sessionFactory);
        IServices service = new Service(dbTester, dbProgrammer, dbBug, dbAdmin);
        int transportServerPort = defaultPort;

        try {
            transportServerPort = Integer.parseInt(serverProps.getProperty("transport.server.port"));
        } catch (NumberFormatException var22) {
            NumberFormatException nef = var22;
            System.err.println("Wrong  Port Number" + nef.getMessage());
            System.err.println("Using default port " + defaultPort);
        }

        System.out.println("Starting server on port: " + transportServerPort);
        AbstractServer server = new TransportRpcConcurrentServer(transportServerPort, service);

        try {
            ((AbstractServer)server).start();
        } catch (RuntimeException var20) {
            RuntimeException e = var20;
            System.err.println("Error starting the server" + e.getMessage());
        } finally {
            try {
                ((AbstractServer)server).stop();
            } catch (RuntimeException var19) {
                RuntimeException e = var19;
                System.err.println("Error stopping server " + e.getMessage());
            }

        }

    }
}
