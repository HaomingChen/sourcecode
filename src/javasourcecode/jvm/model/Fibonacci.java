package javasourcecode.jvm.model;

/**
 * @author Haoming Chen
 * Created on 2020/1/5
 */
public class Fibonacci {

    public static void main(String[] args) {
        System.out.println(Fibonacci.getFibo(5));
    }

    public static int getFibo(int index) {
        if (index == 0 || index == 1) {
            return 1;
        }
        return getFibo(index - 1) + getFibo(index - 2);
    }

}
