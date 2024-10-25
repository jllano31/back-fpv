package backFpv.service;

import backFpv.dto.ClientDTO;
import backFpv.dto.TransactionDTO;
import backFpv.model.Client;
import backFpv.model.Transaction;
import backFpv.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private ClientService clientService;

    @Autowired
    private EmailService emailService;

    public TransactionDTO subscribeToFund(TransactionDTO transactionDTO) {
        try {
            transactionDTO.setTransactionType("subscription");
            transactionDTO.setTransactionDate(LocalDateTime.now());
            Transaction transaction = convertToEntity(transactionDTO);
            ClientDTO client = clientService.getClientById(transactionDTO.getClientId());
            Transaction savedTransaction = transactionRepository.save(transaction);
            String subject = "Confirmación de Suscripción al Fondo";
            String text = "Estimado " + client.getName() + ", se ha realizado su suscripción al fondo con éxito. " +
                    "Monto: " + transactionDTO.getAmount();
            emailService.sendEmail(client.getEmail(), subject, text);

            return convertToDTO(savedTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Error al suscribir al cliente al fondo: " + e.getMessage());
        }
    }

    public TransactionDTO cancelSubscription(TransactionDTO transactionDTO) {
        try {
            transactionDTO.setTransactionType("cancellation");
            transactionDTO.setTransactionDate(LocalDateTime.now());
            Transaction transaction = convertToEntity(transactionDTO);
            ClientDTO client = clientService.getClientById(transactionDTO.getClientId());
            Transaction savedTransaction = transactionRepository.save(transaction);
            String subject = "Confirmación de Cancelación de Suscripción";
            String text = "Estimado " + client.getName() + ", se ha realizado la cancelación de su suscripción al fondo. " +
                    "Monto devuelto: " + transactionDTO.getAmount();
            emailService.sendEmail(client.getEmail(), subject, text);
            return convertToDTO(savedTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Error al cancelar la suscripción del cliente al fondo: " + e.getMessage());
        }
    }

    public List<TransactionDTO> getTransactionsByClientId(String clientId) {
        try {
            List<Transaction> transactions = transactionRepository.findByClientId(clientId);
            return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las transacciones del cliente con ID: " + clientId + ". Error: " + e.getMessage());
        }
    }

    public List<TransactionDTO> getTransactionsByFundId(String fundId) {
        try {
            List<Transaction> transactions = transactionRepository.findByFundId(fundId);
            return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las transacciones del fondo con ID: " + fundId + ". Error: " + e.getMessage());
        }
    }

    private TransactionDTO convertToDTO(Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setClientId(transaction.getClientId());
        transactionDTO.setFundId(transaction.getFundId());
        transactionDTO.setTransactionType(transaction.getTransactionType());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setTransactionDate(transaction.getTransactionDate());
        return transactionDTO;
    }

    private Transaction convertToEntity(TransactionDTO transactionDTO) {
        Transaction transaction = new Transaction();
        transaction.setId(transactionDTO.getId());
        transaction.setClientId(transactionDTO.getClientId());
        transaction.setFundId(transactionDTO.getFundId());
        transaction.setTransactionType(transactionDTO.getTransactionType());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setTransactionDate(transactionDTO.getTransactionDate());
        return transaction;
    }
}
