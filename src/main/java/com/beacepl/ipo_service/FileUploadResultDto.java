package com.beacepl.ipo_service;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FileUploadResultDto {
    private int totalLines;
    private int updatedCount;
    private int notFoundCount;
    private int errorCount;
    private List<String> notFoundInvestorCodes;
    private List<String> errors;
}