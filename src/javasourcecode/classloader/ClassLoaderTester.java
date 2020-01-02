package javasourcecode.classloader;

/**
 * @author 58212
 * @date 2020-01-03 0:46
 */
public class ClassLoaderTester {
    public static void main(String[] args) {
        String var0 = System.getProperty("java.ext.dirs");
        String var1 = System.getProperty("java.class.path");
//        System.out.println(var0);
        System.out.println(var1);
    }
}
