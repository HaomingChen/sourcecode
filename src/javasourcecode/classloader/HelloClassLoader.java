package javasourcecode.classloader;

import java.util.Scanner;

/**
 * @author Haoming Chen
 * Created on 2020/1/9
 */
public class HelloClassLoader extends ClassLoader{
    Scanner sc = new Scanner(System.in);
    String a = sc.nextLine().toString();
    String b = sc.nextLine().toString();
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String a = sc.nextLine().toString();
        String b = sc.nextLine().toString();
        System.out.println(Integer.parseInt(a) + Integer.parseInt(b));
    }
}
