package javasourcecode.stacktrace;

/**
 * 打印当前堆栈信息源码分析
 * 工具类: 被调用的类
 *
 * @author Haoming Chen
 * Created on 2019/11/23
 */
public class ToolFunc {
    //利用Throwble打印堆栈信息
    static protected void ThrowablePST() {
        //获得栈信息元素
        StackTraceElement[] stackTraceElements = new Throwable().getStackTrace();
        System.out.println("*************ThrowablePST*************");
        for (StackTraceElement se : stackTraceElements) {
            //打印堆栈信息
            System.out.println(se);
        }
        System.out.println(stackTraceElements[1].getClassName());
    }

    //利用Thread打印堆栈信息
    //if (this != Thread.currentThread()) {...} -> Thread.currentThread为主线程 this不等于主线程类
    //即调用else {
    // Don't need JVM help for current thread
    //            return (new Exception()).getStackTrace();
    //        }
    //打印了java.lang.Thread.getStackTrace(Thread.java:1559)
    static protected void ThreadPST() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        System.out.println("*************ThreadPST*************");
        for (StackTraceElement se : stackTraceElements) {
            //打印堆栈信息
            System.out.println(se);
        }
        System.out.println(stackTraceElements[2].getClassName());
    }

    //利用Exception打印堆栈信息
    //Excepion为Throwable的子类, 即调用Throwable的getStackTrace
    static protected void ExceptionPST() {
        StackTraceElement[] stackTraceElements = new Exception().getStackTrace();
        System.out.println("*************ExceptionPST*************");
        for (StackTraceElement se : stackTraceElements) {
            //打印堆栈信息
            System.out.println(se);
        }
        System.out.println(stackTraceElements[1]);
    }

}
