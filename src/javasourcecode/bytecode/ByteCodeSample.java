package javasourcecode.bytecode;

/**
 * @author 58212
 * @date 2020-01-01 23:21
 */
public class ByteCodeSample {
    public static void main(String[] args) throws ClassNotFoundException {
        Class.forName("12");
        int i = 1, j = 5;
        i++;
        ++j;
        System.out.println(i);
        System.out.println(j);
    }
}
