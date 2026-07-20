package com.beacepl.ipo_service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IpoAllotmentFileService {

    private final IpoApplicationRepository repository;
    private static final String DELIMITER = "~";
    private static final int EXPECTED_COLUMNS = 13;

    public FileUploadResultDto processAllotmentFile(MultipartFile file, String ipoId) throws IOException {
        int total = 0, updated = 0, notFound = 0, errors = 0;
        List<String> notFoundCodes = new ArrayList<>();
        List<String> errorMessages = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) continue;
                total++;

                try {
                    // trailing delimiter creates an empty trailing token; -1 keeps it
                    String[] cols = line.split(DELIMITER, -1);
                    if (cols.length < EXPECTED_COLUMNS) {
                        errors++;
                        errorMessages.add("Line " + total + ": expected " + EXPECTED_COLUMNS
                                + " columns, found " + cols.length);
                        continue;
                    }

                    // --- column mapping (adjust indices here if wrong) ---
                    String investorCode          = cols[2].trim();   // investorCode
                    String appliedQuantity = cols[7].trim();   // EMP / RB / NRB
                    String fineAmount   = cols[11].trim();
                    int quantityAllotted = parseIntSafe(cols[10]);
                    int refundedAmount = parseIntSafe(cols[12]);
                    double allottedAmount = parseDoubleSafe(cols[9]);

                    Optional<IpoApplication> existing =
                            repository.findByInvestorCodeAndIpoId(investorCode, ipoId);

                    if (existing.isEmpty()) {
                        notFound++;
                        notFoundCodes.add(investorCode);
                        continue;
                    }

                    IpoApplication app = existing.get();
                    app.setQuantityAllocated(quantityAllotted);
                    app.setQuantityApplied(Integer.parseInt(appliedQuantity.trim()));
                    app.setStatus("ALLOTTED");
                    app.setFineAmount(parseDoubleSafe(fineAmount));
                    app.setRefundAmount(refundedAmount);
                    app.setExecutedOn(LocalDateTime.now().toString());
                    // keep raw category / lockin info if you want to extend the entity later
                    repository.save(app);
                    updated++;

                } catch (Exception ex) {
                    errors++;
                    errorMessages.add("Line " + total + ": " + ex.getMessage());
                }
            }
        }

        return FileUploadResultDto.builder()
                .totalLines(total)
                .updatedCount(updated)
                .notFoundCount(notFound)
                .errorCount(errors)
                .notFoundInvestorCodes(notFoundCodes)
                .errors(errorMessages)
                .build();
    }

    private int parseIntSafe(String s) {
        try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; }
    }

    private double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s.trim()); } catch (Exception e) { return 0.0; }
    }
}