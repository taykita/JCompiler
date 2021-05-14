import java.util.Scanner;

public class Test {
    /* Является ли число квадратом */
    public static void main() {
        int k, n;
        int s = 5;
        int e = s;
        e = Math.max(4, 2);
        System.out.println(e);
        //Test = 5;
        Scanner scan = new Scanner(System.in);
        final int t = 10;
        final int a = t;

        scan.nextInt(n);
        k = 1;
        s = 0;
        while (s < n) {
            s = s + k;
            k = k + 2;
        }
        if (s == n) {
            System.out.println(1);
        } else {
            System.out.println(0);
        }
        System.out.println();
    }
}
