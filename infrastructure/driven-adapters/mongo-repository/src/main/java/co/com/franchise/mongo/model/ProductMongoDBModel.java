package co.com.franchise.mongo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "franchises")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductMongoDBModel {
    private String name;
    private int stock;

}
