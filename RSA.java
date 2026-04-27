import java.security.SecureRandom;
class RSA {
    public static void main(String[] args) {
        generatePrime(127);
    }

    public static byte[] generatePrime(int numBytes) {
        SecureRandom secureRand = new SecureRandom();
        byte[] p = new byte[numBytes];
        while (true) {
            //Generates a large number w/ first and last bit 1
            p = secureRand.generateSeed(numBytes);
            p[0] = (byte) ((p[0]) | (1 << 7));
            p[numBytes-1] = (byte) ((p[numBytes-1]) | 1);
            printBytes(p);
            
            //Checks for divisibility all small primes under 50: 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47
            


            break;
        }
        return p;
    }

    public static void printBytes(byte[] bytes) {
        for (byte b : bytes) {
            System.out.print(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
    }
}
