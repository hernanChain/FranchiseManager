package co.com.franchise.api;

import co.com.franchise.model.product.Product;
import co.com.franchise.usecase.branch.BranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class BranchHandler {

    private final BranchUseCase branchUseCase;

    public Mono<ServerResponse> addProductToBranch(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        String branchName = request.pathVariable("branchName");
        return request.bodyToMono(Product.class)
                .flatMap(product -> branchUseCase.addProductToBranch(franchiseName, branchName, product))
                .flatMap(branch -> ServerResponse.status(201).bodyValue(branch));
    }

    public Mono<ServerResponse> deleteProductFromBranch(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        String branchName = request.pathVariable("branchName");
        String productName = request.pathVariable("productName");
        return branchUseCase.deleteProductFromBranch(franchiseName, branchName, productName)
                .flatMap(branch -> ServerResponse.ok().bodyValue(branch));
    }

    public Mono<ServerResponse> updateProductStock(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        String branchName = request.pathVariable("branchName");
        String productName = request.pathVariable("productName");
        int newStock = Integer.parseInt(request.queryParam("newStock").orElse("0"));
        return branchUseCase.updateProductStock(franchiseName, branchName, productName, newStock)
                .flatMap(branch -> ServerResponse.ok().bodyValue(branch));
    }
}
