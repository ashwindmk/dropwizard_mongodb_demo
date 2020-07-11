package com.ashwin.dropwizard;

import com.ashwin.dropwizard.resource.EmployeeResource;
import com.ashwin.dropwizard.service.MongodbService;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;

public class MongodbDemoApplication extends Application<MongodbDemoConfiguration> {
    private static Log LOG = LogFactory.getLog(MongodbDemoApplication.class);

    public static void main(String[] args) throws Exception {
        new MongodbDemoApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<MongodbDemoConfiguration> b) {
        LOG.debug("MongodbDemoApplication: initialize");
    }

    @Override
    public void run(MongodbDemoConfiguration config, Environment env) throws Exception {
        MongoClient mongoClient = new MongoClient(config.getMongoHost(), config.getMongoPort());

        MongodbManaged mongoManaged = new MongodbManaged(mongoClient);
        env.lifecycle().manage(mongoManaged);

        MongoDatabase db = mongoClient.getDatabase(config.getMongoDB());
        MongoCollection<Document> collection = db.getCollection(config.getCollectionName());

        LOG.info("MongodbDemoApplication: Registering RESTful API resources");
        env.jersey().register(new EmployeeResource(collection, new MongodbService()));

        env.healthChecks().register("MongodbHealthCheck", new MongodbHealthCheck(mongoClient));
    }
}
