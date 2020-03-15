using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Numerics;

public class Fibonacci
{
    public static BigInteger fib(int n)
    {
        // Initialize first states
        BigInteger k = 0, k_1 = 1;

        // Handle negative numbers
        if (n < 0)
        {
            // Calculate positive counter part
            BigInteger result = fib(-n);
            // If number is even return minus
            if ((n * -1) % 2 == 0)
            {
                return -result;
            }
            else
            {
                return result;
            }
        }

        // for each bit in the given integer
        for (int i = 31; i >= 0; i--)
        {
            // Calculate the double fibonnaci (from matrix identity)
            // This will not execute, if we have the inital state
            // therefore ignoring the first 0s before the first 1
            // This is a simpler version of the matrix exponantiation by doubling 
            BigInteger _2k = k * (k_1 * 2 - k);
            BigInteger _2k_1 = k_1 * k_1 + k * k;
            // assign the calculated values
            // This is redundant for the first cases before the first 1 in the
            // bit sequence of the given integer
            k = _2k;
            k_1 = _2k_1;
            // If the bit is a one at the current position
            // Add the two values and save them.
            // This will iniate the doubling after the first 1 in the bit sequence.
            if ((((uint)n >> i) & 1) != 0)
            {
                BigInteger sum = k + k_1;
                k = k_1;
                k_1 = sum;
            }
        }
        return k;
    }
}

public class Test
{

    public static void Main(string[] args)
    {
        Stopwatch watch = new Stopwatch();
        watch.Start();
        Random random = new Random();
        for (int i = 0; i < 10; i++)
        {
            int nextInt = random.Next(-1000, 1000);
            Fibonacci.fib(nextInt);
            Console.WriteLine(nextInt);
        }
        for (int i = -10; i <= 10; i++)
        {
            Fibonacci.fib(i);
            Console.WriteLine(i);
        }
        BigInteger big = Fibonacci.fib(2000000);
        Console.WriteLine(2000000);
        watch.Stop();
        Console.WriteLine(big);
        Console.WriteLine("Elapsed " + watch.ElapsedMilliseconds + "ms");
    }
}