package backFpv.repository;

import backFpv.model.Fund;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface FundRepository extends MongoRepository<Fund, String> {
    Optional<Fund> findByName(String name);
}
