package backFpv.service;

import backFpv.dto.ClientDTO;
import backFpv.dto.FundDTO;
import backFpv.dto.TransactionDTO;
import backFpv.exception.InsufficientBalanceException;
import backFpv.model.Transaction;
import backFpv.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ClientService clientService;

    @Mock
    private EmailService emailService;

    @Mock
    private FundService fundService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSubscribeToFund_Success() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setClientId("C1");
        transactionDTO.setFundId("F1");
        transactionDTO.setAmount(200.0);
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setAvailableBalance(500.0);
        clientDTO.setName("John Doe");
        clientDTO.setEmail("john.doe@example.com");
        FundDTO fundDTO = new FundDTO();
        fundDTO.setMinimumInvestment(100.0);
        Transaction transaction = new Transaction();
        when(clientService.getClientById("C1")).thenReturn(clientDTO);
        when(fundService.getFundById("F1")).thenReturn(fundDTO);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        assertDoesNotThrow(() -> transactionService.subscribeToFund(transactionDTO));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(emailService, times(1)).sendEmail(eq("john.doe@example.com"), eq("Confirmación de Suscripción al Fondo"),
                eq("Estimado John Doe, se ha realizado su suscripción al fondo con éxito. Monto: 200.0"));
    }

    @Test
    void testSubscribeToFund_InsufficientBalance() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setClientId("C1");
        transactionDTO.setFundId("F1");
        transactionDTO.setAmount(500.0);
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setAvailableBalance(100.0);
        clientDTO.setName("John Doe");
        clientDTO.setEmail("john.doe@example.com");
        FundDTO fundDTO = new FundDTO();
        fundDTO.setMinimumInvestment(200.0);
        when(clientService.getClientById("C1")).thenReturn(clientDTO);
        when(fundService.getFundById("F1")).thenReturn(fundDTO);
        assertThrows(InsufficientBalanceException.class, () -> transactionService.subscribeToFund(transactionDTO));
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSubscribeToFund_Exception() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setClientId("C1");
        transactionDTO.setFundId("F1");
        transactionDTO.setAmount(200.0);
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setAvailableBalance(500.0);
        FundDTO fundDTO = new FundDTO();
        fundDTO.setMinimumInvestment(100.0);
        when(clientService.getClientById("C1")).thenReturn(clientDTO);
        when(fundService.getFundById("F1")).thenReturn(fundDTO);
        when(transactionRepository.save(any(Transaction.class))).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> transactionService.subscribeToFund(transactionDTO));
        assertTrue(exception.getMessage().contains("Error al suscribir al cliente al fondo"));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testSubscribeToFund_AmountExceedsBalance() {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setClientId("C1");
        transactionDTO.setFundId("F1");
        transactionDTO.setAmount(300.0);
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setAvailableBalance(100.0);
        clientDTO.setName("Jane Doe");
        clientDTO.setEmail("jane.doe@example.com");
        FundDTO fundDTO = new FundDTO();
        fundDTO.setMinimumInvestment(50.0);
        when(clientService.getClientById("C1")).thenReturn(clientDTO);
        when(fundService.getFundById("F1")).thenReturn(fundDTO);
        assertThrows(InsufficientBalanceException.class, () -> transactionService.subscribeToFund(transactionDTO));
        verify(transactionRepository, never()).save(any(Transaction.class));
        verify(emailService, never()).sendEmail(anyString(), anyString(), anyString());
    }

    @Test
    void testCancelSubscription_Success() {
        String transactionID = "T1";
        Transaction transactionEntity = new Transaction();
        transactionEntity.setId(transactionID);
        transactionEntity.setTransactionType("canceled");
        transactionEntity.setTransactionDate(LocalDateTime.now().minusDays(1));
        transactionEntity.setClientId("C1");
        transactionEntity.setAmount(100.0);
        transactionEntity.setFundId("1");
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transactionID);
        transactionDTO.setTransactionType("canceled");
        transactionDTO.setClientId("C1");
        transactionDTO.setAmount(100.0);
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId("C1");
        clientDTO.setAvailableBalance(50.0);
        clientDTO.setName("Jane Doe");
        clientDTO.setEmail("jane.doe@example.com");
        when(transactionRepository.findById(transactionID)).thenReturn(Optional.of(transactionEntity));
        when(clientService.getClientById("C1")).thenReturn(clientDTO);
        when(clientService.saveClient(any(ClientDTO.class))).thenReturn(clientDTO);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transactionEntity);
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());
        TransactionDTO result = transactionService.cancelSubscription(transactionID);
        assertEquals("canceled", result.getTransactionType());
        assertEquals(150.0, clientDTO.getAvailableBalance());
        verify(emailService, times(1)).sendEmail(eq(clientDTO.getEmail()), eq("Confirmación de Cancelación de Suscripción"),
                contains("Monto devuelto: " + transactionDTO.getAmount()));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }


    @Test
    void testCancelSubscription_Exception() {
        String transactionID = "T1";
        when(transactionRepository.findById(transactionID)).thenThrow(new RuntimeException("Transacción no encontrada"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.cancelSubscription(transactionID);
        });
        assertTrue(exception.getMessage().contains("Error al cancelar la suscripción del cliente al fondo"));
        assertTrue(exception.getMessage().contains("Transacción no encontrada"));
    }

    @Test
    void testGetTransactionsByClientId_Success() {
        String clientId = "C1";
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findByClientId(clientId)).thenReturn(transactions);
        List<TransactionDTO> result = transactionService.getTransactionsByClientId(clientId);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findByClientId(clientId);
    }

    @Test
    void testGetTransactionsByClientId_Exception() {
        String clientId = "C1";
        when(transactionRepository.findByClientId(clientId)).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.getTransactionsByClientId(clientId);
        });
        assertEquals("Error al obtener las transacciones del cliente con ID: C1. Error: Database error", exception.getMessage());
        verify(transactionRepository, times(1)).findByClientId(clientId);
    }

    @Test
    void testGetTransactionsByFundId_Success() {
        String fundId = "F1";
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> transactions = Arrays.asList(transaction1, transaction2);
        when(transactionRepository.findByFundId(fundId)).thenReturn(transactions);
        List<TransactionDTO> result = transactionService.getTransactionsByFundId(fundId);
        assertEquals(2, result.size());
        verify(transactionRepository, times(1)).findByFundId(fundId);
    }

    @Test
    void testGetTransactionsByFundId_Exception() {
        String fundId = "F1";
        when(transactionRepository.findByFundId(fundId)).thenThrow(new RuntimeException("Database error"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.getTransactionsByFundId(fundId);
        });
        assertEquals("Error al obtener las transacciones del fondo con ID: F1. Error: Database error", exception.getMessage());
        verify(transactionRepository, times(1)).findByFundId(fundId);
    }

    @Test
    void testGetTransactionById_ThrowsExceptionWhenTransactionNotFound() {
        String transactionId = "T1";
        when(transactionRepository.findById(transactionId)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            transactionService.getTransactionById(transactionId);
        });
        assertTrue(exception.getMessage().contains("Transacción no encontrada con ID: " + transactionId));
    }
}
