package com.grab.home_test.repo;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RedisCaching {

   private static final ConcurrentHashMap<String, Map<String, Object>> store = new ConcurrentHashMap<>();

   public void set(String key, String field, Object value) {
      store.computeIfAbsent(key, k -> new ConcurrentHashMap<>())
            .put(field, value);
   }

   public Map<String, Object> getAll(String key) {
      System.out.println("[Redis] Start getting key: " + key);
      return store.get(key);
   }
}
