package backFpv.service;

import backFpv.dto.ClientDTO;
import backFpv.dto.FundDTO;
import backFpv.dto.TransactionDTO;
import backFpv.exception.InsufficientBalanceException;
import backFpv.model.Client;
import backFpv.model.Transaction;
import backFpv.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar operaciones de transacciones, incluyendo suscripciones,
 * cancelaciones y consultas de transacciones.
 */
@Service
public class TransactionService {

    /** Repositorio de transacciones para operaciones de base de datos. */
    @Autowired
    private TransactionRepository transactionRepository;

    /** Servicio de cliente para gestionar información del cliente. */
    @Autowired
    private ClientService clientService;

    /** Servicio de correo electrónico para enviar notificaciones. */
    @Autowired
    private EmailService emailService;

    /** Servicio de fondos para gestionar información de los fondos. */
    @Autowired
    private FundService fundService;

    /**
     * Suscribir a un cliente a un fondo.
     *
     * @param transactionDTO Detalles de la transacción de suscripción.
     * @return Detalles de la transacción suscrita.
     */
    public TransactionDTO subscribeToFund(TransactionDTO transactionDTO) throws InsufficientBalanceException {
        try {
            transactionDTO.setTransactionType("subscription");
            transactionDTO.setTransactionDate(LocalDateTime.now());
            Transaction transaction = convertToEntity(transactionDTO);
            ClientDTO client = clientService.getClientById(transactionDTO.getClientId());
            FundDTO fund = fundService.getFundById(transactionDTO.getFundId());
            if (client.getAvailableBalance() < fund.getMinimumInvestment() ||
                    transactionDTO.getAmount() > client.getAvailableBalance()) {
                throw new InsufficientBalanceException("No tiene saldo disponible para vincularse al fondo " +
                        fund.getName());
            }
            client.setAvailableBalance(client.getAvailableBalance() - transactionDTO.getAmount());
            clientService.saveClient(client);
            Transaction savedTransaction = transactionRepository.save(transaction);
            emailService.sendEmail(client.getEmail(), "Confirmación de Suscripción al Fondo",
                    "Estimado " + client.getName() +
                            ", se ha realizado su suscripción al fondo con éxito. Monto: " +
                            transactionDTO.getAmount());
            return convertToDTO(savedTransaction);
        } catch (InsufficientBalanceException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error al suscribir al cliente al fondo: " + e.getMessage(), e);
        }
    }

    /**
     * Cancelar la suscripción de un cliente a un fondo.
     *
     * @param transactionID ID de la transacción de suscripción a cancelar.
     * @return Detalles de la transacción cancelada.
     */
    public TransactionDTO cancelSubscription(String transactionID) {
        try {
            TransactionDTO transaction = getTransactionById(transactionID);
            transaction.setTransactionType("canceled");
            transaction.setTransactionDate(LocalDateTime.now());
            ClientDTO client = clientService.getClientById(transaction.getClientId());
            client.setAvailableBalance(client.getAvailableBalance() + transaction.getAmount());
            ClientDTO returnClient = clientService.saveClient(client);
            Transaction transactionEntity = convertToEntity(transaction);
            Transaction savedTransaction = transactionRepository.save(transactionEntity);
            String subject = "Confirmación de Cancelación de Suscripción";
            String text = "Estimado " + client.getName() +
                    ", se ha realizado la cancelación de su suscripción al fondo. " +
                    "Monto devuelto: " + transaction.getAmount();
            emailService.sendEmail(client.getEmail(), subject, text);
            return convertToDTO(savedTransaction);
        } catch (Exception e) {
            throw new RuntimeException("Error al cancelar la suscripción del cliente al fondo: " + e.getMessage());
        }
    }

    /**
     * Obtener una transacción por su ID.
     *
     * @param transactionId ID de la transacción a obtener.
     * @return Detalles de la transacción.
     */
    public TransactionDTO getTransactionById(String transactionId) {
        try {
            Transaction transaction = transactionRepository.findById(transactionId)
                    .orElseThrow(() -> new RuntimeException("Transacción no encontrada con ID: " + transactionId));
            return convertToDTO(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar la transacción con ID: " + transactionId +
                    ". Error: " + e.getMessage());
        }
    }

    /**
     * Obtener todas las transacciones de un cliente.
     *
     * @param clientId ID del cliente.
     * @return Lista de transacciones del cliente.
     */
    public List<TransactionDTO> getTransactionsByClientId(String clientId) {
        try {
            List<Transaction> transactions = transactionRepository.findByClientId(clientId);
            return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las transacciones del cliente con ID: " + clientId +
                    ". Error: " + e.getMessage());
        }
    }

    /**
     * Obtener todas las transacciones de un fondo.
     *
     * @param fundId ID del fondo.
     * @return Lista de transacciones del fondo.
     */
    public List<TransactionDTO> getTransactionsByFundId(String fundId) {
        try {
            List<Transaction> transactions = transactionRepository.findByFundId(fundId);
            return transactions.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al obtener las transacciones del fondo con ID: " + fundId +
                    ". Error: " + e.getMessage());
        }
    }

    /**
     * Convertir un objeto Transaction a TransactionDTO.
     *
     * @param transaction Objeto Transaction a convertir.
     * @return Objeto TransactionDTO.
     */
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

    /**
     * Convertir un objeto TransactionDTO a Transaction.
     *
     * @param transactionDTO Objeto TransactionDTO a convertir.
     * @return Objeto Transaction.
     */
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
