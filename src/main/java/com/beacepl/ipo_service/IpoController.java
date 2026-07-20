package com.beacepl.ipo_service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ipo/api")
@Slf4j
@AllArgsConstructor
public class IpoController {

    private final IpoService ipoService;

    @PostMapping("/apply")
    public ServiceResponse applyForIpo(@RequestBody IpoApplicationDto ipoApplicationDto) {
        log.info("Received request to apply for IPO of: {}", ipoApplicationDto.getCompanyName());
        return ipoService.applyForIpo(ipoApplicationDto);
    }

    @GetMapping("/active-list")
    public ServiceResponse getActiveIpoList(@RequestParam String investorCode, @RequestParam String date) {
        log.info("Received request to fetch active IPO list.");
        return ipoService.getActiveIpoList(investorCode, date);
    }

    @GetMapping("/active-ipo-list")
    public ServiceResponse getActiveIpoListAdmin(@RequestParam String date) {
        log.info("Received request to fetch active IPO list.");
        return ipoService.getActiveIpoListAdmin(date);
    }

    @GetMapping("/previous-requests")
    public ServiceResponse getPreviousIpoRequests(@RequestParam String investorCode) {
        log.info("Received request to fetch previous IPO requests for investorCode: {}", investorCode);
        return ipoService.getPreviousIpoRequests(investorCode);
    }

    @GetMapping
    public String test(){
        return "API CALLED";
    }
}
