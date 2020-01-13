package javasourcecode.jvm.gc;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

/**
 * 强引用, 软引用，弱引用，虚引用
 * @author Haoming Chen
 * Created on 2020/1/8
 */
public class StrongReferenceSoftReference {
    String str = new String("abc");//强引用
    SoftReference<String> softRef = new SoftReference<String>(str);//软引用: 内存紧缺时回收
    WeakReference<String> abcWeakRef = new WeakReference<String>(str);//弱引用
    String strA = new String("ccd");
    ReferenceQueue queue = new ReferenceQueue();//引用队列
    PhantomReference ref = new PhantomReference(strA, queue);//虚引用 一般用于跟踪垃圾收集器回收的活动



}
