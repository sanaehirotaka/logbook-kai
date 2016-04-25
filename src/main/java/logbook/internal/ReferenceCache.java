package logbook.internal;

import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * LRUアルゴリズムを実装した容量制限付きキャッシュ
 *
 */
public final class ReferenceCache<K, V> {

    private LRUCache<K, Reference<V>> cache;

    /**
     * キャッシュを構築します
     *
     * @param cacheSize キャッシュサイズ
     */
    public ReferenceCache(int cacheSize) {
        this.cache = new LRUCache<>(cacheSize);
    }

    /**
     * 指定されたキーがマップされている値を返します。
     * このキャッシュにそのキーのマッピングが含まれていない、または参照オブジェクトがクリアされている場合は
     * getterで指定された値と指定されたキーをこのキャッシュに関連付けます。
     *
     * @param key 関連付けられた値が返されるキー
     * @param getter 指定されたキーに関連付けられる値
     * @return 指定されたキーがマップされている値
     */
    public V get(K key, Function<K, V> getter) {
        Reference<V> r = this.cache.get(key);
        V value;
        if (r != null && (value = r.get()) != null) {
            return value;
        }
        value = getter.apply(key);
        this.cache.put(key, new SoftReference<>(value));
        return value;
    }

    @Override
    public String toString() {
        return this.cache.toString();
    }

    private static class LRUCache<K, V> extends LinkedHashMap<K, V> {

        private static final long serialVersionUID = 8863893985584717675L;

        private int cacheSize;

        public LRUCache(int cacheSize) {
            super(16, 0.75F, true);
            this.cacheSize = cacheSize;
        }

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return this.size() > this.cacheSize;
        }
    }
}
