package backFpv.controller;

import backFpv.dto.TransactionDTO;
import backFpv.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Test
    public void subscribeToFund_ReturnsCreated_WhenTransactionIsSuccessful() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        when(transactionService.subscribeToFund(any(TransactionDTO.class))).thenReturn(transactionDTO);
        mockMvc.perform(post("/api/transactions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(transactionDTO)))
                .andExpect(status().isCreated());
    }

    @Test
    public void subscribeToFund_ReturnsInternalServerError_WhenException() throws Exception {
        when(transactionService.subscribeToFund(any(TransactionDTO.class))).thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(post("/api/transactions/subscribe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(new TransactionDTO())))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void cancelSubscription_ReturnsCreated_WhenCancellationIsSuccessful() throws Exception {
        TransactionDTO transactionDTO = new TransactionDTO();
        when(transactionService.cancelSubscription(anyString())).thenReturn(transactionDTO);
        mockMvc.perform(post("/api/transactions/cancel/1"))
                .andExpect(status().isCreated());
    }

    @Test
    public void cancelSubscription_ReturnsInternalServerError_WhenException() throws Exception {
        when(transactionService.cancelSubscription(anyString())).thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(post("/api/transactions/cancel/1"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    public void getTransactionsByClientId_ReturnsOk_WhenTransactionsFound() throws Exception {
        when(transactionService.getTransactionsByClientId(anyString())).thenReturn(List.of(new TransactionDTO()));
        mockMvc.perform(get("/api/transactions/client/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTransactionsByClientId_ReturnsInternalServerError_WhenException() throws Exception {
        when(transactionService.getTransactionsByClientId(anyString())).thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(get("/api/transactions/client/1"))
                .andExpect(status().isInternalServerError());
    }


    @Test
    public void getTransactionsByFundId_ReturnsOk_WhenTransactionsFound() throws Exception {
        when(transactionService.getTransactionsByFundId(anyString())).thenReturn(List.of(new TransactionDTO()));
        mockMvc.perform(get("/api/transactions/fund/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void getTransactionsByFundId_ReturnsInternalServerError_WhenException() throws Exception {
        when(transactionService.getTransactionsByFundId(anyString())).thenThrow(new RuntimeException("Unexpected error"));
        mockMvc.perform(get("/api/transactions/fund/{fundId}", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

}
