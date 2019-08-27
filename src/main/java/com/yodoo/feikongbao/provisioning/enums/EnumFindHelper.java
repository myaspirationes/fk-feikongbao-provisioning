package com.yodoo.feikongbao.provisioning.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description 枚举查找类 : 参考：http://www.cnblogs.com/doubleten/p/3678379.html
 * @Author jinjun_luo
 * @Date 2019/4/11 9:33
 **/
public class EnumFindHelper<E extends Enum<E>, K> {

    private final HashMap<K, E> cachedMap;

    private EnumFindHelper(Class<E> clazz, Function<E, K> keyMapper) {
        EnumSet<E> enumElements = EnumSet.allOf(clazz);
        cachedMap = new HashMap<>(enumElements.size());
        enumElements.stream()
                .collect(Collectors.toMap(keyMapper, e -> e,
                                (l, r) -> null == l ? r : l,
                                () -> cachedMap));
    }
    
    static <E extends Enum<E>, K> EnumFindHelper<E, K> of(Class<E> clazz, Function<E, K> keyMapper) {
        return new EnumFindHelper<>(clazz, keyMapper);
    }
    
    E find(K key, E identity) {
        E r = cachedMap.get(key);
        if (null == r) {
            return identity;
        }
        return r;
    }
    
}
