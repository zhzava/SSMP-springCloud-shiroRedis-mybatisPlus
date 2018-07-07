package com.bosi.itms.shiro.Base;

import java.util.HashMap;

/**
 * Created by zhz on 2018/6/19.
 */
public class BaseMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = 9155660212774034975L;

    public V put(K key, V value) {
        if (key instanceof String) {
            key = (K) ((String) key).toLowerCase();
            StringBuffer keyName = new StringBuffer(((String) key).toLowerCase());
            while(keyName.indexOf("_") != -1){//修改字段名,如user_name改为userName
                int index = keyName.indexOf("_");
                keyName.insert(index ,keyName.substring(index+1, index+2).toUpperCase());
                keyName.delete(index+1, index+3);
            }
            String theKeyName = keyName.toString();
            key = (K) theKeyName;
            /*Set<Map.Entry<K, V>> set = super.entrySet();
            for (Map.Entry entry : set) {
                if(key.toString().equals(entry.getKey().toString())){
                    return super.put((K)entry.getKey(),value);
                }
            }*/
        }
        return super.put(key, value);
    }

}
