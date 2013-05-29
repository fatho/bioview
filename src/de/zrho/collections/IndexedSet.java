package de.zrho.collections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

/**
 * Implements a bijective map between indices and elements.
 * The <tt>IndexedSet</tt> cannot contain duplicate elements or
 * multiple elements with the same index.
 * 
 * The class provides fast lookups in either direction
 * at the cost of an increased memory usage.
 * 
 * @author Fabian Thorand
 *
 * @param <T> the type of elements in this indexed set.
 */
public class IndexedSet<T> implements Set<T>, List<T> {
	private ArrayList<T> data;
	private HashMap<T,Integer> indices;
	
	public IndexedSet() {
		this(10);
	}
	
	public IndexedSet(int initialCapacity) {
		this.data = new ArrayList<>(initialCapacity);
		this.indices = new HashMap<>(initialCapacity);
	}
	
	public IndexedSet(Collection<? extends T> data) {
		this.data = new ArrayList<>(data);
		this.indices = new HashMap<>();
		rebuildIndices();
	}
	
	private void rebuildIndices() {
		this.indices.clear();
		for(int i = 0; i < data.size(); i++) {
			this.indices.put(data.get(i), i);
		}
	}
	private void rebuildIndicesFrom(int from) {
		for(int i = from; i < data.size(); i++) {
			this.indices.put(data.get(i), i);
		}
	}
	
	public T get(int index) {
		return data.get(index);
	}
	
	@Override
	public int size() {
		return data.size();
	}

	@Override
	public boolean isEmpty() {
		return data.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return indices.containsKey(o);
	}

	@Override
	public Iterator<T> iterator() {
		return data.iterator();
	}

	@Override
	public Object[] toArray() {
		return data.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return data.toArray(a);
	}

	@Override
	public boolean add(T e) {
		if(indices.containsKey(e)) {
			return false;
		}
		int index = data.size();
		data.add(e);
		indices.put(e, index);
		return true;
	}

	@Override
	public boolean remove(Object o) {
		Integer io = indices.get(o);
		if(io != null) {
			int idx = io;
			data.remove(idx);
			indices.remove(o);
			rebuildIndicesFrom(idx);
			return true;
		}
		return false;
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return indices.keySet().containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		return addAll(data.size(), c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		boolean modified = data.retainAll(c);
		rebuildIndices();
		return modified;
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		boolean ret = data.removeAll(c);
		rebuildIndices();
		return ret;
	}

	@Override
	public void clear() {
		data.clear();
		indices.clear();
	}

	@Override
	public boolean addAll(int index, Collection<? extends T> c) {
		int ci = 0;
		boolean modified = false;
		for(T o : c) {
			if(!indices.containsKey(o)) {
				add(index + ci, o);
				ci += 1;
				modified = true;
			}
		}
		rebuildIndicesFrom(index);
		return modified;
	}

	@Override
	public T set(int index, T element) {
		if(indices.containsKey(element)) {
			throw new IllegalArgumentException("Duplicate entry");
		}
		T old = data.get(index);
		if(old != element) {
			indices.remove(old);
			indices.put(element, index);
		}
		return old;
	}

	@Override
	public void add(int index, T element) {
		if(indices.containsKey(element)) {
			throw new IllegalArgumentException("Duplicate entry");
		} else {
			data.add(index, element);
			indices.put(element, index);	
		}
	}

	@Override
	public T remove(int index) {
		T old = data.get(index);
		data.remove(index);
		indices.remove(old);
		rebuildIndicesFrom(index);
		return old;
	}

	@Override
	public int indexOf(Object o) {
		Integer i = indices.get(o);
		return i == null ? -1 : i;
	}

	@Override
	public int lastIndexOf(Object o) {
		return indexOf(o);
	}

	@Override
	public ListIterator<T> listIterator() {
		return data.listIterator();
	}

	@Override
	public ListIterator<T> listIterator(int index) {
		return data.listIterator(index);
	}

	@Override
	public List<T> subList(int fromIndex, int toIndex) {
		throw new RuntimeException("not implemented");
	}
	
	public Map<T, Integer> asMap() {
		return new MapWrapper();
	}

	class MapWrapper implements Map<T, Integer> 
	{
		@Override
		public boolean containsKey(Object key) {
			return indices.containsKey(key);
		}

		@Override
		public boolean containsValue(Object value) {
			if(value instanceof Integer) {
				int i = (Integer) value;
				return i >= 0 && i < data.size();
			} else {
				return false;
			}
		}

		@Override
		public Integer get(Object key) {
			return indexOf(key);
		}

		@Override
		public Integer put(T key, Integer value) {
			add(value, key);
			return null;
		}

		@Override
		public void putAll(Map<? extends T, ? extends Integer> m) {
			for(T k : m.keySet()) {
				put(k, m.get(k));
			}
		}

		@Override
		public Set<T> keySet() {
			return indices.keySet();
		}

		@Override
		public Collection<Integer> values() {
			return indices.values();
		}

		@Override
		public Set<java.util.Map.Entry<T, Integer>> entrySet() {
			return indices.entrySet();
		}

		@Override
		public int size() {
			return indices.size();
		}

		@Override
		public boolean isEmpty() {
			return indices.isEmpty();
		}

		@Override
		public Integer remove(Object key) {
			Integer i = indices.get(key);
			remove(key);
			return i;
		}

		@Override
		public void clear() {
			IndexedSet.this.clear();
		}
	}
}
