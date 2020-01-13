package javasourcecode.jvm.model;


/**
 * @author Haoming Chen
 * Created on 2020/1/5
 */
public class ByteCodeSample {

    //局部变量表(存储局部变量) + JVM指令(operator) -> 逆波兰表达式
    //操作数栈 -> 逆波兰表达式用于运算的stack
    public static int add(int a, int b){
        int c = 0;
        c = a + b;
        return c;
    }

}
