package co.com.franchise.api;

import co.com.franchise.usecase.product.ProductUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class ProductHandler {

    private final ProductUseCase productUseCase;

    public Mono<ServerResponse> findTopStockProductPerBranch(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        return productUseCase.findTopStockProductPerBranch(franchiseName)
                .collectList()
                .flatMap(products -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(products))
                .switchIfEmpty(ServerResponse.notFound().build());
    }
}
