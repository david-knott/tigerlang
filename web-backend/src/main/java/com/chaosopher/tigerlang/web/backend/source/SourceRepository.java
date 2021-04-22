package com.chaosopher.tigerlang.web.backend.source;

import org.springframework.data.mongodb.repository.MongoRepository;

interface SourceRepository extends MongoRepository<Source, String> {
  public Source findByName(String name);
}