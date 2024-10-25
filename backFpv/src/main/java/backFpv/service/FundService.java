package backFpv.service;

import backFpv.dto.FundDTO;
import backFpv.model.Fund;
import backFpv.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FundService {

    @Autowired
    private FundRepository fundRepository;

    public List<FundDTO> getAllFunds() {
        try {
            List<Fund> funds = fundRepository.findAll();
            if (funds.isEmpty()) {
                return List.of();
            }
            return funds.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener los fondos: " + e.getMessage());
        }
    }

    public FundDTO getFundById(String id) {
        try {
            Optional<Fund> fundOpt = fundRepository.findById(id);
            if (fundOpt.isPresent()) {
                return convertToDTO(fundOpt.get());
            } else {
                throw new RuntimeException("Fondo no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener el fondo con ID: " + id + ". Error: " + e.getMessage());
        }
    }

    public FundDTO saveFund(FundDTO fundDTO) {
        try {
            Fund fund = convertToEntity(fundDTO);
            Fund savedFund = fundRepository.save(fund);
            return convertToDTO(savedFund);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar/actualizar el fondo: " + e.getMessage());
        }
    }

    public void deleteFund(String id) {
        try {
            if (fundRepository.existsById(id)) {
                fundRepository.deleteById(id);
            } else {
                throw new RuntimeException("Fondo no encontrado con ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el fondo con ID: " + id + ". Error: " + e.getMessage());
        }
    }

    private FundDTO convertToDTO(Fund fund) {
        FundDTO fundDTO = new FundDTO();
        fundDTO.setId(fund.getId());
        fundDTO.setName(fund.getName());
        fundDTO.setMinimumInvestment(fund.getMinimumInvestment());
        fundDTO.setCategory(fund.getCategory());
        return fundDTO;
    }

    private Fund convertToEntity(FundDTO fundDTO) {
        Fund fund = new Fund();
        fund.setId(fundDTO.getId());
        fund.setName(fundDTO.getName());
        fund.setMinimumInvestment(fundDTO.getMinimumInvestment());
        fund.setCategory(fundDTO.getCategory());
        return fund;
    }
}
