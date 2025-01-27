package com.example.ead2project.repository.Data.DataService;
import com.example.ead2project.repository.Data.Entities.Customer.Customer;
import com.example.ead2project.repository.Data.Entities.Order.Order;
import com.example.ead2project.repository.Data.Entities.Order.Sequence;
import com.example.ead2project.repository.Data.Entities.product.Product;
import com.example.ead2project.repository.Data.Entities.tags.Tag;
import com.example.ead2project.repository.Data.Entities.user.User;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Getter
public class MongoDBContext {
    
    private final MongoDatabase database;

    // Collections
    private final FilteredMongoCollection<Customer> customers;
    private final FilteredMongoCollection<Order> orders;
    private final FilteredMongoCollection<Product> products;
    private final FilteredMongoCollection<Sequence> sequences;
    private final FilteredMongoCollection<User> users;
    private final FilteredMongoCollection<Tag> tags;
  
    public MongoDBContext(@Value("${spring.data.mongodb.uri}") String connectionString) {
        MongoClient client = MongoClients.create(connectionString);
        this.database = client.getDatabase("your-database-name");

        // Initialize collections
        this.customers = getFilteredCollection("Customer", Customer.class);
        this.orders = getFilteredCollection("Order", Order.class);
        this.products = getFilteredCollection("Product", Product.class);
        this.sequences = getFilteredCollection("Sequence", Sequence.class);
        this.users = getFilteredCollection("User", User.class);
        this.tags = getFilteredCollection("Tag", Tag.class);
    }

    public MongoDatabase getDatabase() {
        return database;
    }

    private HttpServletRequest getRequest() {
        try {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        } catch (Exception e) {
            return null;
        }
    }
    public FilteredMongoCollection<Product> getProducts() {
        return products;
    }
    private <T> FilteredMongoCollection<T> getFilteredCollection(String collectionName, Class<T> entityClass) {
        return new FilteredMongoCollection<>(database.getCollection(collectionName, entityClass), getRequest());
    }
} 