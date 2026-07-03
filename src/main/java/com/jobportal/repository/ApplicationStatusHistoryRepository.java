package com.jobportal.repository;

import com.jobportal.model.ApplicationStatusHistory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationStatusHistoryRepository extends MongoRepository<ApplicationStatusHistory, String> {
    List<ApplicationStatusHistory> findByApplicationIdOrderByUpdatedAtAsc(String applicationId);
}
