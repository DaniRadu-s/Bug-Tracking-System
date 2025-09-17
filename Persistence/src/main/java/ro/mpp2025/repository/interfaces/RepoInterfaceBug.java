package ro.mpp2025.repository.interfaces;

import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.repository.Repository;

import java.util.Optional;

public interface RepoInterfaceBug extends Repository<Long, Bug> {
    Optional<Bug> findBugByName(String email);

}
