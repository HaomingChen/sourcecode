package javasourcecode.threadlocal;

/**
 * @author 58212
 * @date 2020-01-26 22:46
 */
public class Basic {

    public static ThreadLocal<Long> x = new ThreadLocal<Long>() {
        @Override
        protected Long initialValue() {
            System.out.println("Initial Value run..");
            return Thread.currentThread().getId();
        }
    };

    public static void main(String[] argv) {
        new Thread() {
            @Override
            public void run() {
                System.out.println(x.get());
            }
        }.start();
        x.set(107l);
        x.remove();
        System.out.println(x.get());
    }
}
