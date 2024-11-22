package co.ke.whatsappcommerce.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.Index;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MongoInitializer {

    private final MongoTemplate mongoTemplate;

    public MongoInitializer(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Bean
    public ApplicationRunner initDatabase() {
        return args -> {
            log.info("Initializing MongoDB collections and indexes");
            createCollections();
            createIndexes();
            log.info("MongoDB initialization completed");
        };
    }

    private void createCollections() {
        createCollectionIfNotExists("orders");
        createCollectionIfNotExists("products");
        createCollectionIfNotExists("customers");
    }

    private void createCollectionIfNotExists(String collectionName) {
        if (!mongoTemplate.collectionExists(collectionName)) {
            mongoTemplate.createCollection(collectionName);
            log.info("Created collection: {}", collectionName);
        }
    }

    private void createIndexes() {
        // Orders indexes
        mongoTemplate.indexOps("orders")
                .ensureIndex(new Index()
                        .on("customerPhone", Sort.Direction.DESC)
                        .on("createdAt", Sort.Direction.DESC));

        mongoTemplate.indexOps("orders")
                .ensureIndex(new Index()
                        .on("status", Sort.Direction.ASC));

        // Products indexes
        mongoTemplate.indexOps("products")
                .ensureIndex(new Index()
                        .on("whatsappCatalogId", Sort.Direction.ASC)
                        .unique());

        // Customers indexes
        mongoTemplate.indexOps("customers")
                .ensureIndex(new Index()
                        .on("phoneNumber", Sort.Direction.ASC)
                        .unique());

        log.info("Created indexes for collections");
    }
}