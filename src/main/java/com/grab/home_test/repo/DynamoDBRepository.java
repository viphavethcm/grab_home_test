package com.grab.home_test.repo;

import com.grab.home_test.service.VersionControl;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class DynamoDBRepository {

   private final int numberOfNodes = 4;

   public static final ConcurrentMap<Integer, Map<String, Object>> dynamoDB = new ConcurrentHashMap<>();

   public Optional<Object> getItem(String key) {
      int partition = getPartition(key);
      Map<String, Object> map = dynamoDB.get(partition);
      if (map == null || !map.containsKey(key)) {
         return Optional.empty();
      }
      return Optional.of(map.get(key));
   }

   public void putItem(String key, Object object) {
      int partition = getPartition(key);
      Map<String, Object> partitionMap = dynamoDB.computeIfAbsent(partition, k -> new ConcurrentHashMap<>());
      partitionMap.put(key, object);
   }

   public boolean updateItemIfCondition(String key, Object newValue, int expectedVersion) {
      int partition = getPartition(key);
      Map<String, Object> partitionMap = this.dynamoDB.get(partition);
      if (partitionMap == null) {
         return false;
      }
      Object currentValue = partitionMap.get(key);
      // Locking on this record, allow sync request that wants to update
      synchronized (currentValue) {
         VersionControl currentVersion = (VersionControl) currentValue;

         // Update currentVersion += 1 where currentVersion = oldVersion
         if (currentVersion.getVersion() == expectedVersion) {
            VersionControl newVersionControl = (VersionControl) newValue;
            newVersionControl.setVersion(currentVersion.getVersion() + 1);

            partitionMap.put(key, newVersionControl);
            return true;
         }
      }
      return false;
   }

   private int getPartition(String key) {
      return Math.abs(key.hashCode() % numberOfNodes);
   }
}
