package backFpv.service;

import backFpv.dto.ClientDTO;
import backFpv.dto.FundSubscribedDTO;
import backFpv.model.Client;
import backFpv.model.FundSubscribed;
import backFpv.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    // Obtener todos los clientes
    public List<ClientDTO> getAllClients() {
        try{
            List<Client> clients = clientRepository.findAll();
            return clients.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error al recuperar los clientes: "+e.getMessage());
        }

    }

    // Obtener un cliente por su ID
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

    // Crear o actualizar un cliente
    public ClientDTO saveClient(ClientDTO clientDTO) {
        try {
            Client client = convertToEntity(clientDTO);
            Client savedClient = clientRepository.save(client);
            return convertToDTO(savedClient);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar/actualizar el cliente: " + e.getMessage());
        }
    }

    // Eliminar un cliente por su ID
    public void deleteClient(String id) {
        try {
            clientRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el cliente con ID: " + id + ". Error: " + e.getMessage());
        }
    }

    // Convertir client a clientDTO
    private ClientDTO convertToDTO(Client client) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(client.getId());
        clientDTO.setName(client.getName());
        clientDTO.setAvailableBalance(client.getAvailableBalance());

        List<FundSubscribedDTO> subscribedFundsDTO = client.getSubscribedFunds().stream()
                .map(fund -> {
                    FundSubscribedDTO dto = new FundSubscribedDTO();
                    dto.setFundId(fund.getFundId());
                    dto.setFundName(fund.getFundName());
                    dto.setInvestedAmount(fund.getInvestedAmount());
                    dto.setSubscriptionDate(fund.getSubscriptionDate());
                    return dto;
                })
                .collect(Collectors.toList());

        clientDTO.setSubscribedFunds(subscribedFundsDTO);
        return clientDTO;
    }

    // Metodo para convertir ClientDTo a Client
    private Client convertToEntity(ClientDTO clientDTO) {
        Client client = new Client();
        client.setId(clientDTO.getId());
        client.setName(clientDTO.getName());
        client.setAvailableBalance(clientDTO.getAvailableBalance());

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
        return client;
    }
}
