package javasourcecode.classloader;

import static com.sun.beans.finder.ClassFinder.findClass;

/**
 * @author 58212
 * @date 2020-01-03 0:39
 */
public class ClassLoaderQ {

//    protected Class<?> loadClass(String name, boolean resolve)
//            throws ClassNotFoundException {
//        synchronized (getClassLoadingLock(name)) {
//            // First, check if the class has already been loaded
//            Class<?> c = findLoadedClass(name);
//            if (c == null) {
//                long t0 = System.nanoTime();
//                try {
//                    //if this class loader still has parent class loader
//                    if (parent != null) {
//                        //invoking parent classLoader's loadClass method
//                    } else {
//                        //check if BootstrapClassLoader loaded this class
//                        c = findBootstrapClassOrNull(name);
//                    }
//                }
//
//                if (c == null) {
//                    // If still not found, then invoke findClass in order
//                    // to find the class.
//                    long t1 = System.nanoTime();
//                    c = findClass(name);
//
//                    // this is the defining class loader; record the stats
//                    sun.misc.PerfCounter.getParentDelegationTime().addTime(t1 - t0);
//                    sun.misc.PerfCounter.getFindClassTime().addElapsedTimeFrom(t1);
//                    sun.misc.PerfCounter.getFindClasses().increment();
//                }
//            }
//            if (resolve) {
//                resolveClass(c);
//            }
//            return c;
//        }
//    }

    private ClassLoader parent;

    private void resolveClass(Class<?> c) {
    }

    private Class<?> findBootstrapClassOrNull(String name) {
        return null;
    }

    private Class<?> findLoadedClass(String name) {
        return null;
    }

    private Object getClassLoadingLock(String name) {
        return null;
    }

}
