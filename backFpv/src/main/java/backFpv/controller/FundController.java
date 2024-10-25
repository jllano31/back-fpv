package backFpv.controller;

import backFpv.dto.FundDTO;
import backFpv.service.FundService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/funds")
public class FundController {

    @Autowired
    private FundService fundService;

    @GetMapping
    public ResponseEntity<List<FundDTO>> getAllFunds() {
        try {
            List<FundDTO> funds = fundService.getAllFunds();
            return new ResponseEntity<>(funds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<FundDTO> getFundById(@PathVariable String id) {
        try {
            FundDTO fund = fundService.getFundById(id);
            return new ResponseEntity<>(fund, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<FundDTO> createFund(@Valid @RequestBody FundDTO fundDTO) {
        try {
            FundDTO savedFund = fundService.saveFund(fundDTO);
            return new ResponseEntity<>(savedFund, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<FundDTO> updateFund(@PathVariable String id, @Valid @RequestBody FundDTO fundDTO) {
        try {
            fundDTO.setId(id);
            FundDTO updatedFund = fundService.saveFund(fundDTO);
            return new ResponseEntity<>(updatedFund, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFund(@PathVariable String id) {
        try {
            fundService.deleteFund(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
