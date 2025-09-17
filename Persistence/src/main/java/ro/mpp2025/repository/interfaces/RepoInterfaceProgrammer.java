package ro.mpp2025.repository.interfaces;

import ro.mpp2025.domain.Programmer;
import ro.mpp2025.repository.Repository;

import java.util.Optional;

public interface RepoInterfaceProgrammer extends Repository<Long, Programmer> {
    Optional<Programmer> findProgrammerByEmail(String email);
}
