package ro.mpp2025.services;

import ro.mpp2025.domain.Admin;
import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;

import java.time.LocalDateTime;

public interface IServices {
    void loginAdmin(Admin admin, IObserver observer);
    Admin findAdminByEmail(String email);
    void loginTester(Tester tester, IObserver client);
    void loginProgrammer(Programmer programmer, IObserver client);
    void logoutTester(Tester tester, IObserver client);
    void logoutProgrammer(Programmer programmer, IObserver client);
    void logoutAdmin(Admin admin, IObserver observer);
    Tester addTester(Tester tester);
    Tester deleteTester(Long id);
    Tester updateTester(Tester tester);
    Tester findTesterByEmail(String email);
    Iterable<Tester> getAllTesters();
    Programmer addProgrammer(Programmer programmer);
    Programmer deleteProgrammer(Long id);
    Programmer updateProgrammer(Programmer programmer);
    Iterable<Programmer> getAllProgrammers();
    Programmer findProgrammerByEmail(String email);
    Bug addBug(String name, String description, Tester tester, Programmer programmer, LocalDateTime date);
    Bug deleteBug(Long id);
    Bug updateBug(Bug bug);
    Iterable<Bug> getAllBugs();
    Bug searchBugByName(String name);
}
