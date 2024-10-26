package backFpv.service;

import backFpv.dto.FundDTO;
import backFpv.model.Fund;
import backFpv.repository.FundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FundServiceTest {

    @Mock
    private FundRepository fundRepository;

    @InjectMocks
    private FundService fundService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllFunds_Success() {
        Fund fund = new Fund();
        fund.setId("F1");
        fund.setName("Fund Sample");
        fund.setMinimumInvestment(100.0);
        fund.setCategory("Category Sample");
        List<Fund> funds = new ArrayList<>();
        funds.add(fund);
        when(fundRepository.findAll()).thenReturn(funds);
        List<FundDTO> fundDTOs = fundService.getAllFunds();
        assertNotNull(fundDTOs);
        assertEquals(1, fundDTOs.size());
        assertEquals("F1", fundDTOs.get(0).getId());
        assertEquals("Fund Sample", fundDTOs.get(0).getName());
        assertEquals(100.0, fundDTOs.get(0).getMinimumInvestment());
        assertEquals("Category Sample", fundDTOs.get(0).getCategory());
        verify(fundRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFunds_EmptyList() {
        when(fundRepository.findAll()).thenReturn(new ArrayList<>());
        List<FundDTO> fundDTOs = fundService.getAllFunds();
        assertNotNull(fundDTOs);
        assertTrue(fundDTOs.isEmpty());
        verify(fundRepository, times(1)).findAll();
    }

    @Test
    void testGetAllFunds_Exception() {
        when(fundRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, fundService::getAllFunds);
        assertTrue(exception.getMessage().contains("Error al obtener los fondos: Database error"));
        verify(fundRepository, times(1)).findAll();
    }

    @Test
    void testGetFundById_Success() {
        Fund fund = new Fund();
        fund.setId("F1");
        fund.setName("Sample Fund");
        fund.setMinimumInvestment(100.0);
        fund.setCategory("Category");
        when(fundRepository.findById("F1")).thenReturn(Optional.of(fund));
        FundDTO fundDTO = fundService.getFundById("F1");
        assertNotNull(fundDTO);
        assertEquals("F1", fundDTO.getId());
        assertEquals("Sample Fund", fundDTO.getName());
        assertEquals(100.0, fundDTO.getMinimumInvestment());
        assertEquals("Category", fundDTO.getCategory());
        verify(fundRepository, times(1)).findById("F1");
    }

    @Test
    void testGetFundById_FundNotFound() {
        when(fundRepository.findById("F2")).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> fundService.getFundById("F2"));
        assertTrue(exception.getMessage().contains("Fondo no encontrado con ID: F2"));
        verify(fundRepository, times(1)).findById("F2");
    }

    @Test
    void testGetFundById_Exception() {
        when(fundRepository.findById("F3")).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> fundService.getFundById("F3"));
        assertTrue(exception.getMessage().contains("Error al obtener el fondo con ID: F3. Error: Database error"));
        verify(fundRepository, times(1)).findById("F3");
    }

    @Test
    void testSaveFund_Success() {
        FundDTO fundDTO = new FundDTO();
        fundDTO.setId("F1");
        fundDTO.setName("Sample Fund");
        fundDTO.setMinimumInvestment(100.0);
        fundDTO.setCategory("Category");
        Fund fund = new Fund();
        fund.setId("F1");
        fund.setName("Sample Fund");
        fund.setMinimumInvestment(100.0);
        fund.setCategory("Category");
        when(fundRepository.save(any(Fund.class))).thenReturn(fund);
        FundDTO savedFundDTO = fundService.saveFund(fundDTO);
        assertNotNull(savedFundDTO);
        assertEquals("F1", savedFundDTO.getId());
        assertEquals("Sample Fund", savedFundDTO.getName());
        assertEquals(100.0, savedFundDTO.getMinimumInvestment());
        assertEquals("Category", savedFundDTO.getCategory());
        verify(fundRepository, times(1)).save(any(Fund.class));
    }

    @Test
    void testSaveFund_Exception() {
        FundDTO fundDTO = new FundDTO();
        fundDTO.setId("F2");
        fundDTO.setName("Faulty Fund");
        when(fundRepository.save(any(Fund.class))).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> fundService.saveFund(fundDTO));
        assertTrue(exception.getMessage().contains("Error al guardar/actualizar el fondo: Database error"));
        verify(fundRepository, times(1)).save(any(Fund.class));
    }

    @Test
    void testDeleteFund_Success() {
        String fundId = "F1";
        when(fundRepository.existsById(fundId)).thenReturn(true);
        assertDoesNotThrow(() -> fundService.deleteFund(fundId));
        verify(fundRepository, times(1)).existsById(fundId);
        verify(fundRepository, times(1)).deleteById(fundId);
    }

    @Test
    void testDeleteFund_NotFound() {
        String fundId = "F2";
        when(fundRepository.existsById(fundId)).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> fundService.deleteFund(fundId));
        assertTrue(exception.getMessage().contains("Fondo no encontrado con ID: " + fundId));
        verify(fundRepository, times(1)).existsById(fundId);
        verify(fundRepository, never()).deleteById(fundId);
    }

    @Test
    void testDeleteFund_Exception() {
        String fundId = "F3";
        when(fundRepository.existsById(fundId)).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> fundService.deleteFund(fundId));
        assertTrue(exception.getMessage().contains("Error al eliminar el fondo con ID: " + fundId + ". Error: Database error"));
        verify(fundRepository, times(1)).existsById(fundId);
        verify(fundRepository, never()).deleteById(fundId);
    }
}
