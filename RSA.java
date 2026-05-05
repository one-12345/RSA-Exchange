import java.security.SecureRandom;
import java.util.Arrays;
import java.math.BigInteger; 

class RSA {
    public static void main(String[] args) {
        BigInteger p = generatePrime(1024);
        BigInteger q = generatePrime(1024);

        BigInteger n = p.multiply(q);
        BigInteger phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));

        BigInteger e = BigInteger.valueOf(65537);

        while (!gcd(e, phi).equals(BigInteger.ONE)) {
            e = e.add(BigInteger.valueOf(2));
        }

        BigInteger d = e.modInverse(phi);
        
        String message = "How are youO";

        BigInteger m = new BigInteger(message.getBytes());

        //encrypt
        BigInteger c = modPower(m, e, n);

        //decrypt
        BigInteger back = modPower(c, d, n);

        System.out.println("Original: " + message);
        System.out.println("Decrypted: " + new String(back.toByteArray()));
        System.out.println("Correct: " + back.equals(m));

    }

    public static BigInteger generatePrime(int bits) {
        SecureRandom secureRand = new SecureRandom();
    
        while (true) {
            BigInteger p = new BigInteger(bits, secureRand);
            p = p.setBit(bits - 1); 
            p = p.setBit(0); 
    
            if (p.bitLength() < 64) {
                if (!isPrimeDeterministic(p.longValue())) continue;
            }
    
            //Cheap checks for divisibility for all small primes under 50
            int[] cheapTestList = {3,5,7,11,13,17,19,23,29,31,37,41,43,47};
            boolean failsTest = false;
    
            for (int prime : cheapTestList) {
                if (p.mod(BigInteger.valueOf(prime)).equals(BigInteger.ZERO)) {
                    failsTest = true;
                    break;
                }
            }
            if (failsTest) continue;
    
            //Miller-Rabin primality test
            if (!millerRabin(p, 20)) continue;
    
            return p;
        }
    }

    // public static void printBytes(byte[] bytes) {
    //     for (byte b : bytes) {
    //         System.out.print(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
    //     }
    // }

    public static boolean millerRabin(BigInteger n, int iterations) {
        SecureRandom secureRand = new SecureRandom();

        if (n.compareTo(BigInteger.valueOf(2)) < 0) return false;
        if (n.equals(BigInteger.valueOf(2))) return true;
        if (n.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) return false;

        // n - 1 = 2^s * d
        BigInteger d = n.subtract(BigInteger.ONE);
        int s = 0;

        while (d.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
            d = d.divide(BigInteger.valueOf(2));
            s++;
        }

        for (int i = 0; i < iterations; i++) {
            BigInteger a;
            do {
                a = new BigInteger(n.bitLength(), secureRand);
            } while (a.compareTo(BigInteger.valueOf(2)) < 0 || a.compareTo(n.subtract(BigInteger.valueOf(2))) > 0);

            BigInteger x = modPower(a, d, n);

            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE))) {
                continue;
            }

            boolean passed = false;

            for (int r = 1; r < s; r++) {
                x = modPower(x, BigInteger.valueOf(2), n);

                if (x.equals(n.subtract(BigInteger.ONE))) {
                    passed = true;
                    break;
                }
            }

            if (!passed) return false;
        }

        return true;
    }

    public static boolean isPrimeDeterministic(long n) {
        if (n < 2) return false;
        for (long i = 2; i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    // public static boolean less(byte[] a, byte[] b, int numBytes) {
    //     for (int i = 0; i < numBytes; i++) {
    //         if ((a[i] & 0xFF) < (b[i] & 0xFF)) {
    //             return true;
    //         } else if ((a[i] & 0xFF) > (b[i] & 0xFF)) {
    //             return false;
    //         }
    //     }
    //     return false;
    // }

    // public static boolean greater(byte[] a, byte[] b, int numBytes) {
    //     for (int i = 0; i < numBytes; i++) {
    //         if ((a[i] & 0xFF) > (b[i] & 0xFF)) {
    //             return true;
    //         } else if ((a[i] & 0xFF) < (b[i] & 0xFF)) {
    //             return false;
    //         }
    //     }
    //     return false;
    // }

    public static BigInteger modPower(BigInteger base, BigInteger exponent, BigInteger modulus) {
        BigInteger result = BigInteger.ONE;
        base = base.mod(modulus);

        while (exponent.compareTo(BigInteger.ZERO) > 0) {
            if (exponent.testBit(0)) {
                result = result.multiply(base).mod(modulus);
            }
            base = base.multiply(base).mod(modulus);
            exponent = exponent.shiftRight(1);
        }

        return result;
    }

    public static BigInteger gcd(BigInteger a, BigInteger b) {
        while (!b.equals(BigInteger.ZERO)) {
            BigInteger temp = b;
            b = a.mod(b);
            a = temp;
        }
        return a;
    }
}
