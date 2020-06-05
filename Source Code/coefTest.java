package coefficients;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * @author Nathan Moder
 */

//Just for checking the values we've produced are correct. The other file was
//Getting a little bloated
public class coefTest 
{
    /**
     * @throws IOException 
     * Reads each of the files that contains relevant information (The values that are 11 mod 12)
     * takes the a(p) from that and checks it against a list of the expected values.
     * 
     * Outputs to the command line the combinations checked and any failures, formatted
     * Failed at: n Using: N Expected: expected a(p) Got: a(p).
     * 
     * Then outputs the number of successfully checked values.
     * Also outputs a file formatted:
     *      n , a(n) , x , 
     *      n , a(n) ,   ,
     *      n , a(n) , x ,
     *      n , a(n) ,   , !
     *      n , a(n) , x ,
     *          ...
     * Where N = 12(n) + 11, and a(n) is what a(N) should be equal to after a few adjustments.
     * If a(N) is the expected value, an x is printed in the next column. If a(N) is not the expected
     * value, nothing is printed in the next column. If n is a multiple of 11, a ! is printed in
     * the fourth column, since the conjecture does not apply to multiples of 11.
     */
    public static void checkCoefficients() throws IOException
    {
        int valuesChecked = 0 ;
        
        File values = new File("23nod11") ;
        Scanner scanner = new Scanner(values) ;
        
        String line = "" ;
        String[] numberAndCoef ;
        
        ArrayList<Integer> number = new ArrayList() ;
        ArrayList<Integer> expectedCoef = new ArrayList() ;
        
        while(scanner.hasNextLine())
        {
            line = scanner.nextLine() ;
            numberAndCoef = line.split(",") ;
            
            number.add(Integer.parseInt(numberAndCoef[0])) ; 
            expectedCoef.add(Integer.parseInt(numberAndCoef[1])) ;
        }
        
        scanner.close() ;
        
        char[] checked = new char[number.size()] ;
        
        //First we will check the primes that are 11 mod 12.
        values = new File("calculatedPrimes11Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        String[] primes11Mod12 = scanner.nextLine().split(",") ;
        String[] coefficients11Mod12 = scanner.nextLine().split(",") ;
        String[] type11Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        /*
        The form of the coefficients is:
        b(n) = [legendre(n , 11) * (x_N + 9 * y_N)] % 11
        N = 12n + 11
        */
        
        int N ;
        int n ;
        int x ; 
        int y ;
        int testCoef ;
        
        System.out.println("PRIMES 11 MOD 12") ;
        
        for(int i = 0 ; i < primes11Mod12.length ; i++)
        {
            if(type11Mod12[i].equals("1"))
            {
                x = Integer.parseInt(coefficients11Mod12[i]) ;
                y = 0 ;
            }
            else
            {
                x = 0 ;
                y = Integer.parseInt(coefficients11Mod12[i]) ;
            }
            
            N = Integer.parseInt(primes11Mod12[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef + " Type: " + type11Mod12[i]) ;
            }
        }
        
        //Next we will check the values that are products of 1 and 11 mod 12 primes.
        //Note that these values are still 11 mod 12.
        values = new File("combinedValues1And11Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        String[] combined1And11 = scanner.nextLine().split(",") ;
        String[] coefficients1And11Mod12 = scanner.nextLine().split(",") ;
        String[] combinedType11Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF 1 AND 11 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined1And11.length ; i++)
        {
            if(combinedType11Mod12[i].equals("1"))
            {
                x = Integer.parseInt(coefficients1And11Mod12[i]) ;
                y = 0 ;
            }
            else
            {
                x = 0 ;
                y = Integer.parseInt(coefficients1And11Mod12[i]) ; ;
            }
            
            N = Integer.parseInt(combined1And11[i]) ;
            n = (N - 11) / 12 ;   
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;

            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef + " Type: " + combinedType11Mod12[i]) ;
            }
        }
        
        //From this point forward I should have somehow compiled this into one general function that
        //takes the files and a header as input, because everything after this point is
        //exactly the same sans the file name and header for the section.
        
        //Then we will check the values that are products of 5 and 7 mod 12 primes.
        //Note that these values are still 11 mod 12.
        values = new File("combinedValues5And7Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        String[] combined5And7 = scanner.nextLine().split(",") ;
        String[] x5And7Mod12 = scanner.nextLine().split(",") ;
        String[] y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF 5 AND 7 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
         
        //Now we are checking the values that are products of 11 mod 12 primes and the powers of 1 mod 12 primes.
        values = new File("combinedPrimePowers1Mod12andPrimes11Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        String[] combined11AndPP1 = scanner.nextLine().split(",") ;
        String[] combinedCoef11AndPP1 = scanner.nextLine().split(",") ;
        String[] parity11AndPP1 = scanner.nextLine().split(",") ;
        type11Mod12 = scanner.nextLine().split(",") ;
        String[] type1Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF 11 MOD 12 PRIMES AND POWERS OF 1 MOD 12 PRIMES") ;
        for(int i = 0 ; i < combined11AndPP1.length ; i++)
        {
            if(parity11AndPP1[i].equals("e"))
            {
                if(type11Mod12[i].equals("1"))
                {
                    x = Integer.parseInt(combinedCoef11AndPP1[i]) ;
                    y = 0 ;
                }
                else
                {
                    x = 0 ;
                    y = Integer.parseInt(combinedCoef11AndPP1[i]) ;
                }
            }
            else
            {
                if(type11Mod12[i].equals("1") && type1Mod12[i].equals("1"))
                {
                    x = Integer.parseInt(combinedCoef11AndPP1[i]) ;
                    y = 0 ;   
                }
                else if(type11Mod12[i].equals("1") && type1Mod12[i].equals("2") || type11Mod12[i].equals("2") && type1Mod12[i].equals("1"))
                {
                    x = 0 ;
                    y = Integer.parseInt(combinedCoef11AndPP1[i]) ;
                }
                else
                {
                    x = 6 * Integer.parseInt(combinedCoef11AndPP1[i]) ;
                    y = 0 ;   
                }
            }
            
            N = Integer.parseInt(combined11AndPP1[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        //Checking values for 3 11 mod 12 primes
        values = new File("three11Mod12Primes.csv") ;
        scanner = new Scanner(values) ;
        
        String[] three11Mod12Primes = scanner.nextLine().split(",") ;
        String[] three11Mod12Coefs = scanner.nextLine().split(",") ;
        String[] three11Mod12Types = scanner.nextLine().split(",") ;

        System.out.println("PRODUCTS OF THREE 11 MOD 12 PRIMES") ;
        for(int i = 0 ; i < three11Mod12Primes.length ; i++)
        {
            if(three11Mod12Types[i].equals("1"))
            {
                x = Integer.parseInt(three11Mod12Coefs[i]) ;
                y = 0 ;
            }
            else
            {
                x = 0 ;
                y = Integer.parseInt(three11Mod12Coefs[i]) ;
            }
                      
            N = Integer.parseInt(three11Mod12Primes[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        //Checking values for 2 or 3 1 mod 12 primes and 1 11 mod 12 prime
        values = new File("twoOrThree1Mod12PrimesAnd11Mod12Prime.csv") ;
        scanner = new Scanner(values) ;
        
        String[] multiple1Mod12And11Value = scanner.nextLine().split(",") ;
        String[] multiple1Mod12And11Coef = scanner.nextLine().split(",") ;
        String[] overallType = scanner.nextLine().split(",") ;

        System.out.println("PRODUCTS OF TWO OR THREE 1 MOD 12 PRIMES AND ONE 11 MOD 12 PRIME") ;
        for(int i = 0 ; i < multiple1Mod12And11Value.length ; i++)
        {
            if(overallType[i].equals("1"))
            {
                x = Integer.parseInt(multiple1Mod12And11Coef[i]) ;
                y = 0 ;
            }
            else
            {
                x = 0 ;
                y = Integer.parseInt(multiple1Mod12And11Coef[i]) ;
            }
                      
            N = Integer.parseInt(multiple1Mod12And11Value[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        //Next we will check the values that are products of 5 and 7 mod 12 primes and some 1 mod 12 primes.
        //Note that these values are still 11 mod 12.
        values = new File("combined5and7Mod12PrimesAndSome1Mod12Primes.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF 5 AND 7 MOD 12 PRIMES WITH SOME 1 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combined5and7Mod12PrimesAndTwo11Mod12Primes.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF 5 AND 7 MOD 12 PRIMES WITH TWO 11 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedEvenPrimePowers5Mod12AndPrimes11Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF EVEN PRIME POWERS OF 5 MOD 12 PRIMES AND A 11 MOD 12 PRIME") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedEvenPrimePowers7Mod12AndPrimes11Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF EVEN PRIME POWERS OF 7 MOD 12 PRIMES AND A 11 MOD 12 PRIME") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedEven5and11and1Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF EVEN PRIME POWERS OF 5 MOD 12 PRIMES A 11 MOD 12 PRIME AND A 1 MOD 12 PRIME") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedEven7and11and1Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF EVEN PRIME POWERS OF 7 MOD 12 PRIMES A 11 MOD 12 PRIME AND A 1 MOD 12 PRIME") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedOddPrimePowers5Mod12and7Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF ODD PRIME POWERS OF 5 MOD 12 PRIMES AND ODD PRIME POWERS OF 7 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedOddPrimePowers5and7WithSome1Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF ODD PRIME POWERS OF 5 AND ODD PRIME POWERS OF 7 MOD 12 WITH SOME 1 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedOddPrimePowers5and7WithEven11Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF ODD PRIME POWERS OF 5 AND ODD PRIME POWERS OF 7 MOD 12 WITH EVEN 11 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        
        values = new File("combinedEvenPrimePower5And5and7.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF EVEN PRIME POWERS OF 5 AND A COMBINED 5 AND 7 VALUE") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedEvenPrimePower7And5and7.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF EVEN PRIME POWERS OF 7 AND A COMBINED 5 AND 7 VALUE AND ZERO OR MORE 1 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedEven5and5and7and1Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF EVEN PRIME POWERS OF 5 AND A COMBINED 5 AND 7 VALUE AND A 1 MOD 12 PRIME") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedEven7and11and1Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF EVEN PRIME POWERS OF 7 AND 11 MOD 12 PRIME AND A 1 MOD 12 PRIME") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedOdd5and7WithEvenPower5or7Mod12.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF COMBINED ODD POWERS OF 5 AND 7 MOD 12 PRIMES WITH EVEN POWERS OF 5 OR 7 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        values = new File("combinedEven7Even5And11.csv") ;
        scanner = new Scanner(values) ;
        
        combined5And7 = scanner.nextLine().split(",") ;
        x5And7Mod12 = scanner.nextLine().split(",") ;
        y5And7Mod12 = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        System.out.println("PRODUCTS OF COMBINED EVEN POWERS OF 7 EVEN COMBINATIONS OF 5 AND 11 MOD 12 PRIMES AND ZERO OR MORE 1 MOD 12 PRIMES") ;
        
        for(int i = 0 ; i < combined5And7.length ; i++)
        {
            x = Integer.parseInt(x5And7Mod12[i]) ;
            y = Integer.parseInt(y5And7Mod12[i]) ;
            
            N = Integer.parseInt(combined5And7[i]) ;
            n = (N - 11) / 12 ;
            
            testCoef = Coefficients.jacobi(n , 11) * (x + (9 * y)) ;
            testCoef = Coefficients.adjustedMod(testCoef , 11) ;
            
            if(testCoef == expectedCoef.get(n-1))
            {
                //System.out.println("Correct at: " + n + " Using: " + N) ;
                if(checked[n-1] != 'x')
                {
                    valuesChecked ++ ;
                    checked[n-1] = 'x' ;
                }
            }
            else
            {
                System.out.println("FAILED at: " + n + " Using: " + N + " Expected: " + expectedCoef.get(n-1) + " Got: " + testCoef) ;
            }
        }
        
        char[] badValue = new char[number.size()] ;
        for(int i = 1 ; i < number.size() ; i++)
        {
            if(i % 11 == 0)
            {
                badValue[i-1] = '!' ;
            }
        }
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File("checkedValueLog.csv"))) ;
        for(int i = 0 ; i < number.size() ; i++)
        {
            writer.append(number.get(i) + "," + expectedCoef.get(i) + "," + checked[i] + "," + badValue[i]) ;
            writer.append("\n") ;
        }
        
        writer.close() ;
        System.out.println("Checked: " + valuesChecked + " values") ;
    }
}
