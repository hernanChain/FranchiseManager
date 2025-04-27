package co.com.franchise.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "branches")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BranchMongoDBModel {
    private String name;
    private List<ProductMongoDBModel> products;

}
