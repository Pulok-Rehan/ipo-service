package com.beacepl.ipo_service;


public interface IpoService {
    ServiceResponse applyForIpo(IpoApplicationDto ipoApplicationDto);
    ServiceResponse getActiveIpoList(String investorCode, String date);
    ServiceResponse getActiveIpoListAdmin( String date);
    ServiceResponse getPreviousIpoRequests(String mobileNumber);
}
