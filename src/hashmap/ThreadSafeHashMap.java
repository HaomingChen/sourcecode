package hashmap;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Haoming Chen
 * Created on 2020/2/19
 */
public class ThreadSafeHashMap {

    public static void main(String[] args) {
        Map hashMap = new HashMap(16);
        Map safeHashMap = Collections.synchronizedMap(hashMap);
        safeHashMap.put("aa", "1");
        safeHashMap.put("bb", "2");
        System.out.println(safeHashMap.get("bb"));
        Map cHashMap = new ConcurrentHashMap();
    }

}
