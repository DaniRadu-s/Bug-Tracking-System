package ro.mpp2025.services;

import ro.mpp2025.domain.Bug;
import ro.mpp2025.domain.Programmer;

public interface IObserver {
    void updatedBug(Bug data);
    void addedBug(Bug data);
    void deletedBug(Bug data);
}
