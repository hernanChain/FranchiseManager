package co.com.franchise.api;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(FranchiseHandler franchiseHandler,
                                                         BranchHandler branchHandler,
                                                         ProductHandler productHandler) {
        return route()
                // Franchise Routes
                .POST("/api/franchises", franchiseHandler::createFranchise)
                .POST("/api/franchises/{franchiseName}/branches", franchiseHandler::addBranchToFranchise)
                .GET("/api/franchises/{franchiseName}", franchiseHandler::getFranchiseByName)
                .PUT("/api/franchises/{oldName}/update/{newName}", franchiseHandler::updateFranchiseName)

                // Branch Routes
                .POST("/api/franchises/{franchiseName}/branches/{branchName}/products", branchHandler::addProductToBranch)
                .DELETE("/api/franchises/{franchiseName}/branches/{branchName}/products/{productName}", branchHandler::deleteProductFromBranch)
                .PUT("/api/franchises/{franchiseName}/branches/{branchName}/products/{productName}/stock", branchHandler::updateProductStock)
                .PUT("/api/franchises/{franchiseName}/branches/{oldBranchName}/update/{newBranchName}", branchHandler::updateBranchName)


                // Product Routes
                .GET("/api/franchises/{franchiseName}/products/top-stock", productHandler::findTopStockProductPerBranch)
                .build();
    }

}
