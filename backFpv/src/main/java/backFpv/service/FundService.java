package backFpv.service;

import backFpv.dto.FundDTO;
import backFpv.model.Fund;
import backFpv.repository.FundRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones relacionadas con los fondos,
 * incluyendo creación, actualización, obtención y eliminación.
 */
@Service
public class FundService {

    /** Repositorio para realizar operaciones de base de datos con los fondos. */
    @Autowired
    private FundRepository fundRepository;

    /**
     * Obtener todos los fondos disponibles.
     *
     * @return Lista de objetos FundDTO.
     */
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

    /**
     * Obtener un fondo por su ID.
     *
     * @param id ID del fondo a obtener.
     * @return FundDTO correspondiente al fondo.
     */
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

    /**
     * Guardar o actualizar un fondo.
     *
     * @param fundDTO Objeto FundDTO con los datos del fondo.
     * @return FundDTO correspondiente al fondo guardado o actualizado.
     */
    public FundDTO saveFund(FundDTO fundDTO) {
        try {
            Fund fund = convertToEntity(fundDTO);
            Fund savedFund = fundRepository.save(fund);
            return convertToDTO(savedFund);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar/actualizar el fondo: " + e.getMessage());
        }
    }

    /**
     * Eliminar un fondo por su ID.
     *
     * @param id ID del fondo a eliminar.
     */
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

    /**
     * Convertir un objeto Fund a FundDTO.
     *
     * @param fund Objeto Fund a convertir.
     * @return Objeto FundDTO.
     */
    private FundDTO convertToDTO(Fund fund) {
        FundDTO fundDTO = new FundDTO();
        fundDTO.setId(fund.getId());
        fundDTO.setName(fund.getName());
        fundDTO.setMinimumInvestment(fund.getMinimumInvestment());
        fundDTO.setCategory(fund.getCategory());
        return fundDTO;
    }

    /**
     * Convertir un objeto FundDTO a Fund.
     *
     * @param fundDTO Objeto FundDTO a convertir.
     * @return Objeto Fund.
     */
    private Fund convertToEntity(FundDTO fundDTO) {
        Fund fund = new Fund();
        fund.setId(fundDTO.getId());
        fund.setName(fundDTO.getName());
        fund.setMinimumInvestment(fundDTO.getMinimumInvestment());
        fund.setCategory(fundDTO.getCategory());
        return fund;
    }
}
