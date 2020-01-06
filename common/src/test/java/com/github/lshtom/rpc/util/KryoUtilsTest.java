package com.github.lshtom.rpc.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Date;
import java.util.Objects;

/**
* Kryo工具类单元测试
*
* @description 对编写的Kryo工具类进行单元测试
* @author lshtom
* @date 2020/1/6
* @version 1.0.0
*/
public class KryoUtilsTest {

    /**
     * 测试无需提供类型信息的对象序列化与反序列化
     */
    @Test
    public void testReadAndWriteObjectWithoutType() {
        TestObj originObj = new TestObj("123", 456, new Date());
        byte[] bytes = KryoUtils.writeObjectWithType(originObj);
        TestObj targetObj = KryoUtils.readObject(bytes);
        Assert.assertEquals(originObj, targetObj);
    }

    /**
     * 测试需要提供类型信息的对象序列化与反序列化
     */
    @Test
    public void testReadAndWriteObjectWithType() {
        TestObj originObj = new TestObj("123", 456, new Date());
        byte[] bytes = KryoUtils.writeObjectWithoutType(originObj);
        TestObj targetObj = KryoUtils.readObjectByType(bytes, TestObj.class);
        Assert.assertEquals(originObj, targetObj);
    }

    private static class TestObj {
        private String code;
        private int num;
        private Date date;

        public TestObj() {
        }

        public TestObj(String code, int num, Date date) {
            this.code = code;
            this.num = num;
            this.date = date;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof TestObj)) return false;
            TestObj testObj = (TestObj) obj;
            return num == testObj.num &&
                Objects.equals(code, testObj.code) &&
                Objects.equals(date, testObj.date);
        }

        @Override
        public int hashCode() {
            return Objects.hash(code, num, date);
        }
    }
}
