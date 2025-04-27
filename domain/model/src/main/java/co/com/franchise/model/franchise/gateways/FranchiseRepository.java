package co.com.franchise.model.franchise.gateways;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.franchise.Franchise;
import reactor.core.publisher.Mono;

public interface FranchiseRepository {

    Mono<Franchise> findFranchiseByName(String franchiseName);

    Mono<Franchise> saveFranchise(Franchise franchise);

    Mono<Void> deleteFranchise(Franchise franchise);
}

