package co.com.franchise.model.branch;
import co.com.franchise.model.product.Product;
import lombok.*;
//import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Branch {
    private String name;
    private List<Product> products;
}
