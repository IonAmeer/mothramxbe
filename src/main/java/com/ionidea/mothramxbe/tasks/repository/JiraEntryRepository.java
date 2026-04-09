package com.ionidea.mothramxbe.tasks.repository;

import com.ionidea.mothramxbe.tasks.model.JiraEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JiraEntryRepository extends JpaRepository<JiraEntry, Integer> {

}