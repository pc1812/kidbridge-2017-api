package net.$51zhiyuan.development.kidbridge.test.service;

import net.$51zhiyuan.development.kidbridge.ui.model.User;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflex {

    @Test
    public void test() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class clazz = User.class;
        Method method = clazz.getMethod("helloworld",null);
        method.invoke(clazz.newInstance(),null);
    }
}
