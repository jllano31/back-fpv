package backFpv.service;

import backFpv.dto.ClientDTO;
import backFpv.dto.FundSubscribedDTO;
import backFpv.model.Client;
import backFpv.model.FundSubscribed;
import backFpv.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

public class ClientServiceTest {

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllClients() {
        ClientDTO clientDTO = new ClientDTO();
        List<ClientDTO> clientDTOS = new ArrayList<>();
        List<Client> clients = new ArrayList<>();
        Client client = new Client();
        client.setId("123test");
        client.setEmail("test@gmail.com");
        client.setPhoneNumber("123456789");
        client.setName("test");
        client.setAvailableBalance(0);
        client.setSubscribedFunds(new ArrayList<>());
        clients.add(client);
        when(clientRepository.findAll()).thenReturn(clients);
        List<ClientDTO> allClients = clientService.getAllClients();
        clientDTO.setId(allClients.get(0).getId());
        clientDTO.setEmail(allClients.get(0).getEmail());
        clientDTO.setPhoneNumber(allClients.get(0).getPhoneNumber());
        clientDTO.setName(allClients.get(0).getName());
        clientDTO.setAvailableBalance(allClients.get(0).getAvailableBalance());
        clientDTO.setSubscribedFunds(allClients.get(0).getSubscribedFunds());
        clientDTOS.add(clientDTO);

        verify(clientRepository, times(1)).findAll();
    }

    @Test
    void testCatchGetAllClients() {
        when(clientRepository.findAll()).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.getAllClients());
        assertEquals("Error al recuperar los clientes: Database error", exception.getMessage());
    }

    @Test
    void testSaveClient_Success() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId("1");
        clientDTO.setAvailableBalance(200.0);
        Client client = new Client();
        client.setId("1");
        client.setAvailableBalance(200.0);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        ClientDTO result = clientService.saveClient(clientDTO);
        assertEquals(200.0, result.getAvailableBalance());
    }

    @Test
    void testSaveClient_SetInitialBalance() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(null);
        clientDTO.setSubscribedFunds(new ArrayList<>());
        Client client = new Client();
        client.setId("2");
        client.setAvailableBalance(500.0);
        when(clientRepository.save(any(Client.class))).thenReturn(client);
        ClientDTO result = clientService.saveClient(clientDTO);
        assertEquals(500.0, result.getAvailableBalance());
    }

    @Test
    void testSaveClient_Exception() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId("3");
        when(clientRepository.save(any(Client.class))).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.saveClient(clientDTO));
        assertEquals("Error al guardar/actualizar el cliente: Database error", exception.getMessage());
    }

    @Test
    void testGetClientById_Success() {
        Client client = new Client();
        client.setId("1");
        client.setName("John Doe");
        when(clientRepository.findById("1")).thenReturn(Optional.of(client));
        ClientDTO result = clientService.getClientById("1");
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(clientRepository, times(1)).findById("1");
    }

    @Test
    void testGetClientById_ClientNotFound() {
        when(clientRepository.findById("2")).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.getClientById("2"));
        assertEquals("Error al recuperar el cliente con ID: 2. Error: No se encuentra un cliente con el ID: 2", exception.getMessage());
        verify(clientRepository, times(1)).findById("2");
    }

    @Test
    void testGetClientById_Exception() {
        when(clientRepository.findById("3")).thenThrow(new RuntimeException("Database error"));
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.getClientById("3"));
        assertEquals("Error al recuperar el cliente con ID: 3. Error: Database error", exception.getMessage());
        verify(clientRepository, times(1)).findById("3");
    }

    @Test
    void testDeleteClient_Success() {
        doNothing().when(clientRepository).deleteById("1");
        clientService.deleteClient("1");
        verify(clientRepository, times(1)).deleteById("1");
    }

    @Test
    void testDeleteClient_Exception() {
        doThrow(new RuntimeException("Database error")).when(clientRepository).deleteById("2");
        Exception exception = assertThrows(RuntimeException.class, () -> clientService.deleteClient("2"));
        assertEquals("Error al eliminar el cliente con ID: 2. Error: Database error", exception.getMessage());
        verify(clientRepository, times(1)).deleteById("2");
    }

    @Test
    void testConvertToDTO_WithNullSubscribedFunds() {
        Client client = new Client();
        client.setId("1");
        client.setName("John Doe");
        client.setAvailableBalance(500.0);
        client.setSubscribedFunds(null);
        ClientDTO clientDTO = clientService.convertToDTO(client);
        assertNotNull(clientDTO);
        assertEquals("1", clientDTO.getId());
        assertEquals("John Doe", clientDTO.getName());
        assertEquals(500.0, clientDTO.getAvailableBalance());
        assertNotNull(clientDTO.getSubscribedFunds());
        assertTrue(clientDTO.getSubscribedFunds().isEmpty());
    }

    @Test
    void testConvertToDTO_WithSubscribedFunds() {
        Client client = new Client();
        client.setId("2");
        client.setName("Jane Doe");
        client.setAvailableBalance(1000.0);
        FundSubscribed fundSubscribed = new FundSubscribed();
        fundSubscribed.setFundId("F1");
        fundSubscribed.setFundName("Sample Fund");
        fundSubscribed.setInvestedAmount(100.0);
        fundSubscribed.setSubscriptionDate("2024-10-24");
        List<FundSubscribed> funds = new ArrayList<>();
        funds.add(fundSubscribed);
        client.setSubscribedFunds(funds);
        ClientDTO clientDTO = clientService.convertToDTO(client);
        assertNotNull(clientDTO);
        assertEquals("2", clientDTO.getId());
        assertEquals("Jane Doe", clientDTO.getName());
        assertEquals(1000.0, clientDTO.getAvailableBalance());
        assertNotNull(clientDTO.getSubscribedFunds());
        assertEquals(1, clientDTO.getSubscribedFunds().size());
        FundSubscribedDTO dto = clientDTO.getSubscribedFunds().get(0);
        assertEquals("F1", dto.getFundId());
        assertEquals("Sample Fund", dto.getFundName());
        assertEquals(100.0, dto.getInvestedAmount());
        assertEquals("2024-10-24", dto.getSubscriptionDate());
    }

    @Test
    void testConvertToEntity_WithSubscribedFunds() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId("1");
        clientDTO.setName("John Doe");
        clientDTO.setAvailableBalance(500.0);
        clientDTO.setEmail("john@example.com");
        clientDTO.setPhoneNumber("1234567890");
        FundSubscribedDTO fundDTO = new FundSubscribedDTO();
        fundDTO.setFundId("F1");
        fundDTO.setFundName("Sample Fund");
        fundDTO.setInvestedAmount(100.0);
        fundDTO.setSubscriptionDate("2024-10-24");
        List<FundSubscribedDTO> subscribedFundsDTO = new ArrayList<>();
        subscribedFundsDTO.add(fundDTO);
        clientDTO.setSubscribedFunds(subscribedFundsDTO);
        Client client = clientService.convertToEntity(clientDTO);
        assertNotNull(client);
        assertEquals("1", client.getId());
        assertEquals("John Doe", client.getName());
        assertEquals(500.0, client.getAvailableBalance());
        assertEquals("john@example.com", client.getEmail());
        assertEquals("1234567890", client.getPhoneNumber());
        assertNotNull(client.getSubscribedFunds());
        assertEquals(1, client.getSubscribedFunds().size());
        FundSubscribed fund = client.getSubscribedFunds().get(0);
        assertEquals("F1", fund.getFundId());
        assertEquals("Sample Fund", fund.getFundName());
        assertEquals(100.0, fund.getInvestedAmount());
        assertEquals("2024-10-24", fund.getSubscriptionDate());
    }

    @Test
    void testConvertToEntity_WithNullSubscribedFunds() {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId("2");
        clientDTO.setName("Jane Doe");
        clientDTO.setAvailableBalance(1000.0);
        clientDTO.setEmail("jane@example.com");
        clientDTO.setPhoneNumber("0987654321");
        clientDTO.setSubscribedFunds(null);
        Client client = clientService.convertToEntity(clientDTO);
        assertNotNull(client);
        assertEquals("2", client.getId());
        assertEquals("Jane Doe", client.getName());
        assertEquals(1000.0, client.getAvailableBalance());
        assertEquals("jane@example.com", client.getEmail());
        assertEquals("0987654321", client.getPhoneNumber());
        assertNotNull(client.getSubscribedFunds());
        assertTrue(client.getSubscribedFunds().isEmpty());
    }

    @Test
    void getClientByUserName_ReturnsClientDTO_WhenClientExists() {
        String userName = "testUser";
        Client client = new Client();
        client.setUserName(userName);
        when(clientRepository.findByUserName(userName)).thenReturn(Optional.of(client));
        ClientDTO result = clientService.getClientByUserName(userName);
        assertEquals(userName, result.getUserName());
        verify(clientRepository, times(1)).findByUserName(userName);
    }

    @Test
    void getClientByUserName_ThrowsException_WhenClientNotFound() {
        String userName = "nonExistentUser";
        when(clientRepository.findByUserName(userName)).thenReturn(Optional.empty());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.getClientByUserName(userName));
        assertEquals("Error al recuperar el cliente con usuario: " + userName + ". Error: No se encuentra un cliente con el usuario: " + userName, exception.getMessage());
        verify(clientRepository, times(1)).findByUserName(userName);
    }

    @Test
    void getClientByUserName_ThrowsException_WhenRepositoryThrowsException() {
        String userName = "errorUser";
        when(clientRepository.findByUserName(userName)).thenThrow(new RuntimeException("Repository error"));
        RuntimeException exception = assertThrows(RuntimeException.class, () -> clientService.getClientByUserName(userName));
        assertEquals("Error al recuperar el cliente con usuario: " + userName + ". Error: Repository error", exception.getMessage());
        verify(clientRepository, times(1)).findByUserName(userName);
    }
}
