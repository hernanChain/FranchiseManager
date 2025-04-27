package co.com.franchise.api;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.usecase.franchise.FranchiseUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {


    private final FranchiseUseCase franchiseUseCase;

    public Mono<ServerResponse> createFranchise(ServerRequest request) {
        return request.bodyToMono(Franchise.class)
                .flatMap(franchiseUseCase::createFranchise)
                .flatMap(franchise -> ServerResponse.status(201).bodyValue(franchise));
    }

    public Mono<ServerResponse> addBranchToFranchise(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        return request.bodyToMono(Branch.class)
                .flatMap(branch -> franchiseUseCase.addBranchToFranchise(franchiseName, branch))
                .flatMap(branch -> ServerResponse.status(201).bodyValue(branch));
    }

    public Mono<ServerResponse> getFranchiseByName(ServerRequest request) {
        String franchiseName = request.pathVariable("franchiseName");
        return franchiseUseCase.getFranchise(franchiseName)
                .flatMap(franchise -> ServerResponse.ok().bodyValue(franchise))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

}
