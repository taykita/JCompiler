import java.util.Scanner;

public class Test {
    public static void main() {
        int n, s, d;
        final int w = 6;
        Scanner scan = new Scanner(System.in);
        scan.nextInt(n);
        int c = 0;
        int i = 2;

        while (i <= n) {
            d = 2;
            while ((i % d) != 0) {
                d = Math.incrementExact(d);
            }
            if (d == i) {
                if ((c % w) == 0) {
                    System.out.println();
                }
                System.out.printf("%6s", i);
                c = Math.incrementExact(c);
            }
            i = Math.incrementExact(i);
        }
        System.out.println();
        System.out.print(c);
        System.out.println();
    }
}
