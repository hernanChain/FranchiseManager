package co.com.franchise.model.product;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductWithBranchDTO {

    private String branchName;
    private String productName;
    private int stock;

}
