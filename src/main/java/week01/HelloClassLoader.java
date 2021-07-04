package week01;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Created by xiaowu
 * @date 2021/6/22.
 */
public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        HelloClassLoader classLoader = new HelloClassLoader();
        Class<?> clazz = classLoader.findClass("Hello");
        Method method = clazz.getDeclaredMethod("hello");
        method.invoke(clazz.newInstance());
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        Path path = Paths.get(System.getProperty("user.dir"), "src/jvm/Hello.xlass");
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage(), e);
        }
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) (255 - bytes[i]);
        }

        return defineClass(name, bytes, 0, bytes.length);
    }
}
