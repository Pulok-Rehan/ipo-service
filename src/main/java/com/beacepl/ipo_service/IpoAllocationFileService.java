//package com.beacepl.ipo_service;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class IpoAllocationFileService {
//
//    private final IpoApplicationRepository repository;
//    private static final String DELIMITER = "~";
//    private static final int EXPECTED_COLUMNS = 13; // per DP95 report spec
//
//    public FileUploadResultDto processAllocationFile(MultipartFile file, String ipoId) throws IOException {
//        int total = 0, updated = 0, notFound = 0, errors = 0;
//        List<String> notFoundCodes = new ArrayList<>();
//        List<String> errorMessages = new ArrayList<>();
//
//        try (BufferedReader reader = new BufferedReader(
//                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                if (line.isBlank()) continue;
//                total++;
//
//                try {
//                    String[] cols = line.split(DELIMITER, -1);
//                    if (cols.length < EXPECTED_COLUMNS) {
//                        errors++;
//                        errorMessages.add("Line " + total + ": expected " + EXPECTED_COLUMNS
//                                + " columns, found " + cols.length);
//                        continue;
//                    }
//
//                    // --- DP95 column mapping ---
//                    String isin              = cols[0].trim();  // Col1
//                    String isinShortName     = cols[1].trim();  // Col2
//                    String sequenceNo        = cols[2].trim();  // Col3
//                    String effectiveDate     = cols[3].trim();  // Col4
//                    String boId              = cols[4].trim();  // Col5 -> investorCode
//                    String boShortName       = cols[5].trim();  // Col6
//                    String boCategory        = cols[6].trim();  // Col7
//                    String boAccountStatus   = cols[7].trim();  // Col8
//                    double currentBalance     = parseDoubleSafe(cols[8]);  // Col9 -> allocated qty
//                    double lockinBalance      = parseDoubleSafe(cols[9]);  // Col10
//                    String procFlag          = cols[10].trim(); // Col11
//                    double blockBalance       = parseDoubleSafe(cols[11]); // Col12
//                    String lockInExpDate     = cols[12].trim(); // Col13
//
//                    Optional<IpoApplication> existing =
//                            repository.findByInvestorCodeAndIpoId(boId, ipoId);
//
//                    if (existing.isEmpty()) {
//                        notFound++;
//                        notFoundCodes.add(boId);
//                        continue;
//                    }
//
//                    IpoApplication app = existing.get();
//                    app.setQuantity((int) currentBalance);
//                    app.setStatus("ALLOCATED");
//                    app.setExecutedOn(effectiveDate);
//                    app.setInstrumentId(isin);
//                    repository.save(app);
//                    updated++;
//
//                } catch (Exception ex) {
//                    errors++;
//                    errorMessages.add("Line " + total + ": " + ex.getMessage());
//                }
//            }
//        }
//
//        return FileUploadResultDto.builder()
//                .totalLines(total)
//                .updatedCount(updated)
//                .notFoundCount(notFound)
//                .errorCount(errors)
//                .notFoundInvestorCodes(notFoundCodes)
//                .errors(errorMessages)
//                .build();
//    }
//
//    private double parseDoubleSafe(String s) {
//        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }
//    }
//}