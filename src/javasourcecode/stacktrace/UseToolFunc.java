package javasourcecode.stacktrace;

/**
 * 打印当前堆栈信息源码分析
 * 调用工具类的类
 * @author Haoming Chen
 * Created on 2019/11/23
 */
public class UseToolFunc {

    public static void main(String[] args) {
        ToolFunc.ExceptionPST();
        //Thread多打印了Thread的堆栈信息
        ToolFunc.ThreadPST();
        ToolFunc.ThrowablePST();
    }
}
