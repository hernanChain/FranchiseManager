package co.com.franchise.usecase.franchise;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class FranchiseUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private FranchiseUseCase franchiseUseCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        franchiseUseCase = new FranchiseUseCase(franchiseRepository);
    }

    @Test
    void createFranchise_ShouldReturnCreatedFranchise() {
        Franchise franchise = Franchise.builder().name("New Franchise").build();

        Mockito.when(franchiseRepository.saveFranchise(any()))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseUseCase.createFranchise(franchise);

        StepVerifier.create(result)
                .expectNextMatches(savedFranchise -> savedFranchise.getName().equals("New Franchise"))
                .verifyComplete();

        Mockito.verify(franchiseRepository).saveFranchise(any());
    }

    @Test
    void updateFranchiseName_ShouldUpdateNameAndDeleteOldFranchise() {
        Franchise oldFranchise = Franchise.builder()
                .name("OldName")
                .build();
        Franchise newFranchise = Franchise.builder()
                .name("NewName")
                .build();

        Mockito.when(franchiseRepository.findFranchiseByName("OldName"))
                .thenReturn(Mono.just(oldFranchise));

        Mockito.when(franchiseRepository.saveFranchise(any()))
                .thenReturn(Mono.just(newFranchise));

        Mockito.when(franchiseRepository.deleteFranchise(any()))
                .thenReturn(Mono.empty());

        Mono<Franchise> result = franchiseUseCase.updateFranchiseName("OldName", "NewName");

        StepVerifier.create(result)
                .expectNextMatches(franchise -> franchise.getName().equals("NewName"))
                .verifyComplete();

        Mockito.verify(franchiseRepository).findFranchiseByName("OldName");
        Mockito.verify(franchiseRepository).saveFranchise(any());
        Mockito.verify(franchiseRepository).deleteFranchise(any());
    }

    @Test
    void addBranchToFranchise_ShouldAddBranchAndReturnUpdatedFranchise() {
        Franchise franchise = Franchise.builder()
                .name("Franchise1")
                .branches(new ArrayList<>())
                .build();
        Branch newBranch = Branch.builder()
                .name("Branch1")
                .build();

        Franchise updatedFranchise = Franchise.builder()
                .name("Franchise1")
                .branches(List.of(newBranch))
                .build();

        Mockito.when(franchiseRepository.findFranchiseByName("Franchise1"))
                .thenReturn(Mono.just(franchise));

        Mockito.when(franchiseRepository.saveFranchise(any()))
                .thenReturn(Mono.just(updatedFranchise));

        Mono<Branch> result = franchiseUseCase.addBranchToFranchise("Franchise1", newBranch);

        StepVerifier.create(result)
                .expectNextMatches(branch -> branch.getName().equals("Branch1"))
                .verifyComplete();

        Mockito.verify(franchiseRepository).findFranchiseByName("Franchise1");
        Mockito.verify(franchiseRepository).saveFranchise(any());
    }

    @Test
    void getFranchise_ShouldReturnFranchise_WhenFound() {
        Franchise franchise = Franchise.builder()
                .name("Franchise1")
                .build();

        Mockito.when(franchiseRepository.findFranchiseByName("Franchise1"))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result = franchiseUseCase.getFranchise("Franchise1");

        StepVerifier.create(result)
                .expectNextMatches(foundFranchise -> foundFranchise.getName().equals("Franchise1"))
                .verifyComplete();

        Mockito.verify(franchiseRepository).findFranchiseByName("Franchise1");
    }

}