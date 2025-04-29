package co.com.franchise.mongo;

import co.com.franchise.model.exceptions.FranchiseNotFoundException;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.mongo.model.FranchiseMongoDBModel;
import org.reactivecommons.utils.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MongoRepositoryAdapterTest {

    private MongoDBRepository mongoDBRepository;
    private ObjectMapper mapper;
    private MongoRepositoryAdapter mongoRepositoryAdapter;


    @BeforeEach
    void setUp() {
        mongoDBRepository = mock(MongoDBRepository.class);
        mapper = mock(ObjectMapper.class);
        mongoRepositoryAdapter = new MongoRepositoryAdapter(mongoDBRepository, mapper);
    }

    @Test
    void findFranchiseByName_ShouldReturnFranchise_WhenExists() {
        String franchiseName = "TestFranchise";
        FranchiseMongoDBModel mongoModel = new FranchiseMongoDBModel();
        Franchise franchise = new Franchise();

        when(mongoDBRepository.findByName(franchiseName)).thenReturn(Mono.just(mongoModel));
        when(mapper.map(mongoModel, Franchise.class)).thenReturn(franchise);

        Mono<Franchise> result = mongoRepositoryAdapter.findFranchiseByName(franchiseName);

        StepVerifier.create(result)
                .expectNext(franchise)
                .verifyComplete();

        verify(mongoDBRepository).findByName(franchiseName);
        verify(mapper).map(mongoModel, Franchise.class);
    }

    @Test
    void findFranchiseByName_ShouldThrowException_WhenNotFound() {
        // Arrange
        String franchiseName = "NonExistentFranchise";

        when(mongoDBRepository.findByName(franchiseName)).thenReturn(Mono.empty());

        Mono<Franchise> result = mongoRepositoryAdapter.findFranchiseByName(franchiseName);

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(mongoDBRepository).findByName(franchiseName);
    }

    @Test
    void saveFranchise_ShouldSaveFranchiseSuccessfully() {
        Franchise franchise = new Franchise();

        MongoRepositoryAdapter spyAdapter = spy(mongoRepositoryAdapter);
        doReturn(Mono.just(franchise)).when(spyAdapter).save(franchise);

        Mono<Franchise> result = spyAdapter.saveFranchise(franchise);

        StepVerifier.create(result)
                .expectNext(franchise)
                .verifyComplete();

        verify(spyAdapter).save(franchise);
    }

    @Test
    void deleteFranchise_ShouldDeleteFranchiseSuccessfully() {
        // Arrange
        Franchise franchise = new Franchise();
        FranchiseMongoDBModel model = new FranchiseMongoDBModel();

        when(mapper.map(franchise, FranchiseMongoDBModel.class)).thenReturn(model);
        when(mongoDBRepository.delete(model)).thenReturn(Mono.empty());

        // Act
        Mono<Void> result = mongoRepositoryAdapter.deleteFranchise(franchise);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();

        verify(mapper).map(franchise, FranchiseMongoDBModel.class);
        verify(mongoDBRepository).delete(model);
    }

}