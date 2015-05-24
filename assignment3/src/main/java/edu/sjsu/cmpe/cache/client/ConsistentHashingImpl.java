package edu.sjsu.cmpe.cache.client;

import java.util.*;
import java.security.*;

public class ConsistentHashingImpl implements HashingInterface{
    
    private SortedMap<String, CacheServiceInterface> ring = new TreeMap<String, CacheServiceInterface>();
    private HashFunction hashFunction;
    
    public ConsistentHashingImpl(List<CacheServiceInterface> nodes, HashFunction hashFunction) throws NoSuchAlgorithmException{
        this.hashFunction = hashFunction;
        for(CacheServiceInterface oneNode : nodes){
            add(oneNode);
        }
    }
    
    @Override
    public void add(CacheServiceInterface node) throws NoSuchAlgorithmException{
        ring.put(hashFunction.hash(node.toString()), node);
    }
    
    @Override
    public void remove(CacheServiceInterface node) throws NoSuchAlgorithmException{
        ring.remove(hashFunction.hash(node.toString()));
    }
    
    @Override
    public CacheServiceInterface get(String key) throws NoSuchAlgorithmException{
        if(ring.isEmpty()){
            return null;
        }
        
        //get the node at this key
        String hash = hashFunction.hash(key);
        //if no node found
        if(!ring.containsKey(hash)){
            SortedMap<String, CacheServiceInterface> tailMap =
        ring.tailMap(hash);
            
            //check for the first key in the tail map if tailMap is not empty
            hash = tailMap.isEmpty() ? ring.firstKey() : tailMap.firstKey();
        }
        
        return ring.get(hash);
    }
}