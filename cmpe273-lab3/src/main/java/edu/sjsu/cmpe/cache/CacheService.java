package edu.sjsu.cmpe.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;

import edu.sjsu.cmpe.cache.api.resources.CacheResource;
import edu.sjsu.cmpe.cache.config.CacheServiceConfiguration;
import edu.sjsu.cmpe.cache.repository.CacheInterface;
import edu.sjsu.cmpe.cache.repository.ChronicleMapCache;


public class CacheService extends Service<CacheServiceConfiguration> {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private static String fileChoice;
    public static void main(String[] args) throws Exception {
    	fileChoice = args[1];
        new CacheService().run(args);
    }

    @Override
    public void initialize(Bootstrap<CacheServiceConfiguration> bootstrap) {
        bootstrap.setName("cache-server");
    }

    @Override
    public void run(CacheServiceConfiguration configuration,
            Environment environment) throws Exception {
        /** Cache APIs */
        //Throws JSONException when fileChoice directly contains whole name from argument 1, so from 7 to 15 relevant
    	//so that server_X can come
        CacheInterface cache = new ChronicleMapCache(fileChoice.substring(7, 15));
        environment.addResource(new CacheResource(cache));
        log.info("Loaded resources");

    }
}
