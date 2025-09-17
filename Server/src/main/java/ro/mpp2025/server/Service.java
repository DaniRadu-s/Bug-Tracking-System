package ro.mpp2025.server;

import ro.mpp2025.domain.Admin;
import ro.mpp2025.repository.interfaces.RepoInterfaceAdmin;
import ro.mpp2025.repository.interfaces.RepoInterfaceBug;
import ro.mpp2025.repository.interfaces.RepoInterfaceProgrammer;
import ro.mpp2025.repository.interfaces.RepoInterfaceTester;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Bug;
import ro.mpp2025.services.IObserver;
import ro.mpp2025.services.IServices;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class Service implements IServices {

    private RepoInterfaceTester testerRepository;
    private RepoInterfaceProgrammer programmerRepository;
    private RepoInterfaceBug bugRepository;
    private RepoInterfaceAdmin adminRepository;
    private Map<Long, IObserver> loggedTesters;
    private Map<Long, IObserver> loggedProgrammers;
    private Map<Long,IObserver> loggedAdmin;

    public Service(RepoInterfaceTester testerRepository, RepoInterfaceProgrammer programmerRepository, RepoInterfaceBug bugRepository, RepoInterfaceAdmin adminRepository) {
        this.testerRepository = testerRepository;
        this.programmerRepository = programmerRepository;
        this.bugRepository = bugRepository;
        this.adminRepository = adminRepository;
        loggedTesters=new ConcurrentHashMap<>();
        loggedProgrammers=new ConcurrentHashMap<>();
        loggedAdmin=new ConcurrentHashMap<>();
    }

    @Override
    public void loginAdmin(Admin admin, IObserver client){
        Admin user = adminRepository.findAdminByEmail(admin.getEmail()).orElse(null);
        if (user != null){
            if(loggedAdmin.get(admin.getId()) != null)
                throw new RuntimeException("User already logged in.");
            if (admin.getPassword().equals(user.getPassword())) {
                loggedAdmin.put(admin.getId(), client);
                EmailService.sendLoginConf(admin.getEmail(),"Te-ai logat cu succes, " + admin.getName() + "!");
            } else
                throw new RuntimeException("Authentication failed.");
        } else
            throw new RuntimeException("Authentication failed.");
    }

    @Override
    public void loginTester(Tester tester, IObserver client) throws RuntimeException {
        Tester user = testerRepository.findTesterByEmail(tester.getEmail()).orElse(null);
        if (user != null){
            if(loggedTesters.get(tester.getId()) != null)
                throw new RuntimeException("User already logged in.");
            if (tester.getPassword().equals(user.getPassword())) {
                loggedTesters.put(tester.getId(), client);
                EmailService.sendLoginConf(tester.getEmail(),"Te-ai logat cu succes, " + tester.getName() + "!");
            } else
                throw new RuntimeException("Authentication failed.");
        } else
            throw new RuntimeException("Authentication failed.");
    }

    @Override
    public void loginProgrammer(Programmer programmer, IObserver client) throws RuntimeException {
        Programmer user = programmerRepository.findProgrammerByEmail(programmer.getEmail()).orElse(null);
        if (user != null){
            if(loggedProgrammers.get(programmer.getId()) != null)
                throw new RuntimeException("User already logged in.");
            if (programmer.getPassword().equals(user.getPassword())) {
                loggedProgrammers.put(programmer.getId(), client);
                EmailService.sendLoginConf(programmer.getEmail(),"Te-ai logat cu succes, " + programmer.getName() + "!");
            } else
                throw new RuntimeException("Authentication failed.");
        } else
            throw new RuntimeException("Authentication failed.");
    }

    @Override
    public void logoutTester(Tester tester, IObserver client){
        IObserver localClient = loggedTesters.remove(tester.getId());
        EmailService.sendLogoutConf(tester.getEmail(),"Te-ai delogat cu succes, " + tester.getName() + "!");
        if (localClient == null)
            throw new RuntimeException("User " + tester.getUsername() + " is not logged in.");
    }

    @Override
    public void logoutProgrammer(Programmer programmer, IObserver client){
        IObserver localClient = loggedProgrammers.remove(programmer.getId());
        EmailService.sendLogoutConf(programmer.getEmail(),"Te-ai delogat cu succes, " + programmer.getName() + "!");
        if(localClient == null)
            throw new RuntimeException("User " + programmer.getUsername() + " is not logged in.");
    }

    @Override
    public void logoutAdmin(Admin admin, IObserver client){
        IObserver localClient = loggedAdmin.remove(admin.getId());
        EmailService.sendLogoutConf(admin.getEmail(),"Te-ai delogat cu succes, " + admin.getName() + "!");
        if (localClient == null)
            throw new RuntimeException("User " + admin.getEmail() + " is not logged in.");
    }

    @Override
    public Tester addTester(Tester tester) {
        return testerRepository.save(tester).orElseThrow(() -> new RuntimeException("Error adding tester"));
    }

    @Override
    public Tester deleteTester(Long id) {
        return testerRepository.delete(id).orElseThrow(() -> new RuntimeException("Error deleting tester"));
    }

    @Override
    public Tester updateTester(Tester tester) {
        return testerRepository.update(tester).orElseThrow(() -> new RuntimeException("Error updating tester"));
    }

    @Override
    public Tester findTesterByEmail(String email) {
        return testerRepository.findTesterByEmail(email).orElse(null);
    }

    @Override
    public Iterable<Tester> getAllTesters() {
        return testerRepository.findAll();
    }

    public Tester searchTesterById(Long id) {
        return testerRepository.findOne(id).orElseThrow(() -> new RuntimeException("Error getting tester"));
    }

    public Tester searchTesterByUsername(String username) {
        Iterable<Tester> testers = testerRepository.findAll();
        for (Tester tester: testers) {
            if (tester.getUsername().equals(username))
                return tester;
        }
        return null;
    }

    @Override
    public Programmer addProgrammer(Programmer programmer) {
        return programmerRepository.save(programmer).orElseThrow(() -> new RuntimeException("Error adding programmer"));
    }

    @Override
    public Programmer findProgrammerByEmail(String email) {
        return programmerRepository.findProgrammerByEmail(email).orElse(null);
    }

    @Override
    public Programmer deleteProgrammer(Long id) {
        return programmerRepository.delete(id).orElseThrow(() -> new RuntimeException("Error deleting programmer"));
    }

    @Override
    public synchronized Programmer updateProgrammer(Programmer programmer) {
        Optional<Programmer> updatedProgrammer = programmerRepository.update(programmer);
        return updatedProgrammer.orElse(null);
    }

    @Override
    public Iterable<Programmer> getAllProgrammers() {
        return programmerRepository.findAll();
    }

    public Programmer searchProgrammerById(Long id) {
        return programmerRepository.findOne(id).orElseThrow(() -> new RuntimeException("Error getting programmer"));
    }

    @Override
    public Admin findAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email).orElse(null);
    }

    public Programmer searchProgrammerByUsername(String username) {
        Iterable<Programmer> programmers = programmerRepository.findAll();
        for (Programmer programmer: programmers) {
            if (programmer.getUsername().equals(username))
                return programmer;
        }
        return null;
    }

    @Override
    public Bug addBug(String name, String description, Tester tester, Programmer programmer, LocalDateTime date) {
        Bug bug = new Bug(name, description, tester, programmer, date);
        Bug addedBug = bugRepository.save(bug).orElseThrow(() -> new RuntimeException("Error adding bug"));
        notifyAllClientsBugReported(addedBug);
        return addedBug;
    }

    private void notifyAllClientsBugReported(Bug bug) {
        for (IObserver observer : loggedTesters.values()) {
            try {
                observer.addedBug(bug);
            } catch (Exception e) {
                System.err.println("Failed to notify tester: " + e.getMessage());
            }
        }
        for (IObserver observer : loggedProgrammers.values()) {
            try {
                observer.updatedBug(bug);
            } catch (Exception e) {
                System.err.println("Failed to notify programmer: " + e.getMessage());
            }
        }
        for(IObserver observer:loggedAdmin.values()) {
            try{
                observer.addedBug(bug);
            }
            catch(Exception e){
                System.err.println("Failed to notify admin: " + e.getMessage());
            }
        }
    }

    @Override
    public Bug deleteBug(Long id) {
        Bug bug = bugRepository.delete(id).orElseThrow(() -> new RuntimeException("Error deleting bug"));
        notifyAllClientsBugDeleted(bug);
        return bug;
    }

    private void notifyAllClientsBugDeleted(Bug bug) {
        for (IObserver observer : loggedTesters.values()) {
            try {
                observer.deletedBug(bug);
            } catch (Exception e) {
                System.err.println("Failed to notify tester: " + e.getMessage());
            }
        }
        for (IObserver observer : loggedProgrammers.values()) {
            try {
                observer.deletedBug(bug);
            } catch (Exception e) {
                System.err.println("Failed to notify programmer: " + e.getMessage());
            }
        }
        for(IObserver observer:loggedAdmin.values()) {
            try{
                observer.deletedBug(bug);
            }
            catch(Exception e){
                System.err.println("Failed to notify admin: " + e.getMessage());
            }
        }
    }


    @Override
    public Bug updateBug(Bug bug) {
        Bug updatedBug = bugRepository.update(bug).orElseThrow(() -> new RuntimeException("Error updating bug"));
        notifyAllClientsBugUpdated(updatedBug);
        return updatedBug;
    }

    private void notifyAllClientsBugUpdated(Bug bug) {
        for (IObserver observer : loggedTesters.values()) {
            try {
                observer.updatedBug(bug);
            } catch (Exception e) {
                System.err.println("Failed to notify tester: " + e.getMessage());
            }
        }

        for (IObserver observer : loggedProgrammers.values()) {
            try {
                observer.updatedBug(bug);
            } catch (Exception e) {
                System.err.println("Failed to notify programmer: " + e.getMessage());
            }
        }
        for(IObserver observer:loggedAdmin.values()){
            try{
                observer.updatedBug(bug);
            }
            catch(Exception e){
                System.err.println("Failed to notify admin: " + e.getMessage());
            }
        }
    }

    @Override
    public Iterable<Bug> getAllBugs() {
        return bugRepository.findAll();
    }

    public Bug searchBugById(Long id) {
        return bugRepository.findOne(id).orElseThrow(() -> new RuntimeException("Error getting bug"));
    }

    @Override
    public Bug searchBugByName(String name) {
        return bugRepository.findBugByName(name).orElse(null);
    }
}