/**
 * 
 */
package com.excel.base.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.excel.base.beans.BeanCellExcel;
import com.excel.base.services.UploadExcelService;

/**
 * @author vin00
 *
 */
@RestController
@RequestMapping(path="/upload")
public class UploadExcelController {

	@Autowired
	UploadExcelService uploadExcelService;
	
	private static Logger LOG = LoggerFactory.getLogger(UploadExcelController.class);
	
	@PostMapping("/uploadExcel.do")
	public List<List<Map<String, BeanCellExcel>>> uploadExcel(@RequestParam("excelFile") MultipartFile multipartFile) {
		try {
			return uploadExcelService.uploadExcelFile(multipartFile);
		} catch (IOException e) {
			LOG.error("Error uploading excel file: ",e);
		}
		return new ArrayList<>();
	}
	
	
	@PostMapping("/generateExcel.do")
	public ResponseEntity<InputStreamResource> generateReport(@RequestParam(name = "identification", required = true) String identification, @RequestParam(name = "employNumber", required = true) int employNumber) {
		ByteArrayInputStream generateExcelReport = null;
		try {
			generateExcelReport = uploadExcelService.generateExcelReport(identification,employNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
		HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");
    
     return ResponseEntity
                  .ok()
                  .headers(headers)
                  .body(new InputStreamResource(generateExcelReport));
	}
}
