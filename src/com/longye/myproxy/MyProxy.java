package com.longye.myproxy;


import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Created by tianl on 2018/9/4.
 */
public class MyProxy {

    public static Object newProxyInstance(MyClassLoader classLoader, Class<?>[] interfaces, MyInvocationHandler h){

        try {
            //1、动态组装代理类的源代码
            //这里为了简,就不用数组了,只取该数组的第一个
            String proxySrc = generatorProxySrc(interfaces[0]);

            //2、生成java文件
            String path = MyProxy.class.getResource("").getPath();
            File file = new File(path + "$Proxy0.java");
            FileWriter fw = new FileWriter(file);
            fw.write(proxySrc);
            fw.flush();
            fw.close();

            //3、编译源代码,生成.class文件
            JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
            StandardJavaFileManager manager = javaCompiler.getStandardFileManager(null, null, null);
            Iterable iterable = manager.getJavaFileObjects(file);
            JavaCompiler.CompilationTask task = javaCompiler.getTask(null, manager, null, null, null, iterable);
            task.call();
            manager.close();

            //4、将class文件中的内容,动态加载至JVM中
            Class proxyClass = classLoader.findClass("$Proxy0");

            //5、返回代理对象
            Constructor c = proxyClass.getConstructor(MyInvocationHandler.class);
            file.delete();
            return c.newInstance(h);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String generatorProxySrc(Class<?> interfaces) {
        String ln = "\r\n";
        StringBuilder src = new StringBuilder();

        src.append("package com.longye.myproxy;").append(ln)
                .append("import java.lang.reflect.Method;").append(ln)
                .append("public class $Proxy0 implements ").append(interfaces.getName()).append("{").append(ln)
                .append("private MyInvocationHandler h;").append(ln)
                .append("public $Proxy0(MyInvocationHandler h) {").append(ln)
                .append("this.h = h;").append(ln)
                .append("}").append(ln);

        for (Method method : interfaces.getMethods()) {
            src.append("public ").append(method.getReturnType()).append(" ").append(method.getName()).append("() {").append(ln)
                    .append("try {").append(ln)
                    .append("Method m = ").append(interfaces.getName()).append(".class.getMethod(\"").append(method.getName()).append("\");").append(ln)
                    .append("this.h.invoke(this, m, new Object[]{});").append(ln)
                    .append("}catch (Throwable e){").append(ln)
                    .append("e.printStackTrace();").append(ln)
                    .append("}").append(ln)
                    .append("}").append(ln);
        }

        src.append("}");
        return src.toString();
    }
}
