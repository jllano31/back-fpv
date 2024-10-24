package backFpv.repository;

import backFpv.model.Client;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface ClientRepository extends MongoRepository<Client, String> {
    // Método adicional para encontrar un cliente por su nombre
    Optional<Client> findByName(String name);
}
