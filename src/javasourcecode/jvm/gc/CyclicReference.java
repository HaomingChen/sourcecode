package javasourcecode.jvm.gc;

/**
 * 循环引用: 若使用引用计数来判定对象是否为垃圾, 循环引用会造成引用计数值永远不会为0
 * 该对象将不会被回收
 *
 * @author Haoming Chen
 * Created on 2020/1/8
 */
public class CyclicReference {

    public static void main(String[] args) {
        MyObject a = new MyObject();
        MyObject b = new MyObject();
        b = a;
        a = b;
    }

}
