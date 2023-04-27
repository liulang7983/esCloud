package com.tuling.jvm;

import java.io.FileInputStream;
import java.lang.reflect.Method;

/**
 * @author ming.li
 * @date 2022/12/9 20:00
 */
public class MyClassLoaderTest {
    static class MyClassLoader extends ClassLoader {
        private String classPath;

        public MyClassLoader(String classPath) {
            this.classPath = classPath;
        }

        private byte[] loadByte(String name) throws Exception {
            name = name.replaceAll("\\.", "/");
            FileInputStream fis = new FileInputStream(classPath + "/" + name
                    + ".class");
            int len = fis.available();
            byte[] data = new byte[len];
            fis.read(data);
            fis.close();
            return data;
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException {
            try {
                byte[] data = loadByte(name);
                //defineClass将一个字节数组转为Class对象，这个字节数组是class文件读取后最终的字节数组。
                return defineClass(name, data, 0, data.length);
            } catch (Exception e) {
                e.printStackTrace();
                throw new ClassNotFoundException();
            }
        }

        @Override
        protected Class<?> loadClass(String name, boolean resolve)
                throws ClassNotFoundException
        {
            synchronized (getClassLoadingLock(name)) {
                // First, check if the class has already been loaded
                Class<?> c = findLoadedClass(name);
                if (c == null) {
                    long t0 = System.nanoTime();
                    //屏蔽双亲委派代码，让他直接找到自己的findClass
                    /*try {
                        if (parent != null) {
                            c = parent.loadClass(name, false);
                        } else {
                            c = findBootstrapClassOrNull(name);
                        }
                    } catch (ClassNotFoundException e) {
                        // ClassNotFoundException thrown if class not found
                        // from the non-null parent class loader
                    }*/

                    if (c == null) {
                        // If still not found, then invoke findClass in order
                        // to find the class.
                        long t1 = System.nanoTime();
                        if (!name.startsWith("com.tuling.jvm")){
                            c=this.getParent().loadClass(name);
                        }else {
                            c = findClass(name);
                        }


                        // this is the defining class loader; record the stats
                        sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
                        sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
                        sun.misc.PerfCounter.getFindClasses().increment();
                    }
                }
                if (resolve) {
                    resolveClass(c);
                }
                return c;
            }
        }

    }


    public static void main(String args[]) throws Exception {
        //初始化自定义类加载器，会先初始化父类ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器AppClassLoader
        MyClassLoader classLoader = new MyClassLoader("D:/t1");
        //D盘创建 t1 几级目录，将User类的复制类User.class丢入该目录
        Class clazz = classLoader.loadClass("com.tuling.jvm.User");
        Object obj = clazz.newInstance();
        Method method = clazz.getDeclaredMethod("test", null);
        method.invoke(obj, null);
        System.out.println(clazz.getClassLoader().getClass().getName());

        //初始化自定义类加载器，会先初始化父类ClassLoader，其中会把自定义类加载器的父加载器设置为应用程序类加载器AppClassLoader
        MyClassLoader classLoader1 = new MyClassLoader("D:/t2");
        //D盘创建 t1 几级目录，将User类的复制类User.class丢入该目录
        Class clazz1 = classLoader1.loadClass("com.tuling.jvm.User");
        Object obj1 = clazz1.newInstance();
        Method method1 = clazz1.getDeclaredMethod("test", null);
        method1.invoke(obj1, null);
        System.out.println(clazz1.getClassLoader().getClass().getName());
    }
}
