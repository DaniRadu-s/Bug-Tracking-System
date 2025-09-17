package ro.mpp2025.repository.database;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import ro.mpp2025.domain.Bug;
import ro.mpp2025.repository.interfaces.RepoInterfaceBug;

import java.util.Collection;
import java.util.Optional;

public class RepoHibernateBug implements RepoInterfaceBug {
    private final SessionFactory sessionFactory;

    public RepoHibernateBug(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Optional<Bug> save(Bug bug) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(bug);
            transaction.commit();
            return Optional.of(bug);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Bug> delete(Long id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Bug bug = session.get(Bug.class, id);
            if (bug != null) {
                session.remove(bug);
                transaction.commit();
                return Optional.of(bug);
            }
            transaction.commit();
            return Optional.empty();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Bug> update(Bug bug) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.merge(bug);
            transaction.commit();
            return Optional.of(bug);
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            return Optional.empty();
        }
    }

    @Override
    public Optional<Bug> findOne(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Bug.class, id));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @Override
    public Collection<Bug> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Bug> query = session.createQuery("FROM Bug", Bug.class);
            return query.list();
        } catch (Exception e) {
            return null;
        }
    }

    public Optional<Bug> findBugByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            Query<Bug> query = session.createQuery("FROM Bug WHERE name = :name", Bug.class);
            query.setParameter("name", name);
            Bug bug = query.uniqueResult();
            return Optional.ofNullable(bug);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
