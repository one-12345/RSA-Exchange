import java.math.BigInteger;
import java.util.Scanner;

public class Alice {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Alice side");
        System.out.print("enter Bob n: ");
        BigInteger n = new BigInteger(scanner.nextLine().trim());
        System.out.print("enter Bob e: ");
        BigInteger e = new BigInteger(scanner.nextLine().trim());
        System.out.print("\nenter message for Bob: ");
        String message = scanner.nextLine();
        
        BigInteger m = new BigInteger(message.getBytes());

        //Encrypt c = m^e mod n
        BigInteger c = RSA.modPower(m, e, n);

        System.out.println("original: " + message);
        System.out.println("m: " + m);
        System.out.println("\nciphertext:");
        System.out.println(c);

        scanner.close();
    }
}