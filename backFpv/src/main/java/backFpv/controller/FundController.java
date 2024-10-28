package backFpv.controller;

import backFpv.dto.FundDTO;
import backFpv.service.FundService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    /**
     * Servicio de fondos utilizado para gestionar las operaciones de los fondos,
     * incluyendo creación, actualización, y eliminación de registros de los fondos.
     */
    @Autowired
    private FundService fundService;

    @GetMapping
    @Operation(summary = "Obtener todos los fondos",
            description = "Devuelve una lista de todos los fondos disponibles en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de fondos obtenida exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<List<FundDTO>> getAllFunds() {
        try {
            List<FundDTO> funds = fundService.getAllFunds();
            return new ResponseEntity<>(funds, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener fondo por ID", description = "Devuelve los detalles del fondo especificado por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fondo encontrado"),
            @ApiResponse(responseCode = "404", description = "Fondo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<FundDTO> getFundById(@PathVariable String id) {
        try {
            FundDTO fund = fundService.getFundById(id);
            return new ResponseEntity<>(fund, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo fondo", description = "Crea y guarda un nuevo fondo en el sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Fondo creado exitosamente"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<FundDTO> createFund(@Valid @RequestBody FundDTO fundDTO) {
        try {
            FundDTO savedFund = fundService.saveFund(fundDTO);
            return new ResponseEntity<>(savedFund, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar fondo por ID",
            description = "Actualiza los detalles del fondo especificado por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Fondo actualizado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Fondo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
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
    @Operation(summary = "Eliminar fondo por ID", description = "Elimina el fondo especificado por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Fondo eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Fondo no encontrado"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<Void> deleteFund(@PathVariable String id) {
        try {
            fundService.deleteFund(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
