package co.com.franchise.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "franchises")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FranchiseMongoDBModel {

    @Id
    private String name;
    private List<BranchMongoDBModel> branches;

}
