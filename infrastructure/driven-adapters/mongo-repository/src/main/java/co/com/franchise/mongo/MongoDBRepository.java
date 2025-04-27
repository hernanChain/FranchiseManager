package co.com.franchise.mongo;

import co.com.franchise.mongo.model.FranchiseMongoDBModel;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import reactor.core.publisher.Mono;

public interface MongoDBRepository extends ReactiveMongoRepository<FranchiseMongoDBModel, String>, ReactiveQueryByExampleExecutor<FranchiseMongoDBModel> {

    Mono<FranchiseMongoDBModel> findByName(String franchiseName);
}
