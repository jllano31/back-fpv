package backFpv.repository;

import backFpv.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {

    List<Transaction> findByClientId(String clientId);

    List<Transaction> findByFundId(String fundId);
}
