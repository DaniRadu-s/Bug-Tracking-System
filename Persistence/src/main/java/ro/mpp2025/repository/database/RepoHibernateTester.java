package ro.mpp2025.repository.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ro.mpp2025.domain.Tester;
import ro.mpp2025.domain.User;
import ro.mpp2025.repository.interfaces.RepoInterfaceTester;

import java.util.Collection;
import java.util.Optional;

public class RepoHibernateTester implements RepoInterfaceTester {
    private final SessionFactory sessionFactory;

    public RepoHibernateTester(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Tester> save(Tester part) {
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
    public Optional<Tester> delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Tester part = session.get(Tester.class, id);
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
    public Optional<Tester> update(Tester part) {
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
    public Optional<Tester> findOne(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Tester.class, id));
        } catch (Exception exception) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Tester> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Tester> query = session.createQuery("FROM Tester", Tester.class);
            return query.list();
        } catch (Exception exception) {
            return null;
        }
    }

    @Override
    public Optional<Tester> findTesterByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<Tester> query = session.createQuery("FROM Tester WHERE email =:email", Tester.class);
            query.setParameter("email", email);
            Tester tester = query.uniqueResult();
            return Optional.ofNullable(tester);
        } catch (Exception e) {
            return Optional.empty();
        }
    }


}
