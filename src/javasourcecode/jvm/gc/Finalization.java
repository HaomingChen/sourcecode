package javasourcecode.jvm.gc;

/**
 * finaliza重写
 *
 * @author Haoming Chen
 * Created on 2020/1/8
 */
public class Finalization {
    public static Finalization finalization;

    public static boolean checkIfRecyclied = false;
    @Override
    protected void finalize(){
        System.out.println("Finalized");
        finalization = this;
    }

    public static void main(String[] args) {
        Finalization f = new Finalization();
        System.out.println("First print: " + f);
        //将对new Finalization的引用取消
        f = null;
        System.gc();
        try{
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Second print: " + f);
        System.out.println(f.finalization);
    }
}
