package javasourcecode.classloader;

/**
 * @author 58212
 * @date 2020-01-03 1:29
 */
public class ClassLoaderChecker {
    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        MyClassLoader m = new MyClassLoader("D:/360MoveData/Users/58212/Desktop/","pixiv");
        Class c = m.loadClass("Wali");
        System.out.println(c.getClassLoader());
        c.newInstance();
    }
}
