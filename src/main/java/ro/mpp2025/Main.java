package ro.mpp2025;

import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.repository.interfaces.RepoInterfaceProgrammer;
import ro.mpp2025.repository.interfaces.RepoInterfaceTester;
import ro.mpp2025.repository.interfaces.RepoInterfaceBug;
import ro.mpp2025.server.Service;

public class Main {

    private static Service service;
    private static RepoInterfaceTester testerRepository;
    private static RepoInterfaceProgrammer programmerRepository;
    private static RepoInterfaceBug bugRepository;

    public static void main(String[] args) {
        App.main(args);
    }
}