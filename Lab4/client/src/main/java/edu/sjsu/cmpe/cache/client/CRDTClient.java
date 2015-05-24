package edu.sjsu.cmpe.cache.client;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import java.util.Iterator;

public class CRDTClient {

	private ArrayList<DistributedCacheService> serverList = new ArrayList<DistributedCacheService>();
	public ConcurrentHashMap<String, String> putStatus = new ConcurrentHashMap<String, String>();
	public ConcurrentHashMap<String, String> getStatus = new ConcurrentHashMap<String, String>();
	
	public void addServer(String serverURL) {
		serverList.add(new DistributedCacheService(serverURL, this));
	}
	
	public void put(long key, String value) throws Exception{
		//for each server, we need to put the value
		for(DistributedCacheService service: serverList) {
			service.put(key, value);
		}
		
		while(true) {
			//starting to update all the three servers until the updation is complete
        	if(putStatus.size() < 3) {
        		System.out.println("Currently processing your update");
				Thread.sleep(1000);
        	} 
        	else{
        		//if all the three got updated, we need to confirm it
        		
        		int fail = 0, pass = 0;
        		
        		for(DistributedCacheService service: serverList) {
        			
        			System.out.println("Status for PUT on : "+service.getCacheServerURL()+ " = "+ putStatus.get(service.getCacheServerURL()));
        			
        			if(putStatus.get(service.getCacheServerURL()).equalsIgnoreCase("fail")){ 
            			++fail;
        			}
            		else{
            			++pass;
            		}
        		}
        		//If the failure was greater than 1, means it could not get updated on 2, so rollback
        		if(fail > 1) {
        			System.out.println("PUT Rollback");
        			
        			for(DistributedCacheService service: serverList) {
        				service.delete(key);
        			}
        		}
        		else {
        			System.out.println("PUT SUCCESSFUL");
        		}
        		
        		putStatus.clear();
        		
        		break;
        	}
        }
	}
	
	public String get(long key) throws Exception{
		for(DistributedCacheService service: serverList) {
			service.get(key);
		}
		
		while(true) {
        	if(getStatus.size() < 3) {
        		System.out.println("Currently getting your response");
				Thread.sleep(1000);
        	} 
        	else{
        		HashMap<String, List<String>> valuesMap = new HashMap<String, List<String>>();
        		for(DistributedCacheService service: serverList) {
        			
        			//if failed
        			if(getStatus.get(service.getCacheServerURL()).equalsIgnoreCase("fail")){ 
            			System.out.println("Getting value from : "+ service.getCacheServerURL() + " failed");
        			}
            		else {
            			if(valuesMap.containsKey(getStatus.get(service.getCacheServerURL()))) {
            				valuesMap.get(getStatus.get(service.getCacheServerURL())).add(service.getCacheServerURL());
            			} 
            			else {
            				List<String> tempList = new ArrayList<String>();
            				tempList.add(service.getCacheServerURL());
            				valuesMap.put(getStatus.get(service.getCacheServerURL()),tempList);
            			}
            		}
        		}
        		
        		if(valuesMap.size() != 1) {
        			System.out.println("Inconsistent state on the servers");
        			Iterator<Entry<String, List<String>>> iterator = valuesMap.entrySet().iterator();
        			int majority = 0;
        			String finVal = null;
        			ArrayList <String> updateServer = new ArrayList<String>();
        			
        		    while (iterator.hasNext()) {
        		        Map.Entry<String, List<String>> map = (Map.Entry<String, List<String>>)iterator.next();
        		        if(map.getValue().size() > majority) {
        		        	majority = map.getValue().size();
        		        	finVal = map.getKey();
        		        } 
        		        else {
        		        	for (String str: map.getValue()){
        		        		updateServer.add(str);
        		        	}
        		        }
        		    }
        		    
        			System.out.println("Making server consistent");
        			for(String str: updateServer){
        				for(DistributedCacheService service: serverList) {
            				if(service.getCacheServerURL().equalsIgnoreCase(str)){
            					service.put(key, finVal);
            				}
            			}
        			}
        			getStatus.clear();
        			return finVal;
        		} 
        		else {
        			System.out.println("GET SUCCESSFUL");
        			getStatus.clear();
        			return valuesMap.keySet().toArray()[0].toString();
        		}
        	}
        }
		
	}
}
