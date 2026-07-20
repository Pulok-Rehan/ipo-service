package com.beacepl.ipo_service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin/ipo")
@RequiredArgsConstructor
public class IpoAllotmentUploadController {

    private final IpoAllotmentFileService allotmentFileService;
//    private final IpoAllocationFileService allocationFileService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value = "/upload-allocate", consumes = "multipart/form-data")
    public ResponseEntity<FileUploadResultDto> uploadAllotmentFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("ipoId") String ipoId) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        FileUploadResultDto result = allotmentFileService.processAllotmentFile(file, ipoId);
        return ResponseEntity.ok(result);
    }
//
//    @PostMapping(value = "/upload-allocation", consumes = "multipart/form-data")
//    public ResponseEntity<FileUploadResultDto> uploadAllocationFile(
//            @RequestParam("file") MultipartFile file,
//            @RequestParam("ipoId") String ipoId) throws IOException {
//
//        if (file.isEmpty()) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        FileUploadResultDto result = allocationFileService.processAllocationFile(file, ipoId);
//        return ResponseEntity.ok(result);
//    }
}