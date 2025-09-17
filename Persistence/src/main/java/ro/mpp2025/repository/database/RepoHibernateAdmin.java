package ro.mpp2025.repository.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ro.mpp2025.domain.Admin;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.domain.User;
import ro.mpp2025.repository.interfaces.RepoInterfaceAdmin;
import ro.mpp2025.repository.interfaces.RepoInterfaceProgrammer;
import ro.mpp2025.repository.interfaces.RepoInterfaceTester;

import java.util.Collection;
import java.util.Optional;

public class RepoHibernateAdmin implements RepoInterfaceAdmin {
    private final SessionFactory sessionFactory;

    public RepoHibernateAdmin(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Admin> save(Admin part) {
        return Optional.empty();
    }

    @Override
    public Optional<Admin> delete(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Admin> update(Admin part) {
        return Optional.empty();
    }

    @Override
    public Optional<Admin> findOne(Long id) {
        return Optional.empty();
    }

    @Override
    public Collection<Admin> findAll() {
        return null;
    }

    @Override
    public Optional<Admin> findAdminByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Admin> query = session.createQuery("FROM Admin WHERE email =:email", Admin.class);
            query.setParameter("email", email);
            Admin tester = query.uniqueResult();
            return Optional.ofNullable(tester);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

}
