package org.orman.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Allows to query a hashmap from both directions: by key and by value (which
 * returns the key).
 * 
 * By value queries return their key. Underlying implementation consumes 2x
 * memory compared to a {@link HashMap}, however provides O(1) access time (O(N)
 * in {@link HashMap} case.)
 * 
 * NOTE: Do not use Map as its data type if you need to use getByVal() method.
 * 
 * <b>CAUTION:</b> If there are more than one entries with same value (<code>V</code>),
 * getByVal() method will return most recently key-value tuple inserted.
 * 
 * Example usage:
 * <pre>
 * DoubleAssociativeMap<String, Integer> map = new DoubleAssociativeMap<String, Integer>();
 * map.put("ahmet", 1989);
 * map.getByKey("ahmet"); // 1989
 * map.getByVal(1989); // "ahmet"
 * </pre>
 * 
 * @author Ahmet Alp Balkan
 * 
 * @param <K>
 * @param <V>
 *            hashCode() implementation is important for this class!
 */
@SuppressWarnings("unchecked")
public class DoubleAssociativeMap<K,V> implements Map<K, V> {
	protected static final String VALUE_PREFIX = "__val_";
	
	private HashMap<Object, Object> map;
	
	public DoubleAssociativeMap(){
		map = new HashMap<Object, Object>();
	}
	
	/**
	 * Prepares a private key for the entry
	 * that is actual value is its private key
	 * prepared by this method and its value is 
	 * actual key. 
	 * 
	 * @param arg0
	 * @return
	 */
	private Object getValueKey(Object arg0) {
		return VALUE_PREFIX+arg0.hashCode();
	}
	
	/**
	 * Identical to get(Object o)
	 * @param value
	 * @return
	 */
	public V getByKey(K key) {
		return this.get(key);
	}
	
	/**
	 * Request key by its associated value.
	 * 
	 * CAUTION: If there are more than one entries with this
	 * value, key of the most recently inserted entry will
	 * be returned. 
	 * 
	 * @param value
	 * @return
	 */
	public K getByVal(V value) {
		return (K) map.get(getValueKey(value));
	}

	@Override
	public void clear() {
		map.clear();
	}

	@Override
	public boolean containsKey(Object arg0) {
		return map.containsKey(arg0);
	}

	@Override
	public boolean containsValue(Object arg0) {
		return map.containsKey(getValueKey(arg0));
	}

	/**
	 * Does not return hidden entries.
	 */
	@Override
	public Set<Entry<K, V>> entrySet() {
		Set<Entry<Object, Object>> entrySet = map.entrySet();
		Set<Entry<K, V>> ret = new HashSet<Entry<K,V>>();
		
		for(Entry<Object, Object> entry : entrySet){
			if(!entry.getKey().toString().startsWith(VALUE_PREFIX))
				ret.add((Entry<K, V>) entry);
		}
	
		return ret;
	}

	@Override
	public V get(Object o) {
		return (V) map.get(o);
	}

	
	@Override
	public boolean isEmpty() {
		return map.isEmpty();
	}

	/**
	 * Does not return hidden entry keys.
	 */
	@Override
	public Set<K> keySet() {
		Set<Object> keySet = map.keySet();
		Set<K> ret = new HashSet<K>();
		
		for(Object key : keySet){
			if(!key.toString().startsWith(VALUE_PREFIX))
				ret.add((K) key);
		}
		return ret;
	}

	@Override
	public V put(K key, V value) {
		putValueKey(key, value);
		return (V) map.put(key, value);
	}
	
	private void putValueKey(K key, V value){
		map.put(getValueKey(value), key);
	}

	@Override
	public void putAll(Map<? extends K, ? extends V> m) {
		map.putAll(m);
	}

	@Override
	public V remove(Object o) {
		return (V) map.remove(0);
	}

	/**
	 * Caution: Time complexity is O(N).
	 */
	@Override
	public int size() {
		return this.keySet().size();
	}

	@Override
	public Collection<V> values() {
		Collection<V> values = new HashSet<V>();
	
		for(Entry<K, V> entry : this.entrySet()){
			values.add((V) entry.getValue());
		}
		
		return values;
	}
	
	/**
	 * Caution: Set representation surrounded with [], instead
	 * of {} in Map representation. Does not return hidden
	 * entries.
	 */
	@Override
	public String toString() {
		return this.entrySet().toString();
	}
}