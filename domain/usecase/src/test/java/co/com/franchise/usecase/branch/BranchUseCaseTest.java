package co.com.franchise.usecase.branch;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import co.com.franchise.model.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

class BranchUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private BranchUseCase branchUseCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = Mockito.mock(FranchiseRepository.class);
        branchUseCase = new BranchUseCase(franchiseRepository);
    }

    @Test
    void addProductToBranch_ShouldAddProductSuccessfully() {
        Product product = Product.builder().name("Product1").build();
        Branch branch = Branch.builder().name("Branch1").products(new ArrayList<>()).build();
        Franchise franchise = Franchise.builder().name("Franchise1").branches(List.of(branch)).build();

        Mockito.when(franchiseRepository.findFranchiseByName("Franchise1"))
                .thenReturn(Mono.just(franchise));
        Mockito.when(franchiseRepository.saveFranchise(any()))
                .thenReturn(Mono.just(franchise));

        Mono<Franchise> result = branchUseCase.addProductToBranch("Franchise1", "Branch1", product);

        StepVerifier.create(result)
                .expectNextMatches(savedFranchise -> savedFranchise.getBranches().getFirst().getProducts().contains(product))
                .verifyComplete();

        Mockito.verify(franchiseRepository).findFranchiseByName("Franchise1");
        Mockito.verify(franchiseRepository).saveFranchise(any());
    }

    @Test
    void deleteProductFromBranch_ShouldDeleteProductSuccessfully() {
        Product product = Product.builder().name("Product1").build();
        Branch branch = Branch.builder().name("Branch1").products(new ArrayList<>(List.of(product))).build();
        Franchise franchise = Franchise.builder().name("Franchise1").branches(List.of(branch)).build();

        Mockito.when(franchiseRepository.findFranchiseByName("Franchise1"))
                .thenReturn(Mono.just(franchise));
        Mockito.when(franchiseRepository.saveFranchise(any()))
                .thenReturn(Mono.just(franchise));

        Mono<Branch> result = branchUseCase.deleteProductFromBranch("Franchise1", "Branch1", "Product1");

        StepVerifier.create(result)
                .expectNextMatches(updatedBranch -> updatedBranch.getProducts().isEmpty())
                .verifyComplete();

        Mockito.verify(franchiseRepository).findFranchiseByName("Franchise1");
        Mockito.verify(franchiseRepository).saveFranchise(any());
    }

    @Test
    void updateProductStock_ShouldUpdateStockSuccessfully() {
        Product product = Product.builder().name("Product1").stock(10).build();
        Branch branch = Branch.builder().name("Branch1").products(new ArrayList<>(List.of(product))).build();
        Franchise franchise = Franchise.builder().name("Franchise1").branches(List.of(branch)).build();

        Mockito.when(franchiseRepository.findFranchiseByName("Franchise1"))
                .thenReturn(Mono.just(franchise));
        Mockito.when(franchiseRepository.saveFranchise(any()))
                .thenReturn(Mono.just(franchise));

        Mono<Branch> result = branchUseCase.updateProductStock("Franchise1", "Branch1", "Product1", 50);

        StepVerifier.create(result)
                .expectNextMatches(updatedBranch ->
                        updatedBranch.getProducts().stream().anyMatch(p -> p.getStock() == 50)
                )
                .verifyComplete();

        Mockito.verify(franchiseRepository).findFranchiseByName("Franchise1");
        Mockito.verify(franchiseRepository).saveFranchise(any());
    }

    @Test
    void updateBranchName_ShouldUpdateBranchNameSuccessfully() {
        Branch branch = Branch.builder().name("OldBranch").products(new ArrayList<>()).build();
        Franchise franchise = Franchise.builder().name("Franchise1").branches(new ArrayList<>(List.of(branch))).build();

        Mockito.when(franchiseRepository.findFranchiseByName("Franchise1"))
                .thenReturn(Mono.just(franchise));
        Mockito.when(franchiseRepository.saveFranchise(any()))
                .thenReturn(Mono.just(franchise));

        Mono<Branch> result = branchUseCase.updateBranchName("Franchise1", "OldBranch", "NewBranch");

        StepVerifier.create(result)
                .expectNextMatches(updatedBranch -> updatedBranch.getName().equals("NewBranch"))
                .verifyComplete();

        Mockito.verify(franchiseRepository).findFranchiseByName("Franchise1");
        Mockito.verify(franchiseRepository).saveFranchise(any());
    }


}