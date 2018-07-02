package i8n.api.map.v1.impl.remote;

import i8n.api.map.v1.DummyMap;
import org.kohsuke.MetaInfServices;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

@MetaInfServices
public class DummyMapRemote<K, V> implements DummyMap<K, V> {

   private final Queue<String> queue = new LinkedList<>();

   public V get(K key) {
      queue.offer("[map-v1-remote] GET key=" + key);
      return null;
   }

   public V put(K key, V value) {
      queue.offer("[map-v1-remote] PUT key=" + key + ",value=" + value);
      return null;
   }

   @Override
   public String toString() {
      return queue.stream().collect(Collectors.joining("\n"));
   }

}