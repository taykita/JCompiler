import java.util.Scanner;

public class Test {
    /* Является ли число квадратом */
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
                d = d + 1;
            }
            if (d == i) {
                if ((c % w) == 0) {
                    System.out.println();
                }
                System.out.printf("%6s", i);
                c = c + 1;
            }
            i = i + 1;
        }

    }
    system.out.println();
    system.out.print(c);
    system.out.println();

}
