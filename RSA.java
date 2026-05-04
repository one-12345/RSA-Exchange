import java.security.SecureRandom;
import java.util.Arrays;
class RSA {
    public static void main(String[] args) {
        generatePrime(64);
    }

    public static byte[] generatePrime(int numBytes) {
        SecureRandom secureRand = new SecureRandom();
        byte[] p = new byte[numBytes];
        while (true) {
            //Generates a large number w/ first and last bit 1
            p = secureRand.generateSeed(numBytes);
            p[0] |= (byte) 0x80;
            p[numBytes-1] |= (byte) 0x01;
            
            //Cheap checks for divisibility for all small primes under 50: 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47
            int[] cheapTestList = {3,5,7,11,13,17,19,23,29,31,37,41,43,47};
            boolean failsTest = false;
            for (int cheapTest : cheapTestList) {
                int sum = 0;
                for (int i = 0; i < numBytes; i++) {
                    sum+=p[i]*(Math.pow(2,numBytes - i - 1) % cheapTest);
                }
                if (sum % cheapTest == 0) {
                    failsTest = true;
                }
            }
            if (failsTest) {
                continue;
            }

            //Miller Rabin primality test
            if (!millerRabin(p, numBytes)) {
                continue;
            }

            break;
        }
        printBytes(p);
        return p;
    }

    public static void printBytes(byte[] bytes) {
        for (byte b : bytes) {
            System.out.print(String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0'));
        }
    }

    public static boolean millerRabin(byte[] p, int numBytes) {
        SecureRandom secureRand = new SecureRandom();
        p[numBytes-1] = (byte) ((p[numBytes-1]) & 254); //Makes last bit a 0
        int trailingZeroes = 0;
        //Finds number of trailing zeroes in binary representation
        while (true) {
            int pointer = numBytes-1;
            if (p[pointer] % 256 != 0) {
                if (p[pointer] % 128 == 0) {
                    trailingZeroes+=7;
                    break;
                }
                if (p[pointer] % 64 == 0) {
                    trailingZeroes+=6;
                    break;
                }
                if (p[pointer] % 32 == 0) {
                    trailingZeroes+=5;
                    break;
                }
                if (p[pointer] % 16 == 0) {
                    trailingZeroes+=4;
                    break;
                }
                if (p[pointer] % 8 == 0) {
                    trailingZeroes+=3;
                    break;
                }
                if (p[pointer] % 4 == 0) {
                    trailingZeroes+=2;
                    break;
                }
                if (p[pointer] % 2 == 0) {
                    trailingZeroes+=1;
                    break;
                }
                break;
            }
            else {
                trailingZeroes+=8;
                pointer--;
            }
            break;
        }

        //Creates representation of n - 1 = 2^k * d, where d is odd
        byte[] d = new byte[numBytes];
        for (int i = trailingZeroes; i < 8 * numBytes; i++) {
            d[i] = p[i -trailingZeroes];
        }
        byte[] two = new byte[numBytes];
        two[numBytes - 1] = (byte) 2;
        for (int i = 0; i < 20; i++) {
            byte[] a = new byte[numBytes];
            while (less(a, p, numBytes) && greater(a, two, numBytes)) {
                a = secureRand.generateSeed(numBytes);
            }
            if (!Arrays.equals(modPower(a, d, p, numBytes), new byte[] {1})) {
                return false;
            }
        }

        return true;
    }

    public static boolean less(byte[] a, byte[] b, int numBytes) {
        for (int i = 0; i < numBytes; i++) {
            if ((a[i] & 0xFF) < (b[i] & 0xFF)) {
                return true;
            } else if ((a[i] & 0xFF) > (b[i] & 0xFF)) {
                return false;
            }
        }
        return false;
    }

    public static boolean greater(byte[] a, byte[] b, int numBytes) {
        for (int i = 0; i < numBytes; i++) {
            if ((a[i] & 0xFF) > (b[i] & 0xFF)) {
                return true;
            } else if ((a[i] & 0xFF) < (b[i] & 0xFF)) {
                return false;
            }
        }
        return false;
    }

    public static byte[] modPower(byte[] base, byte[] exponent, byte[] modulus, int numBytes) {
            
        return base;
    }
}
