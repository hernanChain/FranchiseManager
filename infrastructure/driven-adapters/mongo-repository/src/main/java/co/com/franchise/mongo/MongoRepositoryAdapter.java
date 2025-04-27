package co.com.franchise.mongo;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.exceptions.FranchiseNotFoundException;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import co.com.franchise.mongo.helper.AdapterOperations;
import co.com.franchise.mongo.model.FranchiseMongoDBModel;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.data.domain.Example;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class MongoRepositoryAdapter extends AdapterOperations<Franchise, FranchiseMongoDBModel, String, MongoDBRepository> implements FranchiseRepository
{

    public MongoRepositoryAdapter(MongoDBRepository repository, ObjectMapper mapper) {
        /**
         *  Could be use mapper.mapBuilder if your domain model implement builder pattern
         *  super(repository, mapper, d -> mapper.mapBuilder(d,ObjectModel.ObjectModelBuilder.class).build());
         *  Or using mapper.map with the class of the object model
         */
        super(repository, mapper, d -> mapper.map(d, Franchise.class/* change for domain model */));
    }

    @Override
    public Mono<Franchise> findFranchiseByName(String franchiseName) {
        return repository.findByName(franchiseName)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                .map(franchiseMongoDBModel -> mapper.map(franchiseMongoDBModel, Franchise.class));
    }

    @Override
    public Mono<Franchise> saveFranchise(Franchise franchise) {
        return this.save(franchise);
    }

    @Override
    public Mono<Void> deleteFranchise(Franchise franchise) {
        FranchiseMongoDBModel model = mapper.map(franchise, FranchiseMongoDBModel.class);
        return this.repository.delete(model);
    }


}
