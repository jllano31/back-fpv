package backFpv.controller;

import backFpv.dto.TransactionDTO;
import backFpv.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    /**
     * Servicio de cliente utilizado para gestionar las operaciones de las transacciones,
     * incluyendo creación, actualización de las transacciones.
     */
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/subscribe")
    @Operation(summary = "Suscribirse a un fondo", description = "Crea una transacción de suscripción a un fondo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Suscripción creada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TransactionDTO> subscribeToFund(@RequestBody TransactionDTO transactionDTO) {
        try {
            TransactionDTO transaction = transactionService.subscribeToFund(transactionDTO);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel/{id}")
    @Operation(summary = "Cancelar suscripción a un fondo", description = "Cancela la suscripción a un fondo específica por ID de transacción")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Suscripción cancelada exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<TransactionDTO> cancelSubscription(@PathVariable String id) {
        try {
            TransactionDTO transaction = transactionService.cancelSubscription(id);
            return new ResponseEntity<>(transaction, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Obtener transacciones por ID de cliente", description = "Devuelve todas las transacciones asociadas a un cliente específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<TransactionDTO>> getTransactionsByClientId(@PathVariable String clientId) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByClientId(clientId);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/fund/{fundId}")
    @Operation(summary = "Obtener transacciones por ID de fondo", description = "Devuelve todas las transacciones asociadas a un fondo específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Transacciones obtenidas exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<TransactionDTO>> getTransactionsByFundId(@PathVariable String fundId) {
        try {
            List<TransactionDTO> transactions = transactionService.getTransactionsByFundId(fundId);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
