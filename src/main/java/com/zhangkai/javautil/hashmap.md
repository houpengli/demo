# HashMap
- HashMap基本原理

 1、首先判断Key是否为Null，如果为null，直接查找Enrty[0]，如果不是Null，先计算Key的HashCode，然后经过二次Hash。得到Hash值，这里的Hash特征值是一个int值。

 2、根据Hash值，要找到对应的数组啊，所以对Entry[]的长度length求余，得到的就是Entry数组的index。

 3、找到对应的数组，就是找到了所在的链表，然后按照链表的操作对Value进行插入、删除和查询操作。



- HashMap概念介绍

变量	术语	说明
size	大小	HashMap的存储大小
threshold	临界值	HashMap大小达到临界值，需要重新分配大小。
loadFactor	负载因子	HashMap大小负载因子，默认为75%。
modCount	统一修改	HashMap被修改或者删除的次数总数。
Entry	实体	HashMap存储对象的实际实体，由Key，value，hash，next组成。


- HashMap初始化

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



- HashMap中的Hash计算和碰撞问题

HashMap的hash计算时先计算hashCode(),然后进行二次hash。代码如下：



// 计算二次Hash
int hash = hash(key.hashCode());

// 通过Hash找数组索引
int i = indexFor(hash, table.length);


先不忙着学习HashMap的Hash算法，先来看看JDK的String的Hash算法。代码如下：

复制代码
    /**
     * Returns a hash code for this string. The hash code for a
     * <code>String</code> object is computed as
     * <blockquote><pre>
     * s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]
     * </pre></blockquote>
     * using <code>int</code> arithmetic, where <code>s[i]</code> is the
     * <i>i</i>th character of the string, <code>n</code> is the length of
     * the string, and <code>^</code> indicates exponentiation.
     * (The hash value of the empty string is zero.)
     *
     * @return  a hash code value for this object.
     */
    public int hashCode() {
        int h = hash;
        if (h == 0 && value.length > 0) {
            char val[] = value;

            for (int i = 0; i < value.length; i++) {
                h = 31 * h + val[i];
            }
            hash = h;
        }
        return h;
    }
复制代码
从JDK的API可以看出，它的算法等式就是s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1]，其中s[i]就是索引为i的字符，n为字符串的长度。这里为什么有一个固定常量31呢，关于这个31的讨论很多，基本就是优化的数字，主要参考Joshua Bloch's Effective Java的引用如下：


The value 31 was chosen because it is an odd prime. If it were even and the multiplication overflowed, information would be lost, as multiplication by 2 is equivalent to shifting. The advantage of using a prime is less clear, but it is traditional. A nice property of 31 is that the multiplication can be replaced by a shift and a subtraction for better performance: 31 * i == (i << 5) - i. Modern VMs do this sort of optimization automatically.

大体意思是说选择31是因为它是一个奇素数，如果它做乘法溢出的时候，信息会丢失，而且当和2做乘法的时候相当于移位，在使用它的时候优点还是不清楚，但是它已经成为了传统的选择，31的一个很好的特性就是做乘法的时候可以被移位和减法代替的时候有更好的性能体现。例如31*i相当于是i左移5位减去i，即31*i == (i<<5)-i。现代的虚拟内存系统都使用这种自动优化。



现在进入正题，HashMap为什么还要做二次hash呢? 代码如下：

复制代码
    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
复制代码
回答这个问题之前，我们先来看看HashMap是怎么通过Hash查找数组的索引的。

复制代码
    /**
     * Returns index for hash code h.
     */
    static int indexFor(int h, int length) {
        return h & (length-1);
    }
复制代码
其中h是hash值，length是数组的长度，这个按位与的算法其实就是h%length求余，一般什么情况下利用该算法，典型的分组。例如怎么将100个数分组16组中，就是这个意思。应用非常广泛。



既然知道了分组的原理了，那我们看看几个例子，代码如下：

复制代码
        int h=15,length=16;
        System.out.println(h & (length-1));
        h=15+16;
        System.out.println(h & (length-1));
        h=15+16+16;
        System.out.println(h & (length-1));
        h=15+16+16+16;
        System.out.println(h & (length-1));
复制代码
运行结果都是15，为什么呢?我们换算成二进制来看看。

复制代码
System.out.println(Integer.parseInt("0001111", 2) & Integer.parseInt("0001111", 2));

System.out.println(Integer.parseInt("0011111", 2) & Integer.parseInt("0001111", 2));

System.out.println(Integer.parseInt("0111111", 2) & Integer.parseInt("0001111", 2));

System.out.println(Integer.parseInt("1111111", 2) & Integer.parseInt("0001111", 2));
复制代码
这里你就发现了，在做按位与操作的时候，后面的始终是低位在做计算，高位不参与计算，因为高位都是0。这样导致的结果就是只要是低位是一样的，高位无论是什么，最后结果是一样的，如果这样依赖，hash碰撞始终在一个数组上，导致这个数组开始的链表无限长，那么在查询的时候就速度很慢，又怎么算得上高性能的啊。所以hashmap必须解决这样的问题，尽量让key尽可能均匀的分配到数组上去。避免造成Hash堆积。



回到正题，HashMap怎么处理这个问题，怎么做的二次Hash。

复制代码
    static int hash(int h) {
        // This function ensures that hashCodes that differ only by
        // constant multiples at each bit position have a bounded
        // number of collisions (approximately 8 at default load factor).
        h ^= (h >>> 20) ^ (h >>> 12);
        return h ^ (h >>> 7) ^ (h >>> 4);
    }
复制代码
这里就是解决Hash的的冲突的函数，解决Hash的冲突有以下几种方法：

   (1)、开放定址法（线性探测再散列，二次探测再散列，伪随机探测再散列）

　(2)、再哈希法

   (3)、链地址法

   (4)、建立一 公共溢出区

  而HashMap采用的是链地址法，这几种方法在以后的博客会有单独介绍，这里就不做介绍了。



- HashMap的put()解析

以上说了一些基本概念，下面该进入主题了，HashMap怎么存储一个对象的，代码如下：



复制代码
 /**
     * Associates the specified value with the specified key in this map.
     * If the map previously contained a mapping for the key, the old
     * value is replaced.
     *
     * @param key key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>.)
     */
    public V put(K key, V value) {
        if (key == null)
            return putForNullKey(value);
        int hash = hash(key.hashCode());
        int i = indexFor(hash, table.length);
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
复制代码


从代码可以看出，步骤如下：

  (1) 首先判断key是否为null，如果是null，就单独调用putForNullKey(value)处理。代码如下：



复制代码
    /**
     * Offloaded version of put for null keys
     */
    private V putForNullKey(V value) {
        for (Entry<K,V> e = table[0]; e != null; e = e.next) {
            if (e.key == null) {
                V oldValue = e.value;
                e.value = value;
                e.recordAccess(this);
                return oldValue;
            }
        }
        modCount++;
        addEntry(0, null, value, 0);
        return null;
    }
复制代码
从代码可以看出，如果key为null的值，默认就存储到table[0]开头的链表了。然后遍历table[0]的链表的每个节点Entry，如果发现其中存在节点Entry的key为null，就替换新的value，然后返回旧的value，如果没发现key等于null的节点Entry，就增加新的节点。

  (2) 计算key的hashcode，再用计算的结果二次hash，通过indexFor(hash, table.length);找到Entry数组的索引i。

  (3) 然后遍历以table[i]为头节点的链表，如果发现有节点的hash，key都相同的节点时，就替换为新的value，然后返回旧的value。

  (4) modCount是干嘛的啊? 让我来为你解答。众所周知，HashMap不是线程安全的，但在某些容错能力较好的应用中，如果你不想仅仅因为1%的可能性而去承受hashTable的同步开销，HashMap使用了Fail-Fast机制来处理这个问题，你会发现modCount在源码中是这样声明的。

    transient volatile int modCount;
volatile关键字声明了modCount，代表了多线程环境下访问modCount，根据JVM规范，只要modCount改变了，其他线程将读到最新的值。其实在Hashmap中modCount只是在迭代的时候起到关键作用。

复制代码
private abstract class HashIterator<E> implements Iterator<E> {
        Entry<K,V> next;    // next entry to return
        int expectedModCount;    // For fast-fail
        int index;        // current slot
        Entry<K,V> current;    // current entry

        HashIterator() {
            expectedModCount = modCount;
            if (size > 0) { // advance to first entry
                Entry[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
        }

        public final boolean hasNext() {
            return next != null;
        }

        final Entry<K,V> nextEntry() {
　　　　　　　　// 这里就是关键
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            Entry<K,V> e = next;
            if (e == null)
                throw new NoSuchElementException();

            if ((next = e.next) == null) {
                Entry[] t = table;
                while (index < t.length && (next = t[index++]) == null)
                    ;
            }
        current = e;
            return e;
        }

        public void remove() {
            if (current == null)
                throw new IllegalStateException();
            if (modCount != expectedModCount)
                throw new ConcurrentModificationException();
            Object k = current.key;
            current = null;
            HashMap.this.removeEntryForKey(k);
            expectedModCount = modCount;
        }

    }
复制代码
使用Iterator开始迭代时，会将modCount的赋值给expectedModCount，在迭代过程中，通过每次比较两者是否相等来判断HashMap是否在内部或被其它线程修改，如果modCount和expectedModCount值不一样，证明有其他线程在修改HashMap的结构，会抛出异常。

所以HashMap的put、remove等操作都有modCount++的计算。

  (5)  如果没有找到key的hash相同的节点，就增加新的节点addEntry(),代码如下：

复制代码
  void addEntry(int hash, K key, V value, int bucketIndex) {
    Entry<K,V> e = table[bucketIndex];
        table[bucketIndex] = new Entry<K,V>(hash, key, value, e);
        if (size++ >= threshold)
            resize(2 * table.length);
    }
复制代码
这里增加节点的时候取巧了，每个新添加的节点都增加到头节点，然后新的头节点的next指向旧的老节点。

(6) 如果HashMap大小超过临界值，就要重新设置大小，扩容，见第9节内容。



- HashMap的get()解析

理解上面的put，get就很好理解了。代码如下：



复制代码
    public V get(Object key) {
        if (key == null)
            return getForNullKey();
        int hash = hash(key.hashCode());
        for (Entry<K,V> e = table[indexFor(hash, table.length)];
             e != null;
             e = e.next) {
            Object k;
            if (e.hash == hash && ((k = e.key) == key || key.equals(k)))
                return e.value;
        }
        return null;
    }
复制代码


别看这段代码，它带来的问题是巨大的，千万记住,HashMap是非线程安全的，所以这里的循环会导致死循环的。为什么呢?当你查找一个key的hash存在的时候，进入了循环，恰恰这个时候，另外一个线程将这个Entry删除了，那么你就一直因为找不到Entry而出现死循环，最后导致的结果就是代码效率很低，CPU特别高。一定记住。





- HashMap的size()解析

HashMap的大小很简单，不是实时计算的，而是每次新增加Entry的时候，size就递增。删除的时候就递减。空间换时间的做法。因为它不是线程安全的。完全可以这么做。效力高。

- HashMap的reSize()解析

当HashMap的大小超过临界值的时候，就需要扩充HashMap的容量了。代码如下：



复制代码
    void resize(int newCapacity) {
        Entry[] oldTable = table;
        int oldCapacity = oldTable.length;
        if (oldCapacity == MAXIMUM_CAPACITY) {
            threshold = Integer.MAX_VALUE;
            return;
        }

        Entry[] newTable = new Entry[newCapacity];
        transfer(newTable);
        table = newTable;
        threshold = (int)(newCapacity * loadFactor);
    }
复制代码


从代码可以看出，如果大小超过最大容量就返回。否则就new 一个新的Entry数组，长度为旧的Entry数组长度的两倍。然后将旧的Entry[]复制到新的Entry[].代码如下：

复制代码
    void transfer(Entry[] newTable) {
        Entry[] src = table;
        int newCapacity = newTable.length;
        for (int j = 0; j < src.length; j++) {
            Entry<K,V> e = src[j];
            if (e != null) {
                src[j] = null;
                do {
                    Entry<K,V> next = e.next;
                    int i = indexFor(e.hash, newCapacity);
                    e.next = newTable[i];
                    newTable[i] = e;
                    e = next;
                } while (e != null);
            }
        }
    }
复制代码
在复制的时候数组的索引int i = indexFor(e.hash, newCapacity);重新参与计算。