package ro.mpp2025.repository.interfaces;

import ro.mpp2025.domain.Tester;
import ro.mpp2025.repository.Repository;

import java.util.Optional;

public interface RepoInterfaceTester extends Repository<Long, Tester> {
    Optional<Tester> findTesterByEmail(String email);
}
