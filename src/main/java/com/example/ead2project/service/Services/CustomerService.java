package com.example.ead2project.service.Services;


import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Service;
import com.example.ead2project.repository.Data.DataService.MongoDBContext;
import com.example.ead2project.repository.Data.Entities.Customer.Customer;
import com.example.ead2project.repository.Helper.QueryBuilder;
import com.example.ead2project.repository.Helper.Utility;
import com.example.ead2project.serviceInterface.interfaces.ICustomerService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    
    private final MongoDBContext mongoContext;
    private final HttpServletRequest request;

    private String getClientId() {
        return Utility.getUserIdFromClaims(request);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return mongoContext.getCustomers().find(new Document())
                .sort(new Document("createdAt", -1))
                .into(new ArrayList<>());
    }

    @Override
    public Customer getCustomerById(String id) {
        return mongoContext.getCustomers().find(new Document("_id", id)).first();
    }

    @Override
    public void createCustomer(Customer customer) {
        mongoContext.getCustomers().insertOneAsync(customer);
    }

    @Override
public void updateCustomer(Customer customer) {
    mongoContext.getCustomers().replaceOneAsync(
        new Document("_id", customer.getId()), // Filter to match the document by _id
        customer                                // Replacement document
    );
}


    @Override
    public void deleteCustomer(String id) {
        mongoContext.getCustomers().softDeleteOneAsync(new Document("_id", id)).join();
    }

    @Override
    public List<Customer> searchCustomers(String query) {
        if (query == null || query.isEmpty()) {
            return mongoContext.getCustomers().find(new Document())
                    .sort(new Document("createdAt", -1))
                    .limit(20)
                    .into(new ArrayList<>());
        }

        List<String> searchFields = Arrays.asList("Email", "Phone", "FirstName", "LastName");
        var pipeline = QueryBuilder.buildSearchFilter(query, "customer_search_index", getClientId(), searchFields);
        
        return mongoContext.getCustomers().aggregate(pipeline.stream()
                .<Bson>map(doc -> (Bson) doc)
                .collect(Collectors.toList()))
                .into(new ArrayList<>());
    }

    @Override
    public Customer getCustomerByPhone(String phone) {
        return mongoContext.getCustomers().find(new Document("phone", phone)).first();
    }
} 