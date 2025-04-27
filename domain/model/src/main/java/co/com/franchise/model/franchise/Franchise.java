package co.com.franchise.model.franchise;
import co.com.franchise.model.branch.Branch;
import lombok.*;
//import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Franchise {
    private String name;
    private List<Branch> branches;
}
