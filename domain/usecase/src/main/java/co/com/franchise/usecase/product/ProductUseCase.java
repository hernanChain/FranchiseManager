package co.com.franchise.usecase.product;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.exceptions.BranchNotFoundException;
import co.com.franchise.model.exceptions.FranchiseNotFoundException;
import co.com.franchise.model.exceptions.ProductNotFoundException;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import co.com.franchise.model.product.Product;
import co.com.franchise.model.product.ProductWithBranchDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@RequiredArgsConstructor
public class ProductUseCase {

    private final FranchiseRepository franchiseRepository;

    public Flux<ProductWithBranchDTO> findTopStockProductPerBranch(String franchiseName){
        return franchiseRepository.findFranchiseByName(franchiseName)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                .flatMapMany(franchise -> Flux.fromIterable(franchise.getBranches()))
                .flatMap(branch -> Mono.justOrEmpty(branch.getProducts().stream()
                        .max(Comparator.comparing(Product::getStock))
                        .map(product -> new ProductWithBranchDTO(branch.getName(), product.getName(), product.getStock()))
                ));
    }

}
