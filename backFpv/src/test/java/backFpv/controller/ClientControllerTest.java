package backFpv.controller;

import backFpv.dto.ClientDTO;
import backFpv.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
public class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllClients_ReturnsClientListWithStatusOk() throws Exception {
        ClientDTO client1 = new ClientDTO();
        client1.setId("1");
        client1.setName("John Doe");
        client1.setEmail("john.doe@example.com");
        ClientDTO client2 = new ClientDTO();
        client2.setId("2");
        client2.setName("Jane Doe");
        client2.setEmail("jane.doe@example.com");
        List<ClientDTO> clients = Arrays.asList(client1, client2);
        when(clientService.getAllClients()).thenReturn(clients);
        mockMvc.perform(get("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(clients.size()))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].name").value("Jane Doe"));
    }

    @Test
    void getClientById_ReturnsClientWithStatusOk() throws Exception {
        String clientId = "1";
        ClientDTO client = new ClientDTO();
        client.setId(clientId);
        client.setName("John Doe");
        client.setEmail("john.doe@example.com");
        when(clientService.getClientById(clientId)).thenReturn(client);
        mockMvc.perform(get("/api/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void getClientById_ReturnsNotFound() throws Exception {
        String clientId = "1";
        when(clientService.getClientById(clientId)).thenThrow(new RuntimeException("Client not found"));
        mockMvc.perform(get("/api/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void saveClient_ReturnsCreatedClientWithStatusCreated() throws Exception {
        ClientDTO clientToSave = new ClientDTO();
        clientToSave.setName("John Doe");
        clientToSave.setEmail("john.doe@example.com");
        ClientDTO savedClient = new ClientDTO();
        savedClient.setId("1");
        savedClient.setName("John Doe");
        savedClient.setEmail("john.doe@example.com");
        when(clientService.saveClient(any(ClientDTO.class))).thenReturn(savedClient);
        mockMvc.perform(post("/api/clients")
                        .content(objectMapper.writeValueAsString(clientToSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));
    }

    @Test
    void saveClient_ReturnsInternalServerError() throws Exception {
        ClientDTO clientToSave = new ClientDTO();
        clientToSave.setName("John Doe");
        clientToSave.setEmail("john.doe@example.com");
        when(clientService.saveClient(any(ClientDTO.class))).thenThrow(new RuntimeException("Internal error"));
        mockMvc.perform(post("/api/clients")
                        .content(objectMapper.writeValueAsString(clientToSave))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void updateClient_ReturnsUpdatedClientWithStatusOk() throws Exception {
        ClientDTO clientToUpdate = new ClientDTO();
        clientToUpdate.setId("1");
        clientToUpdate.setName("John Doe Updated");
        clientToUpdate.setEmail("john.updated@example.com");
        ClientDTO updatedClient = new ClientDTO();
        updatedClient.setId("1");
        updatedClient.setName("John Doe Updated");
        updatedClient.setEmail("john.updated@example.com");
        when(clientService.saveClient(any(ClientDTO.class))).thenReturn(updatedClient);
        mockMvc.perform(put("/api/clients/1")
                        .content(objectMapper.writeValueAsString(clientToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"));
    }

    @Test
    void updateClient_ReturnsNotFound() throws Exception {
        ClientDTO clientToUpdate = new ClientDTO();
        clientToUpdate.setId("1");
        clientToUpdate.setName("John Doe Updated");
        clientToUpdate.setEmail("john.updated@example.com");
        doThrow(new RuntimeException("Cliente no encontrado")).when(clientService).saveClient(any(ClientDTO.class));
        mockMvc.perform(put("/api/clients/1")
                        .content(objectMapper.writeValueAsString(clientToUpdate))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }


    @Test
    void deleteClient_ReturnsNoContent() throws Exception {
        String clientId = "1";
        doNothing().when(clientService).deleteClient(clientId);
        mockMvc.perform(delete("/api/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteClient_ReturnsNotFound() throws Exception {
        String clientId = "1";
        doThrow(new RuntimeException("Client not found")).when(clientService).deleteClient(clientId);
        mockMvc.perform(delete("/api/clients/{id}", clientId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
