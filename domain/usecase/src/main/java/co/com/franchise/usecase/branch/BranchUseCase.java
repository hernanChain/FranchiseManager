package co.com.franchise.usecase.branch;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.exceptions.BranchNotFoundException;
import co.com.franchise.model.exceptions.FranchiseNotFoundException;
import co.com.franchise.model.exceptions.ProductNotFoundException;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import co.com.franchise.model.product.Product;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class BranchUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> addProductToBranch(String franchiseName, String branchName, Product product){
        return franchiseRepository.findFranchiseByName(franchiseName)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                .flatMap(franchise -> {
                    Branch branch = franchise.getBranches().stream()
                            .filter(b -> b.getName().equals(branchName))
                            .findFirst()
                            .orElseThrow(BranchNotFoundException::new);

                    branch.getProducts().add(product);
                    return franchiseRepository.saveFranchise(franchise);
                });
    };

    public Mono<Branch> deleteProductFromBranch(String franchiseName, String branchName, String productName){
        return franchiseRepository.findFranchiseByName(franchiseName)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                .flatMap(franchise -> {
                    Branch branch = findBranch(branchName, franchise);
                    branch.getProducts().removeIf(product -> product.getName().equals(productName));
                    return franchiseRepository.saveFranchise(franchise)
                            .thenReturn(branch);
                });
    };

    public Mono<Branch> updateProductStock(String franchiseName, String branchName, String productName, int newStock){
        return franchiseRepository.findFranchiseByName(franchiseName)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                .flatMap(franchise -> {
                    Branch branch = findBranch(branchName, franchise);
                    Product product = branch.getProducts().stream()
                            .filter(p -> p.getName().equals(productName))
                            .findFirst()
                            .orElseThrow(ProductNotFoundException::new);

                    product.setStock(newStock);
                    return franchiseRepository.saveFranchise(franchise)
                            .thenReturn(branch);
                });
    }

    private Branch findBranch(String branchName, Franchise franchise) {
        return franchise.getBranches().stream()
                .filter(branch -> branch.getName().equals(branchName))
                .findFirst()
                .orElseThrow(BranchNotFoundException::new);
    }
}
