package backFpv.controller;

import backFpv.dto.FundDTO;
import backFpv.service.FundService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FundController.class)
public class FundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FundService fundService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllFunds_ReturnsFundListWithStatusOk() throws Exception {
        FundDTO fund = new FundDTO();
        fund.setId("1");
        fund.setName("Fund Name");
        List<FundDTO> fundList = Collections.singletonList(fund);
        when(fundService.getAllFunds()).thenReturn(fundList);
        mockMvc.perform(get("/api/funds"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].name").value("Fund Name"));
    }

    @Test
    void getAllFunds_ReturnsInternalServerError() throws Exception {
        when(fundService.getAllFunds()).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/funds"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getFundById_ReturnsFundWithStatusOk() throws Exception {
        FundDTO fund = new FundDTO();
        fund.setId("1");
        fund.setName("Fund Name");
        when(fundService.getFundById(anyString())).thenReturn(fund);
        mockMvc.perform(get("/api/funds/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Fund Name"));
    }

    @Test
    void getFundById_ReturnsNotFound() throws Exception {
        when(fundService.getFundById(anyString())).thenThrow(new RuntimeException());
        mockMvc.perform(get("/api/funds/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createFund_ReturnsCreatedFundWithStatusCreated() throws Exception {
        FundDTO newFund = new FundDTO();
        newFund.setName("New Fund");
        newFund.setMinimumInvestment(1000.0); // Valor válido para cumplir con @NotNull
        newFund.setCategory("Growth"); // Valor válido para cumplir con @NotBlank
        when(fundService.saveFund(any(FundDTO.class))).thenReturn(newFund);
        mockMvc.perform(post("/api/funds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFund)))
                .andExpect(status().isCreated());
    }


    @Test
    void createFund_ReturnsInternalServerError() throws Exception {
        FundDTO newFund = new FundDTO();
        newFund.setName("New Fund");
        newFund.setMinimumInvestment(1000.0); // Valor válido para evitar error de validación
        newFund.setCategory("Growth"); // Valor válido para evitar error de validación
        when(fundService.saveFund(any(FundDTO.class))).thenThrow(new RuntimeException("Error interno"));
        mockMvc.perform(post("/api/funds")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFund)))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateFund_ReturnsUpdatedFundWithStatusOk() throws Exception {
        FundDTO fundToUpdate = new FundDTO();
        fundToUpdate.setId("1");
        fundToUpdate.setName("Updated Fund");
        fundToUpdate.setMinimumInvestment(1000.0); // Valor válido para evitar el error de validación
        fundToUpdate.setCategory("Growth"); // Valor válido para evitar el error de validación
        when(fundService.saveFund(any(FundDTO.class))).thenReturn(fundToUpdate);
        mockMvc.perform(put("/api/funds/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(fundToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("Updated Fund"))
                .andExpect(jsonPath("$.minimumInvestment").value(1000.0))
                .andExpect(jsonPath("$.category").value("Growth"));
    }

    @Test
    void updateFund_ReturnsNotFound() throws Exception {
        FundDTO updatedFund = new FundDTO();
        updatedFund.setId("1");
        updatedFund.setName("Updated Fund");
        updatedFund.setMinimumInvestment(1000.0); // Valor válido
        updatedFund.setCategory("Growth"); // Valor válido
        when(fundService.saveFund(any(FundDTO.class))).thenThrow(new RuntimeException("Fondo no encontrado"));
        mockMvc.perform(put("/api/funds/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedFund)))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteFund_ReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/api/funds/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteFund_ReturnsNotFound() throws Exception {
        doThrow(new RuntimeException()).when(fundService).deleteFund(anyString());
        mockMvc.perform(delete("/api/funds/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void updateFund_ReturnsOk_WhenUpdateIsSuccessful() throws Exception {
        FundDTO fundDTO = new FundDTO();
        fundDTO.setId("1");
        fundDTO.setName("Updated Fund");
        fundDTO.setMinimumInvestment(1000.0);
        fundDTO.setCategory("Growth");
        when(fundService.saveFund(any(FundDTO.class))).thenReturn(fundDTO);
        mockMvc.perform(put("/api/funds/{id}", "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(fundDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Fund"));
    }

}
