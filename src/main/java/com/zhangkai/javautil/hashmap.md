# HashMap
-- HashMap基本原理
    1、首先判断Key是否为Null，如果为null，直接查找Enrty[0]，如果不是Null，先计算Key的HashCode，然后经过二次Hash。得到Hash值，这里的Hash特征值是一个int值。
    2、根据Hash值，要找到对应的数组啊，所以对Entry[]的长度length求余，得到的就是Entry数组的index。
    3、找到对应的数组，就是找到了所在的链表，然后按照链表的操作对Value进行插入、删除和查询操作。
    public V put(K key, V value) {
            if (table == EMPTY_TABLE) {
                inflateTable(threshold);
            }
            if (key == null) //1.null值处理
                return putForNullKey(value);
            int hash = hash(key); //2.hash计算
            int i = indexFor(hash, table.length);//3.计算索引
            for (Entry<K,V> e = table[i]; e != null; e = e.next) {
                Object k;
                if (e.hash == hash && ((k = e.key) == key || key.equals(k))) {
                    V oldValue = e.value;
                    e.value = value;
                    e.recordAccess(this);
                    return oldValue;
                }
            }

            modCount++;
            addEntry(hash, key, value, i);
            return null;
        }
- HashMap概念介绍

  变量	术语	说明
  size	大小	HashMap的存储大小
  threshold	临界值	HashMap大小达到临界值，需要重新分配大小。
  loadFactor	负载因子	HashMap大小负载因子，默认为75%。
  modCount	统一修改	HashMap被修改或者删除的次数总数。
  Entry	实体	HashMap存储对象的实际实体，由Key，value，hash，next组成。
- HashMap初始化和扩展
默认情况下，大多数人都调用new HashMap()来初始化的，我在这里分析new HashMap(int initialCapacity, float loadFactor)的构造函数，代码如下：

复制代码
public HashMap(int initialCapacity, float loadFactor) {
　　　　　// initialCapacity代表初始化HashMap的容量，它的最大容量是MAXIMUM_CAPACITY = 1 << 30。
        if (initialCapacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                                               initialCapacity);
        if (initialCapacity > MAXIMUM_CAPACITY)
            initialCapacity = MAXIMUM_CAPACITY;

　　　　 // loadFactor代表它的负载因子，默认是是DEFAULT_LOAD_FACTOR=0.75，用来计算threshold临界值的。
        if (loadFactor <= 0 || Float.isNaN(loadFactor))
            throw new IllegalArgumentException("Illegal load factor: " +
                                               loadFactor);

        // Find a power of 2 >= initialCapacity
        int capacity = 1;
        while (capacity < initialCapacity)
            capacity <<= 1;

        this.loadFactor = loadFactor;
        threshold = (int)(capacity * loadFactor);
        table = new Entry[capacity];
        init();
    }
复制代码
由上面的代码可以看出，初始化的时候需要知道初始化的容量大小，因为在后面要通过按位与的Hash算法计算Entry数组的索引，那么要求Entry的数组长度是2的N次方。

- key的hash计算方法: 利用Object的hashcode方法, 然后在进行位移和位运算 将数据散列
    final int hash(Object k) {
        int h = hashSeed;
        if (0 != h && k instanceof String) {
            return sun.misc.Hashing.stringHash32((String) k);
        }

        h ^= k.hashCode();

        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }

- HashMap是怎么通过Hash查找数组的索引的。

  复制代码
      /**
       * Returns index for hash code h.
       */
      static int indexFor(int h, int length) {
          return h & (length-1);
      }
  复制代码
  其中h是hash值，length是数组的长度，这个按位与的算法其实就是h%length求余，一般什么情况下利用该算法，典型的分组。例如怎么将100个数分组16组中，就是这个意思。应用非常广泛。