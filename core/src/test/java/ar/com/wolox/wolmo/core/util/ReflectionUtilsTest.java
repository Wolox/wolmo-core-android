/*
 * Copyright (c) Wolox S.A
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ar.com.wolox.wolmo.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import android.support.v4.app.Fragment;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ReflectionUtilsTest {

    @Test
    public void getClassNameForInterface() throws ClassNotFoundException {
        assertThat(ReflectionUtils.getClassName(List.class)).isEqualTo(List.class.getCanonicalName());
        assertThat(ReflectionUtils.getClassName(Map.class)).isEqualTo(Map.class.getCanonicalName());
    }

    @Test
    public void getClassNameForClass() throws ClassNotFoundException {
        assertThat(ReflectionUtils.getClassName(LinkedList.class)).isEqualTo(LinkedList.class.getCanonicalName());
        assertThat(ReflectionUtils.getClassName(HashMap.class)).isEqualTo(HashMap.class.getCanonicalName());
    }

    @Test
    public void getClassShouldReturnClassInstance() throws ClassNotFoundException {
        Type type = String.class.getGenericInterfaces()[2];
        assertThat(ReflectionUtils.getClass(type)).isEqualTo(CharSequence.class);
        assertThat(ReflectionUtils.getClass(null)).isNull();
    }

    @Test
    public void getInstanceShouldCreateNewObject()
            throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        Type type = Fragment.class;
        assertThat(ReflectionUtils.newInstance(type)).isExactlyInstanceOf(Fragment.class);
        assertThat(ReflectionUtils.newInstance(null)).isNull();
    }

    @Test
    @SuppressWarnings({"MismatchedQueryAndUpdateOfCollection", "ConstantConditions"})
    public void getParameterizedTypes() throws ClassNotFoundException {
        TestClass testClass = new TestClass();
        Type[] types = ReflectionUtils.getParameterizedTypes(testClass);

        assertThat(types).hasSize(2);
        assertThat(Long.class.isAssignableFrom(ReflectionUtils.getClass(types[0])));
        assertThat(Double.class.isAssignableFrom(ReflectionUtils.getClass(types[1])));
        assertThat(ReflectionUtils.getParameterizedTypes(String.class)).isNull();
    }

    @Test
    public void hasDefaultConstructor() {
        assertThat(ReflectionUtils.hasDefaultConstructor(Fragment.class)).isTrue();
        assertThat(ReflectionUtils.hasDefaultConstructor(Integer.class)).isFalse();
    }

    @Test
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public void getFieldClass() {
        TestClass testClass = new TestClass();
        assertThat(ReflectionUtils.getFieldClass(testClass.getClass(), "field1")).isEqualTo(String.class);
        assertThat(ReflectionUtils.getFieldClass(testClass.getClass(), "field2")).isEqualTo(Integer.class);
        assertThat(ReflectionUtils.getFieldClass(null, "field2")).isNull();
    }

    @Test
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    public void getMethodReturnType() {
        TestClass testClass = new TestClass();
        assertThat(ReflectionUtils.getMethodReturnType(testClass.getClass(), "getNumber")).isEqualTo(Float.class);
        assertThat(ReflectionUtils.getMethodReturnType(null, "getNumber")).isNull();
    }

    @SuppressWarnings("unused")
    public static class TestClass extends HashMap<Long, Double> {
        String field1;
        Integer field2;

        public Float getNumber() {
            return 0F;
        }
    }
}
