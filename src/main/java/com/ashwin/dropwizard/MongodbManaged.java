package com.ashwin.dropwizard;

import com.mongodb.Mongo;
import io.dropwizard.lifecycle.Managed;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MongodbManaged implements Managed {
    private static Log LOG = LogFactory.getLog(MongodbManaged.class);
    private Mongo mongo;

    public MongodbManaged(Mongo mongo) {
        this.mongo = mongo;
    }

    @Override
    public void start() throws Exception {
        LOG.debug("MongodbManaged start");
    }

    @Override
    public void stop() throws Exception {
        LOG.debug("MongodbManaged stop");
        mongo.close();
    }
}
