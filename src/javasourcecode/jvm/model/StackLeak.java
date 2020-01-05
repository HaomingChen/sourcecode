package javasourcecode.jvm.model;

/**
 * @author Haoming Chen
 * Created on 2020/1/5
 */
public class StackLeak {

    public void stackLeakByThread(){
        while(true){
            new Thread(() -> {
                while(true){
                }
            }).start();
        }
    }
}
