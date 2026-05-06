import java.math.BigInteger;
import java.util.Scanner;

public class Bob {
    public static void main(String[] args) {
        System.out.println("Bob side");

        BigInteger p = RSA.generatePrime(1024);
        BigInteger q = RSA.generatePrime(1024);
        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        BigInteger e = BigInteger.valueOf(65537);
        
        while (!RSA.gcd(e, phi).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.valueOf(2));
        }

        BigInteger d = e.modInverse(phi);

        System.out.println("prime p: " + p);
        System.out.println("\nprime q: " + q);
        System.out.println("\nn: " + n);
        System.out.println("\nphi(n): " + phi);
        System.out.println("\ne: " + e);
        System.out.println("\nkey d: " + d);
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nenter the ciphertext from Alice: ");
        
        String input = scanner.nextLine();
        if (!input.isEmpty()) {
            try {
                BigInteger c = new BigInteger(input.trim());
                
                //Decrypt m = c^d mod n
                BigInteger m = RSA.modPower(c, d, n);
                
                String decryptedMessage = new String(m.toByteArray());
                System.out.println("\noriginal message: " + decryptedMessage);
            } catch (Exception ex) {
                System.out.println("Error invalid input");
            }
        }
        scanner.close();
    }
}