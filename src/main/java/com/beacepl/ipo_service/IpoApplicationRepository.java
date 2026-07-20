package com.beacepl.ipo_service;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface IpoApplicationRepository extends MongoRepository<IpoApplication, String> {
    List<IpoApplication> findByInvestorCode(String investorCode);
    Optional<IpoApplication> findByInvestorCodeAndIpoId(String investorCode, String ipoId);
}
