package javasourcecode.jvm.gc;

/**
 * @author Haoming Chen
 * Created on 2020/1/8
 */
public class NormalObject {
    public String name;

    public NormalObject(String name) {
        this.name = name;
    }

    @Override
    protected void finalize() {
        System.out.println("Finalizaing obj " + name);
    }
}
