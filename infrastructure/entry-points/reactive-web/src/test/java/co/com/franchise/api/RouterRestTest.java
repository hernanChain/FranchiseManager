package co.com.franchise.api;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductWithBranchDTO;
import co.com.franchise.usecase.branch.BranchUseCase;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import co.com.franchise.usecase.product.ProductUseCase;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, FranchiseHandler.class, BranchHandler.class, ProductHandler.class})
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private FranchiseUseCase franchiseUseCase;

    @MockitoBean
    private BranchUseCase branchUseCase;

    @MockitoBean
    private ProductUseCase productUseCase;

    private Franchise franchise;
    private Branch branch;
    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder().name("Product1").stock(100).build();
        branch = Branch.builder().name("Branch1").products(List.of(product)).build();
        franchise = Franchise.builder().name("Franchise1").branches(List.of(branch)).build();
    }

    // ---------------------- FRANCHISE HANDLER TESTS ----------------------

    @Test
    void createFranchise_ShouldReturnCreatedFranchise() {
        when(franchiseUseCase.createFranchise(Mockito.any())).thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/api/franchises")
                .bodyValue(franchise)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void addBranchToFranchise_ShouldReturnCreatedBranch() {
        when(franchiseUseCase.addBranchToFranchise(Mockito.anyString(), Mockito.any()))
                .thenReturn(Mono.just(branch));

        webTestClient.post()
                .uri("/api/franchises/Franchise1/branches")
                .bodyValue(branch)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void getFranchiseByName_ShouldReturnFranchise() {
        when(franchiseUseCase.getFranchise("Franchise1")).thenReturn(Mono.just(franchise));

        webTestClient.get()
                .uri("/api/franchises/Franchise1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void updateFranchiseName_ShouldReturnUpdatedFranchise() {
        when(franchiseUseCase.updateFranchiseName("Franchise1", "Franchise2"))
                .thenReturn(Mono.just(franchise));

        webTestClient.put()
                .uri("/api/franchises/Franchise1/update/Franchise2")
                .exchange()
                .expectStatus().isOk();
    }

    // ---------------------- BRANCH HANDLER TESTS ----------------------

    @Test
    void addProductToBranch_ShouldReturnUpdatedBranch() {
        when(branchUseCase.addProductToBranch(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(Mono.just(franchise));

        webTestClient.post()
                .uri("/api/franchises/Franchise1/branches/Branch1/products")
                .bodyValue(product)
                .exchange()
                .expectStatus().isCreated();
    }

    @Test
    void deleteProductFromBranch_ShouldReturnBranch() {
        when(branchUseCase.deleteProductFromBranch(Mockito.anyString(), Mockito.anyString(), Mockito.any()))
                .thenReturn(Mono.just(branch));

        webTestClient.delete()
                .uri("/api/franchises/Franchise1/branches/Branch1/products/Product1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void updateProductStock_ShouldReturnBranch() {
        when(branchUseCase.updateProductStock(Mockito.anyString(), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt()))
                .thenReturn(Mono.just(branch));

        webTestClient.put()
                .uri("/api/franchises/Franchise1/branches/Branch1/products/Product1/stock?newStock=200")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void updateBranchName_ShouldReturnBranch() {
        when(branchUseCase.updateBranchName(Mockito.anyString(), Mockito.anyString(), Mockito.anyString()))
                .thenReturn(Mono.just(branch));

        webTestClient.put()
                .uri("/api/franchises/Franchise1/branches/Branch1/update/Branch2")
                .exchange()
                .expectStatus().isOk();
    }

    // ---------------------- PRODUCT HANDLER TESTS ----------------------

    @Test
    void findTopStockProductPerBranch_ShouldReturnProducts() {
        ProductWithBranchDTO dto = new ProductWithBranchDTO("Branch1", "Product1", 100);

        when(productUseCase.findTopStockProductPerBranch("Franchise1"))
                .thenReturn(Flux.just(dto));

        webTestClient.get()
                .uri("/api/franchises/Franchise1/products/top-stock")
                .exchange()
                .expectStatus().isOk();
    }

}
