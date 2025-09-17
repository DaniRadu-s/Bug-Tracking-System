package ro.mpp2025.repository.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ro.mpp2025.domain.Programmer;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.domain.User;
import ro.mpp2025.repository.interfaces.RepoInterfaceProgrammer;
import ro.mpp2025.repository.interfaces.RepoInterfaceTester;

import java.util.Collection;
import java.util.Optional;

public class RepoHibernateProgrammer implements RepoInterfaceProgrammer {
    private final SessionFactory sessionFactory;

    public RepoHibernateProgrammer(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Programmer> save(Programmer part) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(part);
            transaction.commit();
            return Optional.of(part);
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Programmer> delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Programmer part = session.get(Programmer.class, id);
            if (part != null) {
                session.remove(part);
                transaction.commit();
                return Optional.of(part);
            }
            transaction.commit();
            return Optional.empty();
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Programmer> update(Programmer part) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(part);
            transaction.commit();
            return Optional.of(part);
        } catch (Exception exception) {
            if (transaction != null) {
                transaction.rollback();
            }
            return Optional.empty();
        }
    }

    @Override
    public Optional<Programmer> findOne(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Programmer.class, id));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Programmer> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Programmer> query = session.createQuery("FROM Programmer", Programmer.class);
            return query.list();
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public Optional<Programmer> findProgrammerByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Programmer> query = session.createQuery("FROM Programmer WHERE email =:email", Programmer.class);
            query.setParameter("email", email);
            Programmer tester = query.uniqueResult();
            return Optional.ofNullable(tester);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
