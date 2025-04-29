package co.com.franchise.usecase.product;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.exceptions.FranchiseNotFoundException;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductWithBranchDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductUseCaseTest {

    private FranchiseRepository franchiseRepository;
    private ProductUseCase productUseCase;

    @BeforeEach
    void setUp() {
        franchiseRepository = mock(FranchiseRepository.class);
        productUseCase = new ProductUseCase(franchiseRepository);
    }

    @Test
    void findTopStockProductPerBranch_ShouldReturnProductsWithHighestStock() {
        Product product1 = Product.builder().name("Product1").stock(20).build();
        Product product2 = Product.builder().name("Product2").stock(50).build();
        Product product3 = Product.builder().name("Product3").stock(10).build();

        Branch branch1 = Branch.builder().name("Branch1").products(List.of(product1, product2)).build();
        Branch branch2 = Branch.builder().name("Branch2").products(List.of(product3)).build();

        Franchise franchise = Franchise.builder().name("Franchise1").branches(List.of(branch1, branch2)).build();

        when(franchiseRepository.findFranchiseByName("Franchise1")).thenReturn(Mono.just(franchise));

        Flux<ProductWithBranchDTO> result = productUseCase.findTopStockProductPerBranch("Franchise1");

        StepVerifier.create(result)
                .expectNextMatches(dto -> dto.getBranchName().equals("Branch1") && dto.getProductName().equals("Product2") && dto.getStock() == 50)
                .expectNextMatches(dto -> dto.getBranchName().equals("Branch2") && dto.getProductName().equals("Product3") && dto.getStock() == 10)
                .verifyComplete();

        verify(franchiseRepository).findFranchiseByName("Franchise1");
    }

    @Test
    void findTopStockProductPerBranch_ShouldThrowFranchiseNotFoundException_WhenFranchiseDoesNotExist() {
        when(franchiseRepository.findFranchiseByName("NonExistentFranchise"))
                .thenReturn(Mono.empty());

        Flux<ProductWithBranchDTO> result = productUseCase.findTopStockProductPerBranch("NonExistentFranchise");

        StepVerifier.create(result)
                .expectError(FranchiseNotFoundException.class)
                .verify();

        verify(franchiseRepository).findFranchiseByName("NonExistentFranchise");
    }

}