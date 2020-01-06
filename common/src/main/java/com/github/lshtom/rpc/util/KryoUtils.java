package com.github.lshtom.rpc.util;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.objenesis.strategy.StdInstantiatorStrategy;

/**
 * Kryo工具类
 *
 * @description 对Kryo库进行简单封装，以便更为简单的使用
 * @author lshtom
 * @date 2020/1/6
 * @version 1.0.0
 */
public class KryoUtils {

    private static final ThreadLocal<Kryo> kryoLocal = ThreadLocal.withInitial(()->{
        Kryo kryo = new Kryo();
        // 设置为无需注册类
        kryo.setRegistrationRequired(false);

        // 支持对象循环引入
        kryo.setReferences(true);

        // 解决反序列化集合时出现的NPE的bug
        ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
            .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
        return kryo;
    });
    private static final int DEFAULT_BUFFER_SIZE = 8192;

    /**
     * 从字节数组反序列化到任意对象,配合{@link KryoUtils#writeObjectWithoutType(java.lang.Object)}使用
     *
     * @param bytes 字节数组
     * @return 反序列化后得到的对象实例
     */
    public static <T> T readObject(byte[] bytes) {
        Kryo kryo = kryoLocal.get();
        Input input = new Input(bytes);
        return (T) kryo.readClassAndObject(input);
    }

    /**
     * 从字节数组反序列化到指定类型的对象,配合{@link KryoUtils#writeObjectWithoutType(java.lang.Object)}使用
     *
     * @param bytes 字节数组
     * @param type  对象类型
     * @return 反序列化后得到的对象实例
     */
    public static <T> T readObjectByType(byte[] bytes, Class<T> type) {
        Kryo kryo = kryoLocal.get();
        Input input = new Input(bytes);
        return kryo.readObject(input, type);
    }

    /**
     * 将对象序列化为字节数组，且字节数组中不含有类型信息,配合{@link KryoUtils#readObjectByType(byte[], java.lang.Class)}使用
     *
     * @param object 对象实例
     * @return 字节数组
     * @description 不含对象类型信息意味着序列化后的字节数组大小更小
     */
    public static <T> byte[] writeObjectWithoutType(T object) {
        Kryo kryo = kryoLocal.get();
        Output output = new Output(new byte[DEFAULT_BUFFER_SIZE], -1);
        kryo.writeObject(output, object);
        return output.toBytes();
    }

    /**
     * 将对象序列化为字节数组，但含有类型信息,配合{@link KryoUtils#readObject(byte[])}使用
     *
     * @param object 对象实例
     * @return 字节数组
     * @description 含有对象类型信息意味着序列化后的字节数组大小要更大
     */
    public static <T> byte[] writeObjectWithType(T object) {
        Kryo kryo = kryoLocal.get();
        Output output = new Output(new byte[DEFAULT_BUFFER_SIZE], -1);
        kryo.writeClassAndObject(output, object);
        return output.toBytes();
    }
}
