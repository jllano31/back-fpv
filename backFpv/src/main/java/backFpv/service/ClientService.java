package backFpv.service;

import backFpv.dto.ClientDTO;
import backFpv.dto.FundSubscribedDTO;
import backFpv.model.Client;
import backFpv.model.FundSubscribed;
import backFpv.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar las operaciones relacionadas con los clientes,
 * incluyendo obtención, creación, actualización y eliminación.
 */
@Service
public class ClientService {

    /** Repositorio de clientes para operaciones de base de datos. */
    @Autowired
    private ClientRepository clientRepository;

    /** Saldo inicial asignado a un cliente nuevo. */
    @Value("${app.client.initial-balance}")
    private double initialBalance;

    /**
     * Obtener todos los clientes registrados.
     *
     * @return Lista de objetos ClientDTO.
     */
    public List<ClientDTO> getAllClients() {
        try {
            List<Client> clients = clientRepository.findAll();
            return clients.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar los clientes: " + e.getMessage());
        }
    }

    /**
     * Obtener un cliente por su ID.
     *
     * @param id ID del cliente.
     * @return ClientDTO correspondiente al cliente.
     */
    public ClientDTO getClientById(String id) {
        try {
            Optional<Client> clientOpt = clientRepository.findById(id);
            if (clientOpt.isPresent()) {
                return convertToDTO(clientOpt.get());
            } else {
                throw new RuntimeException("No se encuentra un cliente con el ID: " + id);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar el cliente con ID: " + id + ". Error: " + e.getMessage());
        }
    }

    /**
     * Obtener un cliente por su ID.
     *
     * @param userName usuario del cliente.
     * @return ClientDTO correspondiente al cliente.
     */
    public ClientDTO getClientByUserName(String userName) {
        try {
            Optional<Client> clientOpt = clientRepository.findByUserName(userName);
            if (clientOpt.isPresent()) {
                return convertToDTO(clientOpt.get());
            } else {
                throw new RuntimeException("No se encuentra un cliente con el usuario: " + userName);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar el cliente con usuario: " + userName + ". Error: " +
                    e.getMessage());
        }
    }

    /**
     * Crear o actualizar un cliente.
     *
     * @param clientDTO Objeto ClientDTO con los datos del cliente.
     * @return ClientDTO correspondiente al cliente creado o actualizado.
     */
    public ClientDTO saveClient(ClientDTO clientDTO) {
        try {
            Client client = convertToEntity(clientDTO);
            if (client.getId() == null || client.getAvailableBalance() == 0) {
                client.setAvailableBalance(initialBalance);
            }
            Client savedClient = clientRepository.save(client);
            return convertToDTO(savedClient);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar/actualizar el cliente: " + e.getMessage());
        }
    }

    /**
     * Eliminar un cliente por su ID.
     *
     * @param id ID del cliente a eliminar.
     */
    public void deleteClient(String id) {
        try {
            clientRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cliente con ID: " + id + ". Error: " + e.getMessage());
        }
    }

    /**
     * Convertir un objeto Client a ClientDTO.
     *
     * @param client Objeto Client a convertir.
     * @return Objeto ClientDTO.
     */
    ClientDTO convertToDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());
        clientDTO.setAvailableBalance(client.getAvailableBalance());
        List<FundSubscribedDTO> subscribedFundsDTO = client.getSubscribedFunds() != null
                ? client.getSubscribedFunds().stream()
                .map(fund -> {
                    FundSubscribedDTO dto = new FundSubscribedDTO();
                    dto.setFundId(fund.getFundId());
                    dto.setFundName(fund.getFundName());
                    dto.setInvestedAmount(fund.getInvestedAmount());
                    dto.setSubscriptionDate(fund.getSubscriptionDate());
                    return dto;
                })
                .collect(Collectors.toList())
                : new ArrayList<>();

        clientDTO.setSubscribedFunds(subscribedFundsDTO);
        clientDTO.setEmail(client.getEmail());
        clientDTO.setPhoneNumber(client.getPhoneNumber());
        clientDTO.setUserName(client.getUserName());
        return clientDTO;
    }

    /**
     * Convertir un objeto ClientDTO a Client.
     *
     * @param clientDTO Objeto ClientDTO a convertir.
     * @return Objeto Client.
     */
    Client convertToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setName(clientDTO.getName());
        client.setAvailableBalance(clientDTO.getAvailableBalance());
        client.setEmail(clientDTO.getEmail());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        if (clientDTO.getSubscribedFunds() != null) {
            List<FundSubscribed> subscribedFunds = clientDTO.getSubscribedFunds().stream()
                    .map(fundDTO -> {
                        FundSubscribed fund = new FundSubscribed();
                        fund.setFundId(fundDTO.getFundId());
                        fund.setFundName(fundDTO.getFundName());
                        fund.setInvestedAmount(fundDTO.getInvestedAmount());
                        fund.setSubscriptionDate(fundDTO.getSubscriptionDate());
                        return fund;
                    })
                    .collect(Collectors.toList());
            client.setSubscribedFunds(subscribedFunds);
        } else {
            client.setSubscribedFunds(new ArrayList<>());  // Lista vacía si es nula
        }
        client.setUserName(clientDTO.getUserName());
        return client;
    }
}
