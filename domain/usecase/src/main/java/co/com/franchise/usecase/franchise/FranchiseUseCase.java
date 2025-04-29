package co.com.franchise.usecase.franchise;

import co.com.franchise.model.branch.Branch;
import co.com.franchise.model.exceptions.FranchiseNotFoundException;
import co.com.franchise.model.franchise.Franchise;
import co.com.franchise.model.franchise.gateways.FranchiseRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class FranchiseUseCase {

    private final FranchiseRepository franchiseRepository;

    public Mono<Franchise> createFranchise(Franchise franchise){
        return franchiseRepository.saveFranchise(franchise);
    };

    public Mono<Branch> addBranchToFranchise(String franchiseName, Branch branch){
        return franchiseRepository.findFranchiseByName(franchiseName)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException()))
                .flatMap(franchise -> {
                    franchise.getBranches().add(branch);
                    return franchiseRepository.saveFranchise(franchise)
                            .thenReturn(branch);
                });
    }

    public Mono<Franchise> getFranchise(String franchiseName){
        return franchiseRepository.findFranchiseByName(franchiseName)
                .switchIfEmpty(Mono.error(new FranchiseNotFoundException()));
    }

    public Mono<Franchise> updateFranchiseName(String oldName, String newName) {
        return franchiseRepository.findFranchiseByName(oldName)
                .flatMap(oldFranchise -> {
                    Franchise newFranchise = Franchise.builder()
                            .name(newName)
                            .branches(oldFranchise.getBranches())
                            .build();

                    return franchiseRepository.saveFranchise(newFranchise)
                            .then(franchiseRepository.deleteFranchise(oldFranchise))
                            .thenReturn(newFranchise);
                });
    }
}