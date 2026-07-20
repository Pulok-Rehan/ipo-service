package com.beacepl.ipo_service;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;


@Slf4j
@RequiredArgsConstructor
@Service
public class IpoServiceImpl implements IpoService {
    private final IpoApplicationRepository ipoApplicationRepository;
    private final SpApiClientService spApiClientService;
    private final ObjectMapper objectMapper;

    @Override
    public ServiceResponse applyForIpo(IpoApplicationDto ipoApplicationDto) {
        try {
            ApplyIpoDto applyIpoDto = objectMapper.convertValue(ipoApplicationDto, ApplyIpoDto.class);
            applyIpoDto.setRate(ipoApplicationDto.getFaceValue() + ipoApplicationDto.getPremium());
            IpoResponse spResponse = spApiClientService.applyForIpo(applyIpoDto);
            if (!spResponse.getResult().equals(1)) {
                return new ServiceResponse("Failed to apply for IPO.", null, "500");
            } else {
                IpoApplication ipoApplication = IpoApplication.builder()
                        .appliedAmount(ipoApplicationDto.getAmount())
                        .companyName(ipoApplicationDto.getCompanyName())
                        .investorCode(ipoApplicationDto.getInvestorCode())
                        .initiatedOn(LocalDateTime.now().toString())
                        .faceValue(ipoApplicationDto.getFaceValue())
                        .premium(ipoApplicationDto.getPremium())
                        .rate(applyIpoDto.getRate())
                        .ipoId(ipoApplicationDto.getIpoId())
                        .appliedAmount(ipoApplicationDto.getAmount())
                        .status(Status.APPLIED.toString())
                        .instrumentId(applyIpoDto.getInstrumentId())
                        .accountId(applyIpoDto.getAccountId())
                        .offerId(applyIpoDto.getOfferId())
                        .build();
                ipoApplicationRepository.save(ipoApplication);
                return new ServiceResponse("Ipo applied successfully", ipoApplication, "200");


            }
        } catch (Exception e) {
            log.error("Error calling stored procedure: ", e);
            return new ServiceResponse("Failed to apply for IPO.", null, "500");
        }
    }

    @Override
    public ServiceResponse getActiveIpoList(String investorCode, String date) {
        log.info("Fetching active IPO list from stored procedure.");

        try {
           List<IpoEntityResponse> ipoEntityResponseList = this.getActiveIpo(investorCode, date);
            return new ServiceResponse("Active IPO list fetched successfully", ipoEntityResponseList, "200");
        } catch (Exception e) {
            log.error("Error calling stored procedure: ", e);
            return new ServiceResponse("Failed to fetch active IPO list.", null, "500");
        }
}

    @Override
    public ServiceResponse getActiveIpoListAdmin(String date) {
        log.info("Fetching active IPO list from stored procedure.");

        try {
            List<IpoEntityResponse> ipoEntityResponseList = this.getActiveIpoListForAdmin(date);
            return new ServiceResponse("Active IPO list fetched successfully", objectMapper.writeValueAsString(ipoEntityResponseList), "200");
        } catch (Exception e) {
            log.error("Error calling stored procedure: ", e);
            return new ServiceResponse("Failed to fetch active IPO list.", null, "500");
        }
    }

    @Override
    public ServiceResponse getPreviousIpoRequests(String investorCode) {
        log.info("Fetching previous IPO requests from database for investor code: {}", investorCode);
        List<IpoApplication> ipoApplicationOptional = ipoApplicationRepository.findByInvestorCode(investorCode);
        if (ipoApplicationOptional.isEmpty()){
            return new ServiceResponse("No history found", "400");
        }
        return new ServiceResponse("Previous IPO requests fetched successfully", ipoApplicationOptional, "200");
    }

    private List<IpoEntityResponse> getActiveIpo(String investorCode, String date) throws IOException, InterruptedException {
        List<IpoEntityResponse> ipoEntityResponseList = new ArrayList<>();
        List<IpoInfo> ipoInfos = spApiClientService.getIpoList(investorCode, date);
        for (IpoInfo ipoInfo : ipoInfos){
            IpoEntityResponse ipoEntityResponse = this.mapToIpoEntityResponse(ipoInfo);
            ipoEntityResponseList.add(ipoEntityResponse);
        }
        return ipoEntityResponseList;
    }

    private List<IpoEntityResponse> getActiveIpoListForAdmin(String date) throws IOException, InterruptedException {
        List<IpoEntityResponse> ipoEntityResponseList = new ArrayList<>();
        List<IpoInfo> ipoInfos = spApiClientService.getIpoListAdmin(date);
        for (IpoInfo ipoInfo : ipoInfos){
            IpoEntityResponse ipoEntityResponse = this.mapToIpoEntityResponse(ipoInfo);
            ipoEntityResponseList.add(ipoEntityResponse);
        }
        return ipoEntityResponseList;
    }

    private IpoEntityResponse mapToIpoEntityResponse(IpoInfo ipoInfo) {

        IpoEntityResponse response = new IpoEntityResponse();

        response.setCompanyName(ipoInfo.getInstrumentName());
        response.setValidity(ipoInfo.getLastApplicationDate());
        response.setFaceValue(Integer.parseInt(ipoInfo.getFaceValue()));
        response.setPremium(Double.parseDouble(ipoInfo.getPremium()));
        response.setOfferPrice(Double.parseDouble(String.valueOf(Integer.parseInt(ipoInfo.getFaceValue())+Integer.parseInt(ipoInfo.getPremium()))));
        response.setAmountRemarks(Integer.parseInt(ipoInfo.getMinumumAmount())*Integer.parseInt(String.valueOf(ipoInfo.getMultiplyBy())));
        response.setEligibility(ipoInfo.getEligibility() != null ? ipoInfo.getEligibility().equals(BigDecimal.valueOf(1)) : false);
        response.setMaturedShareValue(ipoInfo.getMaturedValueRdt());
        if (!response.isEligibility()){
            response.setRemarksForNonEligibility("You do not have enough market value on the record date");
        }
        response.setLedgerBalance(String.valueOf(ipoInfo.getLedgerBalance()));
        response.setRecordDate(ipoInfo.getRecordDate());
        response.setAccountId(String.valueOf(ipoInfo.getAccountId()));
        response.setInstrumentId(String.valueOf(ipoInfo.getInstrumentId()));
        response.setPublicOfferId(String.valueOf(ipoInfo.getPublicOfferId()));

        return response;
    }
}
