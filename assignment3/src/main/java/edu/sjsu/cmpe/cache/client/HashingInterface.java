package edu.sjsu.cmpe.cache.client;

import java.security.*;

public interface HashingInterface{
    public void add(CacheServiceInterface node) throws NoSuchAlgorithmException;
    public void remove(CacheServiceInterface node) throws NoSuchAlgorithmException;
    public CacheServiceInterface get(String key) throws NoSuchAlgorithmException;
}