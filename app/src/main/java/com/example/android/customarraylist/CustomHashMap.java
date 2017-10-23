package com.example.android.customarraylist;

/**
 * Created by kevinsun on 10/23/17.
 */

public class CustomHashMap<K, V> {

    private MyEntry<K, V>[] table;//存放数据的 tab 表
    private int size;//当前元素个数
    private int threshold;//当前table 长度最大存放的元素个数，超过则需要扩容

    public CustomHashMap() {
        table = new MyEntry[16];//默认16个元素
        size = 0;
    }

    /**
     * 插入一组键值对
     *
     * @param key   插入的键值对 key，可以为 null
     * @param value 插入的键值对 value，可以为 null
     * @return 如果插入的 key 已存在，则返回被覆盖之前的 oldValue，否则返回 null
     */
    public V put(K key, V value) {
        if (key == null) {//如果 key 为 null，则执行插入空 kye 的方法
            return putForNullKey(value);
        }

        int hash = secondaryHash(key);
        int bucketIndex = indexFor(hash, table.length);
        for (MyEntry<K, V> e = table[bucketIndex]; e != null; e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        addEntry(hash, key, value, bucketIndex);
        return null;
    }

    /**
     * 删除 key 所对应的元素
     *
     * @param key 需要删除元素的 key
     * @return 如果找到 key 对应的元素value，则返回 value，否则返回 null
     */
    public V remove(Object key) {
        MyEntry<K, V> e = removeEntryForKey(key);
        return (e == null ? null : e.value);
    }

    /**
     * 对 key 为 null 的插入执行特殊的处理。因为 key 为 null 无法进行 equals 操作
     *
     * @param value 插入空 key 的 value
     * @return 如果插入的空 key 已存在，则返回被覆盖之前的 oldValue，否则返回 null
     */
    private V putForNullKey(V value) {
        for (MyEntry<K, V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                return oldValue;
            }
        }
        addEntry(0, null, value, 0);
        return null;
    }

    /**
     * 先检测是否需要调整表的大小，然后再执行创建 Entry 操作
     *
     * @param hash        插入 key 的 hash 值
     * @param key         需要插入元素 key
     * @param value       需要插入元素 value
     * @param bucketIndex 元素插入在 table 的索引
     */
    private void addEntry(int hash, K key, V value, int bucketIndex) {
        if ((size >= threshold) && (null != table[bucketIndex])) {
            resize(2 * table.length);
            hash = (null != key) ? secondaryHash(key) : 0;
            bucketIndex = indexFor(hash, table.length);
        }

        createEntry(hash, key, value, bucketIndex);
    }

    /**
     * 创建一个新的 MyEntry，并将其插入到表头
     */
    private void createEntry(int hash, K key, V value, int bucketIndex) {
        MyEntry<K, V> e = table[bucketIndex];
        table[bucketIndex] = new MyEntry<>(hash, key, value, e);
        size++;
    }

    /**
     * 对当前的 table 执行扩容操作
     * 这里默认了加载因子为0.75
     * 注意：这里扩容没有做最大值限制，正常应该做的
     *
     * @param newCapacity 需要扩容的表长
     */
    private void resize(int newCapacity) {
        MyEntry<K, V>[] newTable = new MyEntry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = newCapacity >> 1 + newCapacity >> 2;
    }

    /**
     * 将扩容前旧表的所有元素转移到新表
     * 这里消耗比较多，因为表长度变了，需要重新计算每个元素的 bucketIndex
     */
    private void transfer(MyEntry<K, V>[] newTable) {
        int newCapacity = newTable.length;
        for (MyEntry<K, V> e : table) {
            while (null != e) {
                MyEntry<K, V> next = e.next;
                int bucketIndex = indexFor(e.hash, newCapacity);
                e.next = newTable[bucketIndex];
                newTable[bucketIndex] = e;
                e = next;
            }
        }
    }

    /**
     * 查表删除 key 对应的元素
     * @return 如果有 key 的映射则返回映射的元素，如果没有则返回 null
     *
     * */
    private MyEntry<K, V> removeEntryForKey(Object key) {
        if (size == 0) {
            return null;
        }
        int hash = (key == null) ? 0 : secondaryHash(key);
        int bucketIndex = indexFor(hash, table.length);
        MyEntry<K, V> prev = table[bucketIndex];
        MyEntry<K, V> e = prev;

        while (e != null) {
            MyEntry<K, V> next = e.next;
            Object k;
            if (e.hash == hash &&
                    ((k = e.key) == key || (key != null && key.equals(k)))) {
                size--;
                if (prev == e)
                    table[bucketIndex] = next;
                else
                    prev.next = next;
                return e;
            }
            prev = e;
            e = next;
        }

        return null;
    }

    private int secondaryHash(Object obj) {
        int h = obj.hashCode();
        h += (h << 15) ^ 0xffffcd7d;
        h ^= (h >>> 10);
        h += (h << 3);
        h ^= (h >>> 6);
        h += (h << 2) + (h << 14);
        return h ^ (h >>> 16);
    }

    /**
     * 通过 hashCode 计算 bucketIndex
     * @param h hashCode 值
     * @param length 表长
     * @return 计算得出的 bucketIndex
     *
     * */
    private int indexFor(int h, int length) {
        return h & (length - 1);
    }

    /**
     * 保存 Key Value 的实体 bean
     * */
    private static class MyEntry<K, V> {
        private final K key;
        private V value;
        private MyEntry<K, V> next;
        private int hash;

        MyEntry(int h, K key, V value, MyEntry<K, V> next) {
            this.hash = h;
            this.key = key;
            this.value = value;
            this.next = next;
        }

    }

}
