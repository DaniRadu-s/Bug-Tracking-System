package ro.mpp2025.repository.interfaces;

import ro.mpp2025.domain.Admin;
import ro.mpp2025.repository.Repository;

import java.util.Optional;

public interface RepoInterfaceAdmin extends Repository<Long, Admin> {
    Optional<Admin> findAdminByEmail(String email);
}
