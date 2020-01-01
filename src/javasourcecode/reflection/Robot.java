package javasourcecode.reflection;

/**
 * @author 58212
 * @date 2020-01-01 23:32
 */
public class Robot {
    private String name;
    public void sayHi(String helloSentence){
        System.out.println(helloSentence + " " + name);
    }
    private String throwHello(String tag){
        return "Hello " + tag;
    }
}
