package niochatroom;
import java.io.IOException;

/**
 * @author 58212
 * @date 2019-11-16 0:18
 */
public class NioClientB {
    public static void main(String[] args) throws IOException {
        new NioClient().start("clientB");
    }
}
