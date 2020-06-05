// Maybe not the best name, but it will do.
package coefficients;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList ;

/**
 *
 * @author Nathan Moder
 */
public class Coefficients 
{
    // Edit this to try for more/less values.
    public static final int TARGET = 1000000 ;
    
    /**
     * @param limit
     * @throws IOException 
     * An implementation of the sieve of Eratosthenes
     * runs in O(n log log(n)).
     * Did 1 mil in about 5 seconds.
     * Did 10 mil in about 8 minutes.
     * 100 mil should take about 2 hours to finish.
     */
    public static void sievePrimes(int limit) throws IOException
    {
        //Setting things up for file writing
        String fileName = "primes.csv" ;
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)) ;
        
        boolean[] numbers = new boolean[limit + 1] ;
        String primeList = new String() ;
        int primeCount = 0 ; 
        for(int i = 2 ; i < Math.sqrt(limit) ; i++)
        {
            if(!numbers[i])
            {
                for(int j = i*i ; j < limit ; j = j + i)
                {
                    numbers[j] = true ;
                }
            }
        }
        
        for(int i = 2 ; i < limit ; i++)
        {
            if(!numbers[i])
            {
                primeList = primeList.concat(i + ",") ;
                primeCount++ ;
            }
        }
        
        System.out.println(primeCount) ;
        System.out.println(primeList) ;
        writer.write(primeList) ;
        writer.close() ;
    }
    
   /**
    * @return an array of all the primes
    * @throws IOException 
    * 
    * Reads the primes out of the file created by the previous method
    * returns them in an array.
    */
    public static int[] readPrimesFromFile() throws IOException
    {
        //Setting things up to read the file.
        File primeList = new File("primes.csv") ;
        Scanner scanner = new Scanner(primeList) ; 
       
        String line ;
        String[] numberHolder = new String[1] ;
                
        while(scanner.hasNextLine())
        {
            line = scanner.nextLine() ;
            numberHolder = line.split(",") ;
            System.out.println(line) ;
        }
     
        scanner.close() ;
        
        int numberOfPrimes = numberHolder.length ;
        int[] primes = new int[numberOfPrimes] ;
        
        for(int i = 0 ; i < numberOfPrimes ; i++)
        {
            primes[i] = Integer.parseInt(numberHolder[i]) ;
        }
        
        System.out.println(primes[numberOfPrimes-1]) ;
        return primes ;
    }
    
    /**
     * @param primes An array of integers. Will work for any collection of integers,
     * but only works correctly when it is a collection of primes.
     * @throws IOException 
     * Does not actually return anything, instead prints the results into various files
     * based on the remainder of the value when divided by 12.
     * 
     * Writes each file as p , p , p , ... 
     */
    public static void filterPrimes(int[] primes) throws IOException
    {
        //Setting things up for file writing
        String fileName = "primes11Mod12.csv" ;
        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)) ;
        
        String primes11Mod12 = new String() ;
        String primes1Mod12 = new String() ;
        String primes5Mod12 = new String() ;
        String primes7Mod12 = new String() ;
        String otherPrimes = new String() ;
        
        for(int i = 0 ; i < primes.length ; i++)
        {
            // Does p % 12 then does the correct action based on the result.
            switch (primes[i] % 12)
            {
                case 11:
                    primes11Mod12 = primes11Mod12.concat(primes[i] + ",") ;
                    break ;
                case 1:
                    primes1Mod12 = primes1Mod12.concat(primes[i] + ",") ;
                    break ;
                case 5:
                    primes5Mod12 = primes5Mod12.concat(primes[i] + ",") ;
                    break ;
                case 7:
                    primes7Mod12 = primes7Mod12.concat(primes[i] + ",") ;
                    break ;
                default :
                    otherPrimes = otherPrimes.concat(primes[i] + ",") ;
                    break ;
            }
        }
        
        //Writing everything into the proper files.
        writer.write(primes11Mod12) ;
        writer.close() ;
        
        writer = new BufferedWriter(new FileWriter("primes1Mod12.csv")) ;
        writer.write(primes1Mod12) ;
        writer.close() ;
        
        writer = new BufferedWriter(new FileWriter("primes5Mod12.csv")) ;
        writer.write(primes5Mod12) ;
        writer.close() ;
        
        writer = new BufferedWriter(new FileWriter("primes7Mod12.csv")) ;
        writer.write(primes7Mod12) ;
        writer.close() ;
        
        writer = new BufferedWriter(new FileWriter("otherPrimes.csv")) ;
        writer.write(otherPrimes) ;
        writer.close() ;
    }
    
    /**
     * @param inputFile a file name as a string. Should only contain primes of a certain type mod 12.
     * @param outputFile a file name as a string. This is where the output of the file is written.
     * @param modType an integer that represents p % 12.
     * @throws IOException 
     * 
     * The input file should be formatted p , p , p , ... otherwise it will not work correctly.
     * Takes each prime, and finds the x and y values for the corresponding quadratic form for
     * each type. Something like p = x^2 + xy + y^2, and the coefficients on each value is 
     * different depending on the type of prime.
     */
    public static void solvePrimes(String inputFile , String outputFile , int modType) throws IOException
    {
        //Setting up the file reading
        File primeList = new File(inputFile) ;
        Scanner scanner = new Scanner(primeList) ; 
       
        String line ;
        String[] numberHolder = new String[1] ;
                
        while(scanner.hasNextLine())
        {
            line = scanner.nextLine() ;
            numberHolder = line.split(",") ;
            System.out.println(line) ;
        }
     
        scanner.close() ;
        
        ArrayList<Integer> primes = new ArrayList() ;
        ArrayList<Integer> solutionX = new ArrayList() ;
        ArrayList<Integer> solutionY = new ArrayList() ;
        ArrayList<Integer> solutionType = new ArrayList() ;
        
        int currentPrime = 0 ;
        int solutionValue = 0 ;
        int altSolutionValue = 0 ; 
        boolean solutionFound = false ;
        
        for(int i = 0 ; i < numberHolder.length ; i++)
        {
            currentPrime = Integer.parseInt(numberHolder[i]) ;
            //Checking if the prime can be solved by one of the quadratic forms.
            if( ((modType == 1 || modType == 11) && jacobiBool(-69 , currentPrime)) || ((modType == 5 || modType == 7) && jacobiBool(-69 , currentPrime)) )
            {
                primes.add(currentPrime) ;
                
                //First Equation
                for(int x = 0 ; x < Math.sqrt(currentPrime) ; x++)
                {
                    for(int y = 0 ; y < Math.sqrt(currentPrime) + 1 ; y++)
                    {
                        //Primes % 12 = 5 or 7 do not have an equation of the first type.
                        if(modType == 5 || modType == 7)
                        {
                            x = currentPrime ;
                            y = currentPrime ;
                            solutionFound = false ;
                            break ;
                        }
                        
                        switch (modType)
                        {
                            case 1 : 
                                solutionValue = (x * x) + 69 * (y * y) ;
                                break ;
                            case 11 : 
                                solutionValue = 3 * (x * x) + 23 * (y * y) ;
                                break ;      
                        } 
                        if(solutionValue == currentPrime)
                        {
                            solutionX.add(x) ;
                            solutionY.add(y) ;
                            solutionType.add(1) ;
                            System.out.println("Solution found with first equation "+ currentPrime + " " + x + " " + y);
                            solutionFound = true ;
                            break ;
                        }
                        else if(solutionValue > currentPrime)
                        {
                            break ;
                        }
                    }
                    
                    if(solutionFound)
                    {
                        break ;
                    }
                }
                
                //Second Equation
                if(!solutionFound)
                {
                    //For these types, x can be positive or negative, so we need to test both outcomes at the same time.
                    //Bounded by sqrt(Integer.MAX_VALUE) to avoid overflow.
                    for(int x = 0 ; x < Math.min(currentPrime , (int)(Math.sqrt(Integer.MAX_VALUE))) ; x++)
                    {
                        for(int y = 0 ; y < 35 * Math.sqrt(currentPrime) ; y++)
                        {
                            switch (modType)
                            {
                                case 1 :
                                    solutionValue = 6 * (x * x) + 6 * (x * y) +  13 * (y * y) ;
                                    altSolutionValue = 6 * (x * x) + 6 * (-x * y) + 13 * (y * y) ;
                                    break ;
                                case 11 : 
                                    solutionValue = 2 * (x * x) + 2 * (x * y) +  35 * (y * y) ;
                                    altSolutionValue = 2 * (x * x) + 2 * (-x * y) + 35 * (y * y) ;
                                    break ;
                                case 5 :
                                    solutionValue = 5 * (x * x) + 2 * (x * y) + 14 * (y * y) ;
                                    altSolutionValue = 5 * (x * x) + 2 * (-x * y) + 14 * (y * y) ;
                                    break ;
                                case 7 :
                                    solutionValue = 7 * (x * x) + 2 * (x * y) + 10 * (y * y) ;
                                    altSolutionValue = 7 * (x * x) + 2 * (-x * y) + 10 * (y * y) ;
                                    break ;
                            }

                            if(solutionValue == currentPrime)
                            {
                                solutionX.add(x) ;
                                solutionY.add(y) ;
                                solutionType.add(2) ;
                                System.out.println("Solution found with second equation " + currentPrime + " " + x + " " + y);
                                solutionFound = true ;
                                break ;
                            }
                            else if(altSolutionValue == currentPrime)
                            {
                                solutionX.add(-x) ;
                                solutionY.add(y) ;
                                solutionType.add(2) ;
                                System.out.println("Solution found with second equation " + currentPrime + " " + -x + " " + y);
                                solutionFound = true ;
                                break ;
                            }
                            else if((altSolutionValue > currentPrime) && (y > x) )
                            {
                                break ;
                            }
                        }

                        if(solutionFound)
                        {
                            break ;
                        }
                    }
                } 
                
                if(!solutionFound)
                {
                    System.out.println("Solution Not Found! " + currentPrime) ;
                    break ;
                }
            }
            else
            {
                //These should be primes that have -69/p = -1, in which case the,
                //coefficient is 0, so the x and y should both be zero so the coefficient
                //gets evaluated as 0 later. 
                
                //Does not apply for values that are multiples of 11. So 11 needs
                //to be excluded.
                if(currentPrime != 11)
                {
                    primes.add(currentPrime) ;
                    solutionX.add(0) ;
                    solutionY.add(0) ;
                    solutionType.add(1) ;
                }
            }
            solutionFound = false ;
        }
        ArrayList[] lines = {primes , solutionX , solutionY , solutionType} ;
        writeToFile(lines , outputFile) ;
    }
    
    /**
     * @param inputFile The file name as a string of the list of primes, x and y values, and types to calculate
     * the coefficients for.
     * @param outputFile The file that the results will be written to.
     * @param modType The value of p % 12.
     * @throws IOException 
     * 
     * The input file should be formatted p , p , p , ...
     *                                    x , x , x , ...
     *                                    y , y , y , ...
     *                                    t , t , t , ...
     * where t is the type of quadratic form used to solve the prime. It indicates extra
     * values added to the coefficient at the end.
     * 
     * Computes the a(p) for each prime in the list using certain polynomials containing
     * the x and y values that differ based on the type. Again stored with a type that
     * indicates if the final a(p) has certain attached values that do not play nicely with
     * computers.
     * 
     * Writes the results in a file formatted p , p , p , ...
     *                                        a , a , a , ...
     *                                        t , t , t , ...
     * Then only for 5 and 7 mod 12 primes -> x , x , x , ...
     *                                     -> y , y , y , ...
     * Because the x and y values may need to change based on their sign when added,
     * and they need to be used again later.
     */
    public static void calculatePrimes(String inputFile , String outputFile, int modType) throws IOException
    {
        //Setting things up to read the file.
        File primeList = new File(inputFile) ;
        Scanner scanner = new Scanner(primeList) ; 
       
        String line ;
        String[] primes ;
        String[] xValue ;
        String[] yValue ;
        String[] type ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes = line.split(",") ;
        
        //Then the x values
        line = scanner.nextLine() ;
        xValue = line.split(",") ;
        
        //Then the y values
        line = scanner.nextLine() ;
        yValue = line.split(",") ;
        
        //Finally, the type of equation used to solve it
        line = scanner.nextLine() ;
        type = line.split(",") ;
        
        scanner.close() ;
        
        int currentPrime = 0 ;
        int x = 0 ;
        int y = 0 ;
        int eqType = 0 ;
        int chi = 0 ;
        
        //Double plays nicely with the powers and should help avoid overflow.
        double tempValue ;
        
        int[] outputValue = new int[primes.length] ;
        
        for(int i = 0 ; i < primes.length ; i++)
        {
            currentPrime = Integer.parseInt(primes[i]) ;
            x = Integer.parseInt(xValue[i]) ;
            y = Integer.parseInt(yValue[i]) ;
            eqType = Integer.parseInt(type[i]) ;
            
            //These are the p = x^2 + 69y^2
            if(eqType == 1)
            {
                switch (modType)
                {
                    case 1 :
                        chi = chi(x , y) ;

                        x = adjustedMod(x , 11) ;
                        y = y % 11 ;

                        tempValue = chi *
                                    (2 * Math.pow(x , 9) + 
                                     2 * Math.pow(x , 7) * Math.pow(y , 2) + 
                                     9 * Math.pow(x , 5) * Math.pow(y , 4) +
                                     7 * Math.pow(x , 3) * Math.pow(y , 6) +
                                     8 * x * Math.pow(y , 8) ) ;

                        outputValue[i] = adjustedMod(tempValue , 11) ;
                        break ;
                    case 11 :
                        chi = chi(23 * y , x) ;
                
                        x = adjustedMod(x , 11) ;
                        y = y % 11 ;

                        tempValue = chi *
                                    (3 * Math.pow(x , 8) * y + 
                                     4 * Math.pow(x , 6) * Math.pow(y , 3) + 
                                     2 * Math.pow(x , 4) * Math.pow(y , 5) +
                                     9 * Math.pow(x , 2) * Math.pow(y , 7) +
                                     9 * Math.pow(y , 9) ) ;

                        outputValue[i] = adjustedMod(tempValue , 11) ;
                        //23 is a special case, so it's going to be accounted for here.
                        if(currentPrime == 23)
                        {
                            //The coefficient for 23 should be 23^24 * sqrt(23)
                            //p^11 = p(mod 11) -> 23^24 = 23^11 * 23^11 * 23^2
                            // = 23 * 23 * 23^2 = 23^4
                            tempValue = Math.pow(23 , 4) ;
                            outputValue[i] = (int)tempValue % 11 ;
                        }
                        break ;
                }
            }
            //These are the p = 6x^2 + 6xy + 13y
            else if(eqType == 2)
            {
                switch(modType)
                {
                    case 1 :
                        chi = chi(9 * x + 16 * y , -x + y) ;

                        x = adjustedMod(x , 11) ;
                        y = y % 11 ;

                        tempValue = chi * 
                                    (7 * Math.pow(x , 9) +
                                     4 * Math.pow(x , 8) * y +
                                     3 * Math.pow(x , 7) * Math.pow(y , 2) +
                                         Math.pow(x , 6) * Math.pow(y , 3) +
                                     9 * Math.pow(x , 5) * Math.pow(y , 4) + 
                                     2 * Math.pow(x , 3) * Math.pow(y , 6) +
                                     10 * x * x * Math.pow(y , 7) + 
                                     4 * x * Math.pow(y , 8) + 
                                     5 * Math.pow(y , 9)) ;
                        outputValue[i] = adjustedMod(tempValue , 11) ;
                        break ;
                        
                    case 11 :
                        chi = chi(5 * x + 37 * y , x - 2 * y) ;
                
                        x = adjustedMod(x , 11) ;
                        y = y % 11 ;

                        tempValue = chi * 
                                    (6 * Math.pow(x , 8) * y +
                                     2 * Math.pow(x , 7) * y * y +
                                     5 * Math.pow(x , 6) * Math.pow(y , 3) +
                                     8 * Math.pow(x , 5) * Math.pow(y , 4) +
                                     4 * Math.pow(x , 4) * Math.pow(y , 5) + 
                                     8 * Math.pow(x , 3) * Math.pow(y , 6) +
                                     5 * x * x * Math.pow(y , 7) + 
                                     2 * x * Math.pow(y , 8) + 
                                     6 * Math.pow(y , 9)) ;
                        outputValue[i] = adjustedMod(tempValue , 11) ;
                        break ;
                    
                    //These values have an additional (x +- 2y +- sqrt(6)y attached.
                    //(+ for 5, - for 7). They will get added in later.
                    case 5 :
                        if(-x - (2 * y) + (Math.sqrt(6) * y) <= 0)
                        {
                            x = -x ;
                            y = -y ;
                            xValue[i] = Integer.toString(x) ;
                            yValue[i] = Integer.toString(y) ;
                        }
                        
                        chi = chi((5 * x) + y , -y) ;
                        x = adjustedMod(x , 11) ;
                        y = adjustedMod(y , 11) ;
                        
                        tempValue = chi * 
                                    (9 * Math.pow(x , 8) + 
                                     10 * Math.pow(x , 7) * y +
                                     10 * Math.pow(x , 6) * y * y + 
                                     2 * Math.pow(x , 5) * Math.pow(y , 3) +
                                     7 * Math.pow(x , 4) * Math.pow(y , 4) +
                                     10 * Math.pow(x , 3) * Math.pow(y , 5) + 
                                     8 * x * x * Math.pow(y , 6) + 
                                     7 * x * Math.pow(y , 7) +
                                     4 * Math.pow(y , 8)) ;
                        outputValue[i] = adjustedMod(tempValue , 11) ;
                        break ;
                        
                    case 7 :
                        if(x - (2 * y) - (Math.sqrt(6) * y) <= 0)
                        {
                            x = -x ;
                            y = -y ;
                            xValue[i] = Integer.toString(x) ;
                            yValue[i] = Integer.toString(y) ;
                        }
                        
                        chi = chi((7 * x) + y , y) ;
                        x = adjustedMod(x , 11) ;
                        y = adjustedMod(y , 11) ;
                        
                        tempValue = chi *
                                    (3 * Math.pow(x , 8) +
                                     5 * Math.pow(x , 7) * y +
                                     2 * Math.pow(x , 6) * y * y +
                                     5 * Math.pow(x , 5) * Math.pow(y , 3) +
                                     7 * Math.pow(x , 4) * Math.pow(y , 4) +
                                     4 * Math.pow(x , 3) * Math.pow(y , 5) +
                                     7 * x * x * Math.pow(y , 6) +
                                     3 * x * Math.pow(y , 7) +
                                     Math.pow(y , 8)) ;
                        outputValue[i] = adjustedMod(tempValue , 11) ;
                        break ;
                }
            }
        }
        
        //Writing the output.
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)) ;
        for(int i = 0 ; i < primes.length ; i++)
        {
            writer.append(primes[i] + ",") ;
        }
        
        writer.append("\n") ;
        
        for(int i = 0 ; i < outputValue.length ; i++)
        {
            writer.append(outputValue[i] + ",") ;
        }
        
        writer.append("\n") ;
        
        for(int i = 0 ; i < type.length ; i++)
        {
            writer.append(type[i] + ",") ;
        }
        
        if(modType == 5 || modType == 7)
        {
            writer.append("\n") ;
            
            for(int i = 0 ; i < xValue.length ; i++)
            {
                writer.append(xValue[i] + ",") ;
            }
            
            writer.append("\n") ;
            
            for(int i = 0 ; i < yValue.length ; i++)
            {
                writer.append(yValue[i] + ",") ;
            }
        }
        
        writer.close() ;
    }
    
    /**
     * @throws IOException
     * 
     * Uses the calculated values of 11 and 1 mod 12 primes to create additional
     * 11 mod 12 values. Probably should take the filenames as arguments, but they
     * are hard-coded to what they should be currently.
     * 
     * Writes the output to a file formatted v , v , v , ...
     *                                       a , a , a , ...
     *                                       t , t , t , ...
     * where v = prime1 * prime2 , a = a(v) sans a few things signified by the type t.
     * t = 1 basically means no sqrt(6) and t = 2 means it has a sqrt(6)
     */
    public static void combinePrimes1And11Mod12() throws IOException
    {
        File primeList11Mod12 = new File("calculatedPrimes11Mod12.csv") ;
        File primeList1Mod12 = new File("calculatedPrimes1Mod12.csv") ;
        Scanner scanner = new Scanner(primeList11Mod12) ;
        
        String line ;
        String[] primes11Mod12 ;
        String[] value11Mod12 ;
        String[] type11Mod12 ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes11Mod12 = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value11Mod12 = line.split(",") ;
        
        //Finally, the type of equation used to solve it
        line = scanner.nextLine() ;
        type11Mod12 = line.split(",") ;
        
        scanner.close() ;
        
        //Grabbing info from the other file.
        scanner = new Scanner(primeList1Mod12) ;
        
        String[] primes1Mod12 ;
        String[] value1Mod12 ;
        String[] type1Mod12 ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes1Mod12 = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value1Mod12 = line.split(",") ;
        
        //Finally, the type of equation used to solve it
        line = scanner.nextLine() ;
        type1Mod12 = line.split(",") ;
        
        scanner.close() ;
        
        ArrayList<Integer> primeProduct = new ArrayList() ;
        ArrayList<Integer> valueProduct = new ArrayList() ;
        ArrayList<String> overallEqType = new ArrayList() ;
        
        int prime11M12 ;
        int prime1M12 ;
        int value11M12 ;
        int value1M12 ;
        int type11M12 ;
        int type1M12 ;
        int overallType ;
        int combinedValue ;
        
        for(int i = 0 ; i < primes11Mod12.length ; i++)
        {
            prime11M12 = Integer.parseInt(primes11Mod12[i]) ;
            value11M12 = Integer.parseInt(value11Mod12[i]) ;
            type11M12 = Integer.parseInt(type11Mod12[i]) ;
            
            for(int j = 0 ; j < primes1Mod12.length ; j++)
            {
                prime1M12 = Integer.parseInt(primes1Mod12[j]) ;
                value1M12 = Integer.parseInt(value1Mod12[j]) ;
                type1M12 = Integer.parseInt(type1Mod12[j]) ;
                
                //If the value is going to be too large, we can just move on
                //to combinations with the next 11 mod 12 prime since they are
                //stored in increasing order.
                if(prime1M12 > TARGET / prime11M12)
                {
                    break ;
                }
                
                switch(type11M12 + type1M12)
                {
                    //1 and 1 -> sqrt(-23)
                    case 2:
                        combinedValue = value11M12 * value1M12 ;
                        overallType = 1 ;
                        break ;
                    //1 and 2 -> sqrt(-23) sqrt(6)
                    case 3:
                        combinedValue = value11M12 * value1M12 ;
                        overallType = 2 ;
                        break ;
                    //2 and 2 -> 6 * sqrt(-23)
                    case 4:
                        combinedValue = value11M12 * value1M12 * 6 ;
                        overallType = 1 ;
                        break ;
                    //should never happen
                    default :
                        combinedValue = value11M12 * value1M12 ;
                        overallType = -100 ;
                }
                
                primeProduct.add(prime11M12 * prime1M12) ;
                valueProduct.add(adjustedMod(combinedValue , 11)) ;
                overallEqType.add(Integer.toString(overallType)) ;
            }
        }
        
        //Setting things up for file writing
        String fileName = "combinedValues1and11Mod12.csv" ;
        ArrayList[] lines = {primeProduct , valueProduct , overallEqType} ;
        writeToFile(lines , fileName) ;
    }
    
    /**
     * @throws IOException 
     * Functions similarly to the previous function, except it has to account for
     * the a(v) having the form a(v) = x + y(sqrt(6)).
     * The output is formatted v , v , v , ...
     *                         x , x , x , ...
     *                         y , y , y , ...
     */
    public static void combinePrimes5And7Mod12() throws IOException
    {
        File primeList5Mod12 = new File("calculatedPrimes5Mod12.csv") ;
        File primeList7Mod12 = new File("calculatedPrimes7Mod12.csv") ;
        Scanner scanner = new Scanner(primeList5Mod12) ;
        
        String line ;
        String[] primes5Mod12 ;
        String[] value5Mod12 ;
        String[] x5Mod12 ;
        String[] y5Mod12 ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes5Mod12 = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value5Mod12 = line.split(",") ;
        
        //Then the x value associated with the primes (skip a line for the type)
        line = scanner.nextLine() ;
        line = scanner.nextLine() ;
        x5Mod12 = line.split(",") ;
        
        //Finally the y value associated with the primes
        line = scanner.nextLine() ;
        y5Mod12 = line.split(",") ;
        
        scanner.close() ;
        
        //Grabbing info from the other file.
        scanner = new Scanner(primeList7Mod12) ;
        
        String[] primes7Mod12 ;
        String[] value7Mod12 ;
        String[] x7Mod12 ;
        String[] y7Mod12 ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes7Mod12 = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value7Mod12 = line.split(",") ;
        
        //and the x value. (skip a line for the type).
        line = scanner.nextLine() ;
        line = scanner.nextLine() ;
        x7Mod12 = line.split(",") ;
        
        //finally, the y value.
        line = scanner.nextLine() ;
        y7Mod12 = line.split(",") ;
        
        scanner.close() ;
        
        ArrayList<Integer> primeProduct = new ArrayList() ;
        ArrayList<Integer> integerProductPart = new ArrayList() ;
        ArrayList<Integer> root6ProductPart = new ArrayList() ;
        
        int prime5M12 ;
        int prime7M12 ;
        int value5M12 ;
        int value7M12 ;
        int x5 ;
        int x7 ;
        int y5 ;
        int y7 ;
        int product ;
        int integerPart ;
        int root6Part ;
        
        for(int i = 0 ; i < primes5Mod12.length ; i++)
        {
            prime5M12 = Integer.parseInt(primes5Mod12[i]) ;
            value5M12 = Integer.parseInt(value5Mod12[i]) ;
            x5 = Integer.parseInt(x5Mod12[i]) ;
            y5 = Integer.parseInt(y5Mod12[i]) ;
            
            for(int j = 0 ; j < primes7Mod12.length ; j++)
            {
                prime7M12 = Integer.parseInt(primes7Mod12[j]) ;
                value7M12 = Integer.parseInt(value7Mod12[j]) ;
                x7 = Integer.parseInt(x7Mod12[j]) ;
                y7 = Integer.parseInt(y7Mod12[j]) ;
                
                // So to get the product here we need to add in some extra stuff
                // That was left out of the original calculations. Namely
                // sqrt(-23) * (-2 + sqrt(6)) and (x5 + 2y5 + sqrt(6)y5) * (x7 - 2y7 - sqrt(6)y7)
                // We are omitting the sqrt(-23) because it comes out later.
                // These will be stored as an ordered pair to represent
                // Int1 + Int2 * sqrt(6)
                
                product = prime5M12 * prime7M12 ;
                
                if(prime5M12 > TARGET / prime7M12)
                {
                    break ;
                }
                else
                {
                    primeProduct.add(product) ;
                    
                    // The integer part of the product will look like:
                    // v5 * v7 * (-2x7x5 + 2x7y5 - 2x5y7 - 4y7y5)
                    // and v5 * v7 * (x7x5 - 2y7y5) for the root 6 part
                    
                    integerPart = -2 * x7 * x5 + 2 * x7 * y5 - 2 * x5 * y7 - 4 * y7 * y5 ;
                    integerPart = integerPart * value5M12 * value7M12 ;
                    
                    root6Part = x7 * x5 - 2 * y7 * y5 ;
                    root6Part = root6Part * value5M12 * value7M12 ;
                    
                    integerProductPart.add(adjustedMod(integerPart , 11)) ;
                    root6ProductPart.add(adjustedMod(root6Part , 11)) ;
                }
            }
        }
        
        //Setting things up for file writing
        String fileName = "combinedValues5and7Mod12.csv" ;
        ArrayList[] lines = {primeProduct , integerProductPart , root6ProductPart} ;
        writeToFile(lines , fileName) ;
    }
    
    /**
     * @param inputFile name of the input file as a string.
     * @param outputFile name of the output file as a string.
     * @param modType the value of p % 12 for the primes in the input file.
     * @throws IOException 
     * 
     * The input should be one of the files created by the calculatePrimes function. The output file will look like a table
     * formatted p, a(p), a(p^2) , a(p^3) , ... , type
     *           p, a(p), a(p^2) , a(p^3) , ... , type
     *              ...
     *           p, a(p), a(p^2) , a(p^3) , ... , type
     * with values of -1 slotted in for any value that corresponds to a prime power that exceeds the TARGET value.
     */
    public static void calculatePrimePowers1And11Mod12(String inputFile , String outputFile , int modType) throws IOException
    {
        //Setting up for reading the file.
        File values = new File(inputFile) ;
        Scanner scanner = new Scanner(values) ;
        
        String line ;
        String[] primes ;
        String[] value ;
        String[] type ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value = line.split(",") ;
        
        //Finally, the type of equation used to solve it
        line = scanner.nextLine() ;
        type = line.split(",") ;
        
        scanner.close() ;
        
        int currentPrime ;
        int currentValue ;
        int aPminus2 ;
        double primePower = 0 ;
        
        int count = (int)(Math.ceil(logBase(TARGET , Integer.parseInt(primes[0])))) ;
        
        int[][] powerTable = new int[primes.length][count + 1] ;
        
        for(int i = 0 ; i < primes.length ; i++)
        {
            for(int j = 0 ; j < count + 1 ; j++)
            {
                powerTable[i][j] = -1 ;
            }
        }
        
        for(int i = 0 ; i < primes.length ; i++)
        {
            currentPrime = Integer.parseInt(primes[i]) ;
            currentValue = Integer.parseInt(value[i]) ;
            
            if(currentPrime * currentPrime > TARGET)
            {
                break ;
            }
            
            powerTable[i][0] = currentPrime ;
            powerTable[i][1] = currentValue ;
            powerTable[i][count] = Integer.parseInt(type[i]) ;
            
            for(int j = 2 ; j < logBase(TARGET , currentPrime) ; j++)
            {
                if(j == 2)
                {
                    aPminus2 = 1 ;
                }
                else
                {
                    aPminus2 = powerTable[i][j - 2] ;
                }
                
                //evens need to calculated differently because the a(p) have extra information
                //riding along with them when they get squared. For example if a(p) = b * sqrt(6)
                //the a(p^2) needs to account for that and add an additional factor of 6.
                if(j % 2 == 0)
                {
                    switch(modType)
                    {
                        case 11 :
                            if(type[i].equals("1"))
                            {
                                primePower = powerTable[i][1] * powerTable[i][j-1] * (-23) - 
                                             jacobi(23 , currentPrime) * Math.pow(currentPrime % 11 , 9) *
                                             aPminus2 ;                                
                            }
                            if(type[i].equals("2"))
                            {
                                primePower = powerTable[i][1] * powerTable[i][j-1] * 5 - 
                                             jacobi(23 , currentPrime) * Math.pow(currentPrime % 11 , 9) *
                                             aPminus2 ;
                            }

                            break ;
                        case 1 :
                            if(type[i].equals("1"))
                            {
                                primePower = powerTable[i][1] * powerTable[i][j-1] - 
                                             jacobi(23 , currentPrime) * Math.pow(currentPrime % 11 , 9) *
                                             aPminus2 ;
                            }
                            if(type[i].equals("2"))
                            {
                                primePower = powerTable[i][1] * powerTable[i][j-1] * 6 - 
                                             jacobi(23 , currentPrime) * Math.pow(currentPrime % 11 , 9) *
                                             aPminus2 ;                                
                            }
                            break ;
                    }
                }
                else //odds
                {
                    primePower = powerTable[i][1] * powerTable[i][j-1] - 
                                   jacobi(23 , currentPrime) * Math.pow(currentPrime % 11 , 9) *
                                   aPminus2 ;
                }
                
                powerTable[i][j] = adjustedMod(primePower , 11) ;
            }
        }
        
        //Setting things up for file writing
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)) ;
        line = "" ;
        for(int i = 0 ; i < powerTable.length ; i++)
        {
            for(int j = 0 ; j < powerTable[0].length ; j++)
            {
                line = line.concat(powerTable[i][j] + ",") ;
            }
            line = line.concat("\n") ;
        }
        
        writer.write(line) ;
        writer.close() ;  
    }
    
    /**
     * @param inputFile name of the input file as a string
     * @param outputFile name of the output file as a string
     * @param modType value of p % 12 from the input file.
     * @throws IOException 
     * 
     * Should only be used with files produced from the "calculatePrimes" method.
     * Calculates all the powers of primes for primes that are 5 or 7 mod 12 and have 
     * prime powers less than the TARGET value.
     * Output is formatted: p , value(p) , a(p) X , a(p) Y , a(p^2) X , a(p^2) Y , a(p^3) X , a(p^3) Y , ... , -1
     *                      p , value(p) , a(p) X , a(p) Y , a(p^2) X , a(p^2) Y , a(p^3) X , a(p^3) Y , ... , -1
     *                      p , value(p) , a(p) X , a(p) Y , a(p^2) X , a(p^2) Y , a(p^3) X , a(p^3) Y , ... , -1
     *                          ...
     *                      p , value(p) , a(p) X , a(p) Y , -1 , -1 , ... , -1
     * Again, values of -1 are used to correspond to prime powers that exceed the TARGET value. However, the first
     * four columns are still contain the correct values because they get used for combinations later. a(p) X and a(p) Y,
     * are used to represent a(p) in the form a(p) = X + Y sqrt(6).
     */
    public static void calculatePrimePowers5And7Mod12(String inputFile , String outputFile , int modType) throws IOException
    {
        File values = new File(inputFile) ;
        Scanner scanner = new Scanner(values) ;
        
        String line ;
        String[] primes ;
        String[] value ;
        String[] xVal ;
        String[] yVal ;
        
         //Grab the list of primes first
        line = scanner.nextLine() ;
        primes = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value = line.split(",") ;
        
        //There is an extra line we don't use here.
        line = scanner.nextLine() ;
        
        //Then the x and y values
        line = scanner.nextLine() ;
        xVal = line.split(",") ;
        
        line = scanner.nextLine() ;
        yVal = line.split(",") ;
        
        scanner.close() ;
        
        /*
        So the "value" is a constant that gets multiplied to the x and y values.
        Then each actual a(p) has a (for the 5 mod 12, 7 is similar though) 
        i * sqrt(10 - 3sqrt(6)) * (x + 2y + sqrt(6) y).
        In short, these prime power values will end up in the form of a + b sqrt(6)
        for the powers that are interesting to us, namely the even powers since
        they are 1 mod 12.
        */
        
        int currentPrime ;
        int currentValue ;
        int currentX ;
        int currentY ;
        
        //Here A denotes the part without the sqrt(6) and B the part with it.
        int aPminus2A ;
        int aPminus2B ;
        int A1 ;
        int B1 ;
        int Aeven ;
        int Beven ;
        int Aodd ;
        int Bodd ;  
        double p9Times23overP ;
        double primePowerA = 0 ;
        double primePowerB = 0 ;
        
        int count = (int)(Math.ceil(logBase(TARGET , Integer.parseInt(primes[0])))) * 2 ;
        
        int[][] powerTable = new int[primes.length][count + 1] ;
        
        for(int i = 0 ; i < primes.length ; i++)
        {
            for(int j = 0 ; j < count + 1 ; j++)
            {
                powerTable[i][j] = -1 ;
            }
        }
        
        for(int i = 0 ; i < primes.length ; i++)
        {
            currentPrime = Integer.parseInt(primes[i]) ;
            currentValue = Integer.parseInt(value[i]) ;
            currentX = Integer.parseInt(xVal[i]) ;
            currentY = Integer.parseInt(yVal[i]) ;
            
            if(modType == 5)
            {
                primePowerA = currentValue * (currentX + 2 * currentY) ;
                primePowerB = currentValue * currentY ;
            }
            else if(modType == 7)
            {
                primePowerA = currentValue * (currentX - 2 * currentY) ;
                primePowerB = currentValue * (-1) * currentY ;
            }
            
            powerTable[i][0] = currentPrime ;
            powerTable[i][1] = currentValue ;
            powerTable[i][2] = adjustedMod(primePowerA , 11) ;
            powerTable[i][3] = adjustedMod(primePowerB , 11) ;
            
            for(int j = 4 ; j < logBase(TARGET , currentPrime) * 2 ; j += 2)
            {
                if(j == 4)
                {
                    aPminus2A = 1 ;
                    aPminus2B = 0 ;
                }
                else
                {
                    aPminus2A = powerTable[i][j - 4] ;
                    aPminus2B = powerTable[i][j - 3] ;
                }
                
                p9Times23overP = jacobi(23 , currentPrime) * Math.pow(currentPrime % 11 , 9) ;
                
                //evens
                if( (j/2) % 2 == 0)
                {
                    A1 = powerTable[i][2] ;
                    B1 = powerTable[i][3] ;
                    Aeven = aPminus2A ;
                    Beven = aPminus2B ;
                    Aodd = powerTable[i][j-2] ;
                    Bodd = powerTable[i][j-1] ;
                    
                    switch(modType)
                    {
                        case 5 :
                            primePowerA = -10 * A1 * Aodd + 18 * A1 * Bodd + 18 * B1 * Aodd
                                          -60 * B1 * Bodd - p9Times23overP * Aeven ;
                                    
                            primePowerB = 3 * A1 * Aodd - 10 * A1 * Bodd - 10 * B1 * Aodd
                                          + 18 * B1 * Bodd - p9Times23overP * Beven ;
                            break ;
                        case 7 :
                            primePowerA = 14 * A1 * Aodd + 84 * B1 * Bodd - 30 * A1 * Bodd
                                          - 30 * B1 * Aodd - p9Times23overP * Aeven ;
                            primePowerB = 14 * A1 * Bodd + 14 * B1 * Aodd - 5 * A1 * Aodd
                                          - 30 * B1 * Bodd - p9Times23overP * Beven ;
                            break ;
                    }
                }
                else //odds
                {
                    A1 = powerTable[i][2] ;
                    B1 = powerTable[i][3] ;
                    Aodd = aPminus2A ;
                    Bodd = aPminus2B ;
                    Aeven = powerTable[i][j-2] ;
                    Beven = powerTable[i][j-1] ;
                    
                    primePowerA = A1 * Aeven + 6 * B1 * Beven - p9Times23overP * Aodd ;
                    primePowerB = A1 * Beven + B1 * Aeven - p9Times23overP * Bodd ;
                }
                
                powerTable[i][j] = adjustedMod(primePowerA , 11) ;
                powerTable[i][j+1] = adjustedMod(primePowerB , 11) ;
            }
        }
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile)) ;
        line = "" ;
        for(int i = 0 ; i < powerTable.length ; i++)
        {
            for(int j = 0 ; j < powerTable[0].length ; j++)
            {
                line = line.concat(powerTable[i][j] + ",") ;
            }
            line = line.concat("\n") ;
        }
        
        writer.write(line) ;
        writer.close() ; 
    }
    
    /**
     * @param inputFile The name of the input file as a string.
     * @param primePowersFile The name of the second input file as a string.
     * @param outputFile The name of the output file as a string.
     * @param modType The value of p % 12 corresponding to the primes in the input file.
     * @throws IOException 
     * 
     * Since the product of even numbers of 5 or 7 mod 12 primes is 1 mod 12, these values
     * are useful to have on hand for later combinations, as they can be combined with any
     * 11 mod 12 value to create another 11 mod 12 value.
     * 
     * The input file should be one of the outputs from the "calculatePrimes" method and the
     * prime powers file should be the file containing the corresponding prime powers.
     * 
     * Output is in the form v , v , v , ...
     *                       x , x , x , ...
     *                       y , y , y , ...
     *                       p1 , p1 , p1 , ...
     *                       p2 , p2 , p2 , ...
     *                       p3 , p3 , p3 , ...
     *                       p4 , p4 , p4 , ...
     * Where v = p1 * p2 * p3 * p4, and p3 , p4 = -1 meaning v = p1 * p2. And the 
     * x and y are from the representation of a(v) = x + y sqrt(6).
     * 
     * This might need to be extended later or rewritten to use some sort of recursion to handle
     * combinations for larger TARGET values.
     */
    public static void calculateEven5or7Mod12(String inputFile , String primePowersFile , String outputFile , int modType) throws IOException
    {
        File values = new File(inputFile) ;
        Scanner scanner = new Scanner(values) ;
        
        String line ;
        String[] primes ;
        String[] value ;
        String[] xVal ;
        String[] yVal ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value = line.split(",") ;
        
        //There is an extra line we don't use here.
        line = scanner.nextLine() ;
        
        //Then the x and y values
        line = scanner.nextLine() ;
        xVal = line.split(",") ;
        
        line = scanner.nextLine() ;
        yVal = line.split(",") ;
        
        scanner.close() ;
        
        int tempX ;
        int tempY ;
        int tempValue ;
        int firstX ;
        int firstY ;
        int firstPrime ;
        int secondX ;
        int secondY ;
        int secondPrime ;
        int thirdX ;
        int thirdY ;
        int thirdPrime ;
        int fourthX ;
        int fourthY ;
        int fourthPrime ;
        int firstCombinedX ;
        int firstCombinedY ;
        int secondCombinedX ;
        int secondCombinedY ;
        int finalX ;
        int finalY ;
        
        ArrayList<Integer> combinedValues = new ArrayList() ;
        ArrayList<Integer> combinedXValues = new ArrayList() ;
        ArrayList<Integer> combinedYValues = new ArrayList() ;
        ArrayList<Integer> firstFactor = new ArrayList() ;
        ArrayList<Integer> secondFactor = new ArrayList() ;
        ArrayList<Integer> thirdFactor = new ArrayList() ;
        ArrayList<Integer> fourthFactor = new ArrayList() ;
        
        for(int i = 0 ; i < primes.length ; i++)
        {
            tempValue = Integer.parseInt(value[i]) ;
            tempX = Integer.parseInt(xVal[i]) ;
            tempY = Integer.parseInt(yVal[i]) ;
            firstPrime = Integer.parseInt(primes[i]) ;
            if(modType == 5)
            {   
                firstX = tempValue * (tempX + 2 * tempY) ;
                firstY = tempValue * tempY ;
            }
            else
            {
                firstX = tempValue * (tempX - 2 * tempY) ;
                firstY = tempValue * (-1) * tempY ;
            }
            
            for(int j = i + 1 ; j < primes.length ; j++)
            {
                tempValue = Integer.parseInt(value[j]) ;
                tempX = Integer.parseInt(xVal[j]) ;
                tempY = Integer.parseInt(yVal[j]) ;
                secondPrime = Integer.parseInt(primes[j]) ;
                
                if(firstPrime > TARGET / secondPrime)
                {
                    break ;
                }
                
                if(modType == 5)
                {   
                    secondX = tempValue * (tempX + 2 * tempY) ;
                    secondY = tempValue * tempY ;
                    
                    firstCombinedX = -10 * firstX * secondX - 60 * firstY * secondY
                                + 18 * firstX * secondY + 18 * firstY * secondX ;
                    firstCombinedY = -10 * firstX * secondY - 10 * firstY * secondX 
                                + 3 * firstX * secondX + 18 * firstY * secondY ;
                }
                else
                {
                    secondX = tempValue * (tempX - 2 * tempY) ;
                    secondY = tempValue * (-1) * tempY ;
                    
                    firstCombinedX = 14 * firstX * secondX + 84 * firstY * secondY
                                - 30 * firstX * secondY - 30 * firstY * secondX ;
                    firstCombinedY = 14 * firstX * secondY + 14 * firstY * secondX 
                                - 5 * firstX * secondX - 30 * firstY * secondY ;
                }
                
                combinedValues.add(firstPrime * secondPrime) ;
                combinedXValues.add(adjustedMod(firstCombinedX , 11)) ;
                combinedYValues.add(adjustedMod(firstCombinedY , 11)) ;
                firstFactor.add(firstPrime) ;
                secondFactor.add(secondPrime) ;
                thirdFactor.add(-1) ;
                fourthFactor.add(-1) ;
                
                for(int k = j + 1 ; k < primes.length ; k++)
                {
                    tempValue = Integer.parseInt(value[k]) ;
                    tempX = Integer.parseInt(xVal[k]) ;
                    tempY = Integer.parseInt(yVal[k]) ;
                    thirdPrime = Integer.parseInt(primes[k]) ;
                    if(firstPrime * secondPrime > TARGET / thirdPrime)
                    {
                        break ;
                    }
                    
                    if(modType == 5)
                    {   
                        thirdX = tempValue * (tempX + 2 * tempY) ;
                        thirdY = tempValue * tempY ;
                    }
                    else
                    {
                        thirdX = tempValue * (tempX - 2 * tempY) ;
                        thirdY = tempValue * (-1) * tempY ;
                    }
                    
                    for(int m = k + 1 ; m < primes.length ; m++)
                    {
                        tempValue = Integer.parseInt(value[m]) ;
                        tempX = Integer.parseInt(xVal[m]) ;
                        tempY = Integer.parseInt(yVal[m]) ;
                        fourthPrime = Integer.parseInt(primes[m]) ;

                        if(firstPrime * secondPrime * thirdPrime > TARGET / fourthPrime)
                        {
                            break ;
                        }

                        if(modType == 5)
                        {   
                            fourthX = tempValue * (tempX + 2 * tempY) ;
                            fourthY = tempValue * tempY ;

                            secondCombinedX = -10 * thirdX * fourthX - 60 * thirdY * fourthY
                                        + 18 * thirdX * fourthY + 18 * thirdY * fourthX ;
                            secondCombinedY = -10 * thirdX * fourthY - 10 * thirdY * fourthX 
                                        + 3 * thirdX * fourthX + 18 * thirdY * fourthY ;
                        }
                        else
                        {
                            fourthX = tempValue * (tempX - 2 * tempY) ;
                            fourthY = tempValue * (-1) * tempY ;

                            secondCombinedX = 14 * thirdX * fourthX + 84 * thirdY * fourthY
                                        - 30 * thirdX * fourthY - 30 * thirdY * fourthX ;
                            secondCombinedY = 14 * thirdX * fourthY + 14 * thirdY * fourthX 
                                        - 5 * thirdX * fourthX - 30 * thirdY * fourthY ;
                        }
                        
                        finalX = firstCombinedX * secondCombinedX + 6 * firstCombinedY * secondCombinedY ;
                        finalY = firstCombinedX * secondCombinedY + firstCombinedY * secondCombinedX ;
                        
                        combinedValues.add(firstPrime * secondPrime * thirdPrime * fourthPrime) ;
                        combinedXValues.add(adjustedMod(finalX , 11)) ;
                        combinedYValues.add(adjustedMod(finalY , 11)) ;
                        firstFactor.add(firstPrime) ;
                        secondFactor.add(secondPrime) ;
                        thirdFactor.add(thirdPrime) ;
                        fourthFactor.add(fourthPrime) ;
                    }
                }
            }
        }
        
        //Next we want to get the prime power table containing all the values
        //for powers 3 and higher.
        scanner = new Scanner(new File(primePowersFile)) ;
        String[] splitLine ;
        int count ;
        int primePower ;
        line = scanner.nextLine() ;
        
        splitLine = line.split(",") ;
        
        count = primes.length ;
        
        int[][] powerTable = new int[count][splitLine.length - 5] ; 
        
        count = 0 ;
        
        while(scanner.hasNextLine())
        {
            for(int i = 0 ; i < powerTable[0].length ; i++)
            {
                powerTable[count][i] = Integer.parseInt(splitLine[i + 4]) ;
            }
            
            count++ ;
            
            line = scanner.nextLine() ;
            splitLine = line.split(",") ;
            
            if(splitLine[0].equals("-1"))
            {
                break ;
            }
        }
        
        scanner.close() ;
        
        for(int i = 0 ; i < powerTable.length ; i++)
        {
            firstPrime = Integer.parseInt(primes[i]) ;
            //Table starts at p^2 here, only want odd powers.
            for(int j = 2 ; j < powerTable[0].length ; j += 4)
            {
                primePower = (int)Math.pow(firstPrime , (j/2) + 2) ;
                firstX = powerTable[i][j] ;
                firstY = powerTable[i][j+1] ;
                if(firstX == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < primes.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes[k]) ;
                    if(firstPrime == secondPrime)
                    {
                        continue ;
                    }
                    if(primePower > TARGET / secondPrime)
                    {
                        break ;
                    }
                    tempValue = Integer.parseInt(value[k]) ;
                    tempX = Integer.parseInt(xVal[k]) ;
                    tempY = Integer.parseInt(yVal[k]) ;
                    
                    if(modType == 5)
                    {   
                        secondX = tempValue * (tempX + 2 * tempY) ;
                        secondY = tempValue * tempY ;

                        firstCombinedX = -10 * firstX * secondX - 60 * firstY * secondY
                                    + 18 * firstX * secondY + 18 * firstY * secondX ;
                        firstCombinedY = -10 * firstX * secondY - 10 * firstY * secondX 
                                    + 3 * firstX * secondX + 18 * firstY * secondY ;
                    }
                    else
                    {
                        secondX = tempValue * (tempX - 2 * tempY) ;
                        secondY = tempValue * (-1) * tempY ;

                        firstCombinedX = 14 * firstX * secondX + 84 * firstY * secondY
                                    - 30 * firstX * secondY - 30 * firstY * secondX ;
                        firstCombinedY = 14 * firstX * secondY + 14 * firstY * secondX 
                                    - 5 * firstX * secondX - 30 * firstY * secondY ;
                    }
                    
                    combinedValues.add(primePower * secondPrime) ;
                    combinedXValues.add(adjustedMod(firstCombinedX , 11)) ;
                    combinedYValues.add(adjustedMod(firstCombinedY , 11)) ;
                    firstFactor.add(firstPrime) ;
                    secondFactor.add(secondPrime) ;
                    thirdFactor.add(-1) ;
                    fourthFactor.add(-1) ;
                }
            }
        }
        
        //It could also be 1 even power, and 2 5 mod 12
        for(int i = 0 ; i < powerTable.length ; i++)
        {
            firstPrime = Integer.parseInt(primes[i]) ;
            //Table starts at p^2 here, want even powers.
            for(int j = 0 ; j < powerTable[0].length ; j += 4)
            {
                primePower = (int)Math.pow(firstPrime , (j/2) + 2) ;
                firstX = powerTable[i][j] ;
                firstY = powerTable[i][j+1] ;
                if(firstX == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < primes.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes[k]) ;
                    if(firstPrime == secondPrime)
                    {
                        continue ;
                    }
                    if(primePower > TARGET / secondPrime)
                    {
                        break ;
                    }
                    tempValue = Integer.parseInt(value[k]) ;
                    tempX = Integer.parseInt(xVal[k]) ;
                    tempY = Integer.parseInt(yVal[k]) ;
                    
                    if(modType == 5)
                    {   
                        secondX = tempValue * (tempX + 2 * tempY) ;
                        secondY = tempValue * tempY ;
                    }
                    else
                    {
                        secondX = tempValue * (tempX - 2 * tempY) ;
                        secondY = tempValue * (-1) * tempY ;
                    }
                    
                    for(int m = k + 1 ; m < primes.length ; m++)
                    {
                        thirdPrime = Integer.parseInt(primes[m]) ;
                        if(firstPrime == thirdPrime)
                        {
                            continue ;
                        }
                        if(primePower * secondPrime > TARGET / thirdPrime)
                        {
                            break ;
                        }
                        tempValue = Integer.parseInt(value[m]) ;
                        tempX = Integer.parseInt(xVal[m]) ;
                        tempY = Integer.parseInt(yVal[m]) ;
                    
                        if(modType == 5)
                        {   
                            thirdX = tempValue * (tempX + 2 * tempY) ;
                            thirdY = tempValue * tempY ;

                            firstCombinedX = -10 * secondX * thirdX - 60 * secondY * thirdY
                                        + 18 * secondX * thirdY + 18 * secondY * thirdX ;
                            firstCombinedY = -10 * secondX * thirdY - 10 * secondY * thirdX 
                                        + 3 * secondX * thirdX + 18 * secondY * thirdY ;
                        }
                        else
                        {
                            thirdX = tempValue * (tempX - 2 * tempY) ;
                            thirdY = tempValue * (-1) * tempY ;

                            firstCombinedX = 14 * secondX * thirdX + 84 * secondY * thirdY
                                        - 30 * secondX * thirdY - 30 * secondY * thirdX ;
                            firstCombinedY = 14 * secondX * thirdY + 14 * secondY * thirdX 
                                        - 5 * secondX * thirdX - 30 * secondY * thirdY ;
                        }

                        finalX = firstX * firstCombinedX + 6 * firstY * firstCombinedY ;
                        finalY = firstX * firstCombinedY + firstY * firstCombinedX ;

                        combinedValues.add(primePower * secondPrime * thirdPrime) ;
                        combinedXValues.add(adjustedMod(finalX , 11)) ;
                        combinedYValues.add(adjustedMod(finalY , 11)) ;
                        firstFactor.add(firstPrime) ;
                        secondFactor.add(firstPrime) ;
                        thirdFactor.add(secondPrime) ;
                        fourthFactor.add(thirdPrime) ;
                    }
                }
            }
        }
        
        int secondPrimePower ;
        
        //The last combination here should be multiple even powers.
        for(int i = 0 ; i < powerTable.length ; i++)
        {
            firstPrime = Integer.parseInt(primes[i]) ;
            //Table starts at p^2 here, want even powers.
            for(int j = 0 ; j < powerTable[0].length ; j += 4)
            {
                primePower = (int)Math.pow(firstPrime , (j/2) + 2) ;
                firstX = powerTable[i][j] ;
                firstY = powerTable[i][j+1] ;
                if(firstX == -1)
                {
                    break ;
                }
                for(int k = i + 1 ; k < powerTable.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes[k]) ;
                    for(int m = 0 ; m < powerTable[0].length ; m += 4)
                    {
                        secondPrimePower = (int)Math.pow(secondPrime , (m/2) + 2) ;
                        secondX = powerTable[k][m] ;
                        secondY = powerTable[k][m+1] ;
                        if(secondX == -1)
                        {
                            break ;
                        }
                        if(primePower > TARGET / secondPrimePower)
                        {
                            break ;
                        }
                        
                        finalX = firstX * secondX + 6 * firstY * secondY ;
                        finalY = firstX * secondY + firstY * secondX ;
                        
                        combinedValues.add(primePower * secondPrimePower) ;
                        combinedXValues.add(adjustedMod(finalX , 11)) ;
                        combinedYValues.add(adjustedMod(finalY , 11)) ;
                        firstFactor.add(firstPrime) ;
                        secondFactor.add(firstPrime) ;
                        thirdFactor.add(secondPrime) ;
                        fourthFactor.add(secondPrime) ;
                    }
                }
            }
        }
        
        ArrayList[] lines = {combinedValues , combinedXValues , combinedYValues , firstFactor , secondFactor , thirdFactor , fourthFactor} ;
        writeToFile(lines , outputFile) ;    
    }
    
    /**
     * @throws IOException 
     * 
     * Probably one of the more questionable methods I've written.
     * Uses the files produced by the previous methods to create additional
     * combinations of primes that are 11 mod 12.
     * 
     * These include odd numbers of 11 mod 12 primes.
     * Odd numbers of 5 mod 12 primes combined with odd numbers of 7 mod 12 primes.
     * 
     * Then combinations of primes that are 1 mod 12 include
     * Any number of 1 mod 12 primes
     * even numbers of 5 mod 12 prime
     * even numbers of 7 mod 12 primes
     * even numbers of 11 mod 12 primes.
     * 
     * Each combination of 11 mod 12 primes can be combined with any number of
     * combinations that are 1 mod 12 to create more 11 mod 12 primes.
     * 
     * Writes various files as outputs to be checked later. These are generally in the form
     * v , v , v , ... OR v , v , v , ...
     * a , a , a , ...    x , x , x , ...
     * t , t , t , ...    y , y , y , ...
     * Occasionally with some additional rows that may be required for testing.
     */
    public static void combinePrimesAndPowers() throws IOException
    {   
        File primeList11Mod12 = new File("calculatedPrimes11Mod12.csv") ;
        File primeList1Mod12 = new File("calculatedPrimes1Mod12.csv") ;
        Scanner scanner = new Scanner(primeList11Mod12) ;
        
        String line ;
        String[] primes11Mod12 ;
        String[] value11Mod12 ;
        String[] type11Mod12 ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes11Mod12 = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value11Mod12 = line.split(",") ;
        
        //Finally, the type of equation used to solve it
        line = scanner.nextLine() ;
        type11Mod12 = line.split(",") ;
        
        scanner.close() ;
        
        //Grabbing info from the other file.
        scanner = new Scanner(primeList1Mod12) ;
        
        String[] primes1Mod12 ;
        String[] value1Mod12 ;
        String[] type1Mod12 ;
        
        //Grab the list of primes first
        line = scanner.nextLine() ;
        primes1Mod12 = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        value1Mod12 = line.split(",") ;
        
        //Finally, the type of equation used to solve it
        line = scanner.nextLine() ;
        type1Mod12 = line.split(",") ;
        
        scanner.close() ;
        
        //Reading in the combined 1 and 11 mod 12 primes.
        scanner = new Scanner(new File("combinedValues1And11Mod12.csv")) ;
        
        String[] values1and11Mod12 ;
        String[] coef1and11Mod12 ;
        String[] type1and11Mod12 ;
        
        //Grab the list of values first
        line = scanner.nextLine() ;
        values1and11Mod12 = line.split(",") ;
        
        //Then the calculated values
        line = scanner.nextLine() ;
        coef1and11Mod12 = line.split(",") ;
        
        //Finally, the type of equation used to solve it
        line = scanner.nextLine() ;
        type1and11Mod12 = line.split(",") ;
        
        scanner.close() ;
        
        //Grabbing the powers of 1 mod 12 primes
        scanner = new Scanner(new File("primePowers1Mod12.csv")) ;
        String[] splitLine ;
        int count = 0 ;
        int currentPrime ;
        
        line = scanner.nextLine() ;
        
        splitLine = line.split(",") ;
        
        for(int i = 0 ; i < primes1Mod12.length ; i++)
        {
            currentPrime = Integer.parseInt(primes1Mod12[i]) ;
            count = i ;
            if( (currentPrime * currentPrime) > TARGET)
            {
                break ;
            }
        }
        
        int[][] powerTable1Mod12 = new int[count][splitLine.length - 3] ; 
        
        count = 0 ;
        
        while(scanner.hasNextLine())
        {
            for(int i = 0 ; i < powerTable1Mod12[0].length ; i++)
            {
                powerTable1Mod12[count][i] = Integer.parseInt(splitLine[i + 2]) ;
            }
            
            count++ ;
            
            line = scanner.nextLine() ;
            splitLine = line.split(",") ;
            
            if(splitLine[0].equals("-1"))
            {
                break ;
            }
        }
        
        scanner.close() ;
        
        //Grabbing the powers of 5 mod 12 primes now.
        scanner = new Scanner(new File("calculatedPrimes5Mod12.csv")) ;
        String[] primes5Mod12 = scanner.nextLine().split(",") ;
        
        scanner = new Scanner(new File("calculatedPrimes7Mod12.csv")) ;
        String[] primes7Mod12 = scanner.nextLine().split(",") ;
          
        scanner = new Scanner(new File("primePowers5Mod12.csv")) ;
        
        line = scanner.nextLine() ;
        
        splitLine = line.split(",") ;
        
        count = primes5Mod12.length ;
        
        int[][] powerTable5Mod12 = new int[count][splitLine.length - 3] ; 
        
        count = 0 ;
        
        while(scanner.hasNextLine())
        {
            for(int i = 0 ; i < powerTable5Mod12[0].length ; i++)
            {
                powerTable5Mod12[count][i] = Integer.parseInt(splitLine[i + 2]) ;
            }
            
            count++ ;
            
            line = scanner.nextLine() ;
            splitLine = line.split(",") ;
            
            if(splitLine[0].equals("-1"))
            {
                break ;
            }
        }
        
        scanner.close() ;
        
        //Then reading in the powers of 7 mod 12 primes.
        scanner = new Scanner(new File("primePowers7Mod12.csv")) ;
        
        line = scanner.nextLine() ;
        
        splitLine = line.split(",") ;
        
        count = primes7Mod12.length ;
        
        int[][] powerTable7Mod12 = new int[count][splitLine.length - 3] ; 
        
        count = 0 ;
        
        while(scanner.hasNextLine())
        {
            for(int i = 0 ; i < powerTable7Mod12[0].length ; i++)
            {
                powerTable7Mod12[count][i] = Integer.parseInt(splitLine[i + 2]) ;
            }
            
            count++ ;
            
            line = scanner.nextLine() ;
            splitLine = line.split(",") ;
            
            if(splitLine[0].equals("-1"))
            {
                break ;
            }
        }
        
        scanner.close() ;
        
        //And finally, the 11 Mod 12 prime powers.
        scanner = new Scanner(new File("primePowers11Mod12.csv")) ;
        count = 0 ;
        
        line = scanner.nextLine() ;
        
        splitLine = line.split(",") ;
        
        for(int i = 0 ; i < primes11Mod12.length ; i++)
        {
            currentPrime = Integer.parseInt(primes11Mod12[i]) ;
            count = i ;
            if( (currentPrime * currentPrime) > TARGET)
            {
                break ;
            }
        }
        
        int[][] powerTable11Mod12 = new int[count][splitLine.length - 3] ;
        count = 0 ;
        
        while(scanner.hasNextLine())
        {
            for(int i = 0 ; i < powerTable11Mod12[0].length ; i++)
            {
                powerTable11Mod12[count][i] = Integer.parseInt(splitLine[i + 2]) ;
            }
            
            count++ ;
            
            line = scanner.nextLine() ;
            splitLine = line.split(",") ;
            
            if(splitLine[0].equals("-1"))
            {
                break ;
            }
        }
        
        scanner.close() ;
        
        //Actual combination things start here.
        int prime1Mod12 ;
        int primePower1Mod12 ;
        int prime11Mod12 ;
        int combinedValue ;
        int combinedCoef ;
        int firstValue ;
        int xVal ;
        int yVal ;
        int finalX ;
        int finalY ;
        int intermedLoopX ;
        int intermedLoopY ;
        int intermedLoopX2 ;
        int intermedLoopY2 ;
        
        ArrayList<Integer> combinedValues = new ArrayList() ;
        ArrayList<Integer> combinedCoefficients = new ArrayList() ;
        ArrayList<String> combinedType11Mod12 = new ArrayList() ;
        ArrayList<String> combinedType1Mod12 = new ArrayList() ;
        ArrayList<String> powerParity = new ArrayList() ;
        
        //Powers of 1 mod 12 with an 11 mod 12
        for(int i = 0 ; i < powerTable1Mod12.length ; i++)
        {
            prime1Mod12 = Integer.parseInt(primes1Mod12[i]) ;
            
            for(int j = 0 ; j < powerTable1Mod12[0].length ; j++)
            {
                primePower1Mod12 = (int)Math.pow(prime1Mod12 , j+2) ;
                
                if(powerTable1Mod12[i][j] == -1)
                {
                    continue ;
                }
                
                for(int k = 0 ; k < primes11Mod12.length ; k++)
                {
                    prime11Mod12 = Integer.parseInt(primes11Mod12[k]) ;
                    combinedValue = prime11Mod12 * primePower1Mod12 ;
                    if(combinedValue > TARGET)
                    {
                        break ;
                    }
                    
                    combinedCoef = powerTable1Mod12[i][j] * Integer.parseInt(value11Mod12[k]) ;
                    combinedCoef = adjustedMod(combinedCoef , 11) ;
                    
                    combinedValues.add(combinedValue) ;
                    combinedCoefficients.add(combinedCoef) ;
                    combinedType11Mod12.add(type11Mod12[k]) ;
                    combinedType1Mod12.add(type1Mod12[i]) ;
                    
                    if(j % 2 == 0)
                    {
                        powerParity.add("e") ;
                    }
                    else
                    {
                        powerParity.add("o") ;
                    }
                }
            }
        }
        
        //Powers of 1 mod 12 primes with combined 1 and 11 values.
        for(int i = 0 ; i < powerTable1Mod12.length ; i++)
        {
            prime1Mod12 = Integer.parseInt(primes1Mod12[i]) ;
            
            for(int j = 0 ; j < powerTable1Mod12[0].length ; j++)
            {
                primePower1Mod12 = (int)Math.pow(prime1Mod12 , j+2) ;
                
                if(powerTable1Mod12[i][j] == -1)
                {
                    continue ;
                }
                
                for(int k = 0 ; k < values1and11Mod12.length ; k++)
                {
                    firstValue = Integer.parseInt(values1and11Mod12[k]) ;
                    if(primePower1Mod12 > TARGET / firstValue)
                    {
                        continue ;
                    }
                    if(firstValue % prime1Mod12 == 0)
                    {
                        continue ;
                    }
                    
                    combinedCoef = powerTable1Mod12[i][j] * Integer.parseInt(coef1and11Mod12[k]) ;
                    combinedCoef = adjustedMod(combinedCoef , 11) ;
                    
                    combinedValues.add(primePower1Mod12 * firstValue) ;
                    combinedCoefficients.add(combinedCoef) ;
                    combinedType11Mod12.add(type1and11Mod12[k]) ;
                    combinedType1Mod12.add(type1Mod12[i]) ;
                    
                    if(j % 2 == 0)
                    {
                        powerParity.add("e") ;
                    }
                    else
                    {
                        powerParity.add("o") ;
                    }
                }
            }
        }
        
        ArrayList[] lines = {combinedValues , combinedCoefficients , powerParity , combinedType11Mod12 , combinedType1Mod12} ;
        writeToFile(lines , "combinedPrimePowers1Mod12andPrimes11Mod12.csv") ;
       
        combinedValues = new ArrayList() ;
        combinedCoefficients = new ArrayList() ;
        combinedType11Mod12 = new ArrayList() ;
        combinedType1Mod12 = new ArrayList() ;
        powerParity = new ArrayList() ;
        
        int primePower11Mod12 ;
        //odd prime powers of 11 mod 12 with 1 mod 12.
        for(int i = 0 ; i < powerTable11Mod12.length ; i++)
        {
            prime11Mod12 = Integer.parseInt(primes11Mod12[i]) ;
            
            //only want odd powers here
            for(int j = 1 ; j < powerTable11Mod12[0].length ; j += 2)
            {
                primePower11Mod12 = (int)Math.pow(prime11Mod12 , j+2) ;
                
                if(powerTable11Mod12[i][j] == -1 || primePower11Mod12 > TARGET / 13)
                {
                    continue ;
                }
                
                for(int k = 0 ; k < primes1Mod12.length ; k++)
                {
                    prime1Mod12 = Integer.parseInt(primes1Mod12[k]) ;
                    combinedValue = primePower11Mod12 * prime1Mod12 ;
                    if(combinedValue > TARGET || combinedValue < 0)
                    {
                        break ;
                    }
                    
                    combinedCoef = powerTable11Mod12[i][j] * Integer.parseInt(value1Mod12[k]) ;
                    combinedCoef = adjustedMod(combinedCoef , 11) ;
                    
                    combinedValues.add(combinedValue) ;
                    combinedCoefficients.add(combinedCoef) ;
                    combinedType11Mod12.add(type11Mod12[k]) ;
                    combinedType1Mod12.add(type1Mod12[i]) ;
                    
                    if(j % 2 == 0)
                    {
                        powerParity.add("e") ;
                    }
                    else
                    {
                        powerParity.add("o") ;
                    }
                }
            }
        }
        
        ArrayList[] oddPP11Content = {combinedValues , combinedCoefficients , powerParity , combinedType11Mod12 , combinedType1Mod12} ;
        writeToFile(oddPP11Content , "combinedOddPrimePowers11Mod12andPrimes1Mod12.csv") ;

        // We can also get 11 mod 12 values from odd numbers of 11 mod 12 primes.
        int firstPrime ;
        int firstCoef ;
        int firstType ;
        int secondPrime ;
        int secondCoef ;
        int secondType ;
        int thirdPrime ;
        int thirdCoef ;
        int thirdType ;
        int intermediateValue ;
        int overallType ;
        
        combinedValues = new ArrayList() ;
        combinedCoefficients = new ArrayList() ;
        combinedType11Mod12 = new ArrayList() ;
        
        //3 different primes 11 mod 12.
        for(int i = 0 ; i < primes11Mod12.length ; i++)
        {
            firstPrime = Integer.parseInt(primes11Mod12[i]) ;
            firstCoef = Integer.parseInt(value11Mod12[i]) ;
            firstType = Integer.parseInt(type11Mod12[i]) ;
            if(firstPrime > Math.sqrt(TARGET))
            {
                break ;
            }
            
            for(int j = i + 1 ; j < primes11Mod12.length ; j++)
            {
                secondPrime = Integer.parseInt(primes11Mod12[j]) ;
                secondCoef = Integer.parseInt(value11Mod12[j]) ;
                secondType = Integer.parseInt(type11Mod12[j]) ;
                intermediateValue = firstPrime * secondPrime ;
                
                if(intermediateValue > TARGET / 47)
                {
                    break ;
                }
                
                for(int k = j + 1 ; k < primes11Mod12.length ; k++)
                {
                    thirdPrime = Integer.parseInt(primes11Mod12[k]) ;
                    thirdCoef = Integer.parseInt(value11Mod12[k]) ;
                    thirdType = Integer.parseInt(type11Mod12[k]) ;

                    combinedValue = intermediateValue * thirdPrime ;
                    if(combinedValue > TARGET)
                    {
                        break ;
                    }

                    //There are 8 arrangements of type, each corresponding to 4 values
                    overallType = firstType + secondType + thirdType ;

                    //All three are type 1 primes means we have sqrt(-23) ^ 3
                    //The last sqrt(-23) gets removed later.
                    if(overallType == 3)
                    {
                        combinedCoef = firstCoef * secondCoef * thirdCoef * (-23) ;
                        overallType = 1 ;
                    }
                    //All three are type 2 primes means we have sqrt(-138)^3
                    //Which means we are left with a sqrt(6) once the sqrt(-23) gets removed
                    else if(overallType == 6)
                    {
                        combinedCoef = firstCoef * secondCoef * thirdCoef * (-138) ;
                        overallType = 2 ;
                    }
                    //Here we have 2 type 1 primes and 1 type 2 primes, giving us -23 * sqrt(-138)
                    //So again a leftover sqrt(6) 
                    else if(overallType == 4)
                    {
                        combinedCoef = firstCoef * secondCoef * thirdCoef * (-23) ;
                        overallType = 2 ;
                    }
                    //Here we have 2 type 2 and 1 type 1, giving us -138 * sqrt(-23)
                    else if(overallType == 5)
                    {
                        combinedCoef = firstCoef * secondCoef * thirdCoef * (-138) ;
                        overallType = 1 ;
                    }
                    else
                    {
                        //This should never happen, but we need to handle it if it does.
                        combinedCoef = 0 ;
                        overallType = -1 ;
                    }
                    //At the end the overall type will show if the value is as shown (1) or if
                    //it has an additional sqrt(6) (2).

                    combinedCoef = adjustedMod(combinedCoef , 11) ;

                    combinedCoefficients.add(combinedCoef) ;
                    combinedValues.add(combinedValue) ;
                    combinedType11Mod12.add(Integer.toString(overallType)) ;
                }
            }
        }
        
        //odd powers of 11 mod 12 primes.
        for(int i = 0 ; i < powerTable11Mod12.length ; i++)
        {
            firstPrime = Integer.parseInt(primes11Mod12[i]) ;
            
            //only odd powers
            for(int j = 1 ; j < powerTable11Mod12[0].length ; j += 2)
            {
                primePower11Mod12 = (int)Math.pow(firstPrime , j + 2) ;
                firstCoef = powerTable11Mod12[i][j] ;
                if(firstCoef == -1 || primePower11Mod12 > TARGET)
                {
                    break ;
                }
                
                combinedValues.add(primePower11Mod12) ;
                combinedCoefficients.add(firstCoef) ;
                combinedType11Mod12.add(type11Mod12[i]) ;
            }
        }
        
        //Even power of an 11 mod 12 prime and another 11 mod 12 prime
        for(int i = 0 ; i < powerTable11Mod12.length ; i++)
        {
            firstPrime = Integer.parseInt(primes11Mod12[i]) ;
            firstType = Integer.parseInt(type11Mod12[i]) ;
            
            //only even powers
            for(int j = 0 ; j < powerTable11Mod12[0].length ; j += 2)
            {
                primePower11Mod12 = (int)Math.pow(firstPrime , j + 2) ;
                firstCoef = powerTable11Mod12[i][j] ;
                if(firstCoef == -1 || primePower11Mod12 > TARGET)
                {
                    break ;
                }
                
                for(int k = 0 ; k < primes11Mod12.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes11Mod12[k]) ;
                    secondCoef = Integer.parseInt(value11Mod12[k]) ;
                    secondType = Integer.parseInt(type11Mod12[k]) ;
                    if(firstPrime == secondPrime)
                    {
                        continue ;
                    }
                    if(primePower11Mod12 > TARGET / secondPrime)
                    {
                        break ;
                    }
                    
                    combinedCoef = firstCoef * secondCoef ;
                    combinedCoef = adjustedMod(combinedCoef , 11) ;
                    
                    combinedValues.add(primePower11Mod12 * secondPrime) ;
                    combinedCoefficients.add(combinedCoef) ;
                    combinedType11Mod12.add(Integer.toString(secondType)) ;
                }   
            }
        }
        //Going to skip combinations with 5+ different 11 mod 12 primes, since the
        //smallest value there is over 100 million.
        
        ArrayList[] three11Content = {combinedValues , combinedCoefficients , combinedType11Mod12} ;
        writeToFile(three11Content , "three11Mod12Primes.csv") ;
        
        //So the next group is going to be multiple 1 mod 12 primes combined with 1 11 mod 12 prime.
        // We should only need up to 3 1 mod 12 primes, since 4 gives us a minimum of 23 million.
        int fourthPrime ;
        int fourthCoef ;
        int fourthType ;
        
        combinedValues = new ArrayList() ;
        combinedCoefficients = new ArrayList() ;
        ArrayList<Integer> overallTypes = new ArrayList() ;
        
        for(int i = 0 ; i < primes1Mod12.length ; i++)
        {
            firstPrime = Integer.parseInt(primes1Mod12[i]) ;
            firstCoef = Integer.parseInt(value1Mod12[i]) ;
            firstType = Integer.parseInt(type1Mod12[i]) ;
            
            if(firstPrime > TARGET / 37)
            {
                break ;
            }
            
            for(int j = i + 1 ; j < primes1Mod12.length ; j++)
            {
                secondPrime = Integer.parseInt(primes1Mod12[j]) ;
                secondCoef = Integer.parseInt(value1Mod12[j]) ;
                secondType = Integer.parseInt(type1Mod12[j]) ;
                intermediateValue = firstPrime * secondPrime ;
                
                //The smallest 11 mod 12 prime is 11, so if intermediate value * 11 is bigger than
                // the target we dont need to continue.
                if(intermediateValue > TARGET / 11)
                {
                    break ;
                }
                
                for(int k = 0 ; k < primes11Mod12.length ; k++)
                {
                    thirdPrime = Integer.parseInt(primes11Mod12[k]) ;
                    thirdCoef = Integer.parseInt(value11Mod12[k]) ;
                    thirdType = Integer.parseInt(type11Mod12[k]) ;
                    combinedValue = intermediateValue * thirdPrime ;
                    //Accounting for potential overflow here.
                    if(combinedValue > TARGET || combinedValue < 0)
                    {
                        break ;
                    }
                    
                    //For the types, there are still 8 different combinations, but
                    //now the order matters, since we are combining 11 and 1 mod 12.
                    if(firstType == 1)
                    {
                        if(secondType == 1)
                        {
                            // 1 , 1 , 1 -> Only a sqrt(-23)
                            if(thirdType == 1)
                            {
                                combinedCoef = firstCoef * secondCoef * thirdCoef ;
                                overallType = 1 ;
                            }
                            // 1 , 1 , 2 -> Only a sqrt(-138) = sqrt(-23) * sqrt(6)
                            else
                            {
                                combinedCoef = firstCoef * secondCoef * thirdCoef ;
                                overallType = 2 ;
                            }
                        }
                        else
                        {
                            // 1 , 2 , 1 -> sqrt(6) * sqrt(-23)
                            if(thirdType == 1)
                            {
                                combinedCoef = firstCoef * secondCoef * thirdCoef ;
                                overallType = 2 ;
                            }
                            // 1 , 2 , 2 -> sqrt(6) * sqrt(-138) = 6 * sqrt(-23)
                            else
                            {
                                combinedCoef = firstCoef * secondCoef * thirdCoef * (6) ;
                                overallType = 1 ;
                            }
                        }
                    }
                    else
                    {
                        if(secondType == 1)
                        {
                            // 2 , 1 , 1 - > sqrt(6) * sqrt(-23)
                            if(thirdType == 1)
                            {
                                combinedCoef = firstCoef * secondCoef * thirdCoef ;
                                overallType = 2 ;
                            }
                            // 2 , 1 , 2 -> sqrt(6) * sqrt(-138) = 6 * sqrt(-23)
                            else
                            {
                                combinedCoef = firstCoef * secondCoef * thirdCoef * (6) ;
                                overallType = 1 ;
                            }
                        }
                        else
                        {
                            // 2 , 2 , 1 -> 6 * sqrt(-23)
                            if(thirdType == 1)
                            {
                                combinedCoef = firstCoef * secondCoef * thirdCoef * (6) ;
                                overallType = 1 ;
                            }
                            // 2 , 2 , 2 -> 6 * sqrt(-138)
                            else
                            {
                                combinedCoef = firstCoef * secondCoef * thirdCoef * (6) ;
                                overallType = 2 ;
                            }
                        }
                    }
                    
                    combinedCoef = adjustedMod(combinedCoef , 11) ;
                    
                    combinedValues.add(combinedValue) ;
                    combinedCoefficients.add(combinedCoef) ;
                    overallTypes.add(overallType) ;
                    
                    //But wait, there's more! We can have potentially 2 more 1 mod 12 primes.
                    //61 is the next smallest value we could add.
                    if(combinedValue < TARGET / 61)
                    {
                        for(int m = j + 1 ; m < primes1Mod12.length ; m++)
                        {
                            fourthPrime = Integer.parseInt(primes1Mod12[m]) ;
                            fourthCoef = Integer.parseInt(value1Mod12[m]) ;
                            fourthType = Integer.parseInt(type1Mod12[m]) ;
                            combinedValue = firstPrime * secondPrime * thirdPrime * fourthPrime ;
                            if(combinedValue > TARGET || combinedValue < 0)
                            {
                                break ;
                            }

                            if(fourthType == 1)
                            {
                                //Nothing gets added besides the coefficient.
                                if(overallType == 1)
                                {
                                    combinedCoef = firstCoef * secondCoef * thirdCoef * fourthCoef ;
                                    overallType = 1 ;
                                }
                                else
                                {
                                    combinedCoef = firstCoef * secondCoef * thirdCoef * fourthCoef ;
                                    overallType = 2 ;
                                }
                            }
                            else
                            {
                                //A sqrt(6) gets added.
                                if(overallType == 1)
                                {
                                    combinedCoef = firstCoef * secondCoef * thirdCoef * fourthCoef ;
                                    overallType = 2 ;
                                }
                                else
                                {
                                    //In this case there was already a sqrt(6), so they combine.
                                    combinedCoef = firstCoef * secondCoef * thirdCoef * fourthCoef ;
                                    overallType = 1 ;
                                }
                            }

                            combinedCoef = adjustedMod(combinedCoef , 11) ;

                            combinedValues.add(combinedValue) ;
                            combinedCoefficients.add(combinedCoef) ;
                            overallTypes.add(overallType) ;
                        }
                    }
                }
            }
        }
        
        ArrayList[] twoOrThree1With11Content = {combinedValues , combinedCoefficients , overallTypes} ;
        writeToFile(twoOrThree1With11Content , "twoOrThree1Mod12PrimesAnd11Mod12Prime.csv") ;
        
        //I think the next step will be to combine the combined 5 and 7 mod 12 values with 1 mod 12 primes.
        scanner = new Scanner(new File("combinedValues5and7Mod12.csv")) ;
        String[] combined5and7Value = scanner.nextLine().split(",") ;
        String[] combined5and7X = scanner.nextLine().split(",") ;
        String[] combined5and7Y = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        combinedValues = new ArrayList() ;
        ArrayList<Integer> combinedXValues = new ArrayList() ;
        ArrayList<Integer> combinedYValues = new ArrayList() ;
        
        //combined 5 and 7 primes with some different 1 mod 12 primes.
        for(int i = 0 ; i < combined5and7Value.length ; i++)
        {
            firstValue = Integer.parseInt(combined5and7Value[i]) ;
            //The smallest value we can add is 13. 
            if(firstValue > TARGET / 13)
            {
                continue ;
            }
            
            xVal = Integer.parseInt(combined5and7X[i]) ;
            yVal = Integer.parseInt(combined5and7Y[i]) ;
            
            for(int j = 0 ; j < primes1Mod12.length ; j++)
            {   
                firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                firstCoef = Integer.parseInt(value1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;
                intermediateValue = firstValue * firstPrime ;
                
                if(firstValue > TARGET / firstPrime || intermediateValue < 0)
                {
                    break ;
                }
                
                // Just multiply x and y by the coefficient.
                if(firstType == 1)
                {
                    finalX = xVal * firstCoef ;
                    finalY = yVal * firstCoef ;
                }
                // Multiply x and y by the coefficient, multiply y by 6, then swap x and y.
                else
                {
                    finalX = yVal * firstCoef * 6 ;
                    finalY = xVal * firstCoef ;
                }
                
                finalX = adjustedMod(finalX , 11) ;
                finalY = adjustedMod(finalY , 11) ;
                
                intermedLoopX = finalX ;
                intermedLoopY = finalY ;

                combinedValues.add(intermediateValue) ;
                combinedXValues.add(finalX) ;
                combinedYValues.add(finalY) ;
                
                //We need to go up to 4 values, as 5 gets us well over 1 billion.
                //So we need up to 2 more 1 mod 12 values. (Or exactly 2 11 mod 12 values)
                //Or some even powers of 5 or 7 mod 12 primes.
                
                //The next smallest value we could add is 37. And by add I mean multiply.
                if(intermediateValue < TARGET / 37)
                {
                    for(int k = j + 1 ; k < primes1Mod12.length ; k++)
                    {
                        secondPrime = Integer.parseInt(primes1Mod12[k]) ;
                        secondCoef = Integer.parseInt(value1Mod12[k]) ;
                        secondType = Integer.parseInt(type1Mod12[k]) ;
                        combinedValue = intermediateValue * secondPrime ;
                        
                        if(intermediateValue > TARGET / secondPrime || combinedValue < 0)
                        {
                            break ;
                        }
                        
                        //Same as previous.
                        if(secondType == 1)
                        {
                            finalX = intermedLoopX * secondCoef ;
                            finalY = intermedLoopY * secondCoef ;
                        }
                        else
                        {
                            finalX = intermedLoopY * secondCoef * 6 ;
                            finalY = intermedLoopX * secondCoef ;
                        }
                        
                        finalX = adjustedMod(finalX , 11) ;
                        finalY = adjustedMod(finalY , 11) ;
                        
                        intermedLoopX2 = finalX ;
                        intermedLoopY2 = finalY ;
                        
                        combinedValues.add(combinedValue) ;
                        combinedXValues.add(finalX) ;
                        combinedYValues.add(finalY) ;
                        
                        if(combinedValue < TARGET / 61)
                        {
                            for(int m = k + 1 ; m < primes1Mod12.length ; m++)
                            {
                                thirdPrime = Integer.parseInt(primes1Mod12[m]) ;
                                thirdCoef = Integer.parseInt(value1Mod12[m]) ;
                                thirdType = Integer.parseInt(type1Mod12[m]) ;
                                combinedValue = intermediateValue * secondPrime * thirdPrime ;

                                if(combinedValue > TARGET || combinedValue < 0)
                                {
                                    break ;
                                }

                                //Same as previous.
                                if(thirdType == 1)
                                {
                                    finalX = intermedLoopX2 * thirdCoef ;
                                    finalY = intermedLoopY2 * thirdCoef ;
                                }
                                else
                                {
                                    finalX = intermedLoopY2 * thirdCoef * 6 ;
                                    finalY = intermedLoopX2 * thirdCoef ;
                                }

                                finalX = adjustedMod(finalX , 11) ;
                                finalY = adjustedMod(finalY , 11) ;

                                combinedValues.add(combinedValue) ;
                                combinedXValues.add(finalX) ;
                                combinedYValues.add(finalY) ;
                            }
                        }
                    }
                } 
            }
        }
        //We can also use powers of 1 mod 12 primes.
        for(int i = 0 ; i < powerTable1Mod12.length ; i++)
        {
            firstPrime = Integer.parseInt(primes1Mod12[i]) ;
            firstType = Integer.parseInt(type1Mod12[i]) ;
            
            for(int j = 0 ; j < powerTable1Mod12[i].length ; j++)
            {
                primePower1Mod12 = (int)Math.pow(firstPrime , j + 2) ;
                firstCoef = powerTable1Mod12[i][j] ;
                if(firstCoef == -1)
                {
                    break ;
                }  
                for(int k = 0 ; k < combined5and7Value.length ; k++)
                {
                    firstValue = Integer.parseInt(combined5and7Value[k]) ;
                    if(primePower1Mod12 > TARGET / firstValue)
                    {
                        continue ;
                    }
                    xVal = Integer.parseInt(combined5and7X[k]) ;
                    yVal = Integer.parseInt(combined5and7Y[k]) ;
                    
                    if(firstType == 1)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else if(firstType == 2 && j % 2 == 0)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else
                    {
                        finalX = yVal * firstCoef * 6 ;
                        finalY = xVal * firstCoef ;
                    }
                    
                    intermedLoopX = finalX ;
                    intermedLoopY = finalY ;
                    
                    combinedValues.add(primePower1Mod12 * firstValue) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                    
                    for(int m = 0 ; m < primes1Mod12.length ; m++)
                    {
                        secondPrime = Integer.parseInt(primes1Mod12[m]) ;
                        if(firstPrime == secondPrime)
                        {
                            continue ;
                        }
                        secondCoef = Integer.parseInt(value1Mod12[m]) ;
                        secondType = Integer.parseInt(type1Mod12[m]) ;
                        if(primePower1Mod12 * firstValue > TARGET / secondPrime)
                        {
                            break ;
                        }
                        
                        if(secondType == 1)
                        {
                            finalX = intermedLoopX * secondCoef ;
                            finalY = intermedLoopY * secondCoef ;
                        }
                        else
                        {
                            finalX = intermedLoopY * secondCoef * 6 ;
                            finalY = intermedLoopX * secondCoef ;
                        }
                        
                        combinedValues.add(primePower1Mod12 * firstValue * secondPrime) ;
                        combinedXValues.add(adjustedMod(finalX , 11)) ;
                        combinedYValues.add(adjustedMod(finalY , 11)) ;
                    }
                }
            }
        }
        
        ArrayList[] combined5And7And1Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(combined5And7And1Content , "combined5and7Mod12PrimesAndSome1Mod12Primes.csv") ;
        
        //Now we do basically the same thing, but with exactly 2 11 mod 12 primes instead.
        combinedValues = new ArrayList() ;
        combinedXValues = new ArrayList() ;
        combinedYValues = new ArrayList() ;
        
        //Combined 5 and 7 mod 12 primes with exactly 2 different 11 mod 12 primes.
        for(int i = 0 ; i < combined5and7Value.length ; i++)
        {
            firstValue = Integer.parseInt(combined5and7Value[i]) ;
            xVal = Integer.parseInt(combined5and7X[i]) ;
            yVal = Integer.parseInt(combined5and7Y[i]) ;
            
            if(firstValue > TARGET / 11)
            {
                continue ;
            }
            for(int j = 0 ; j < primes11Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes11Mod12[j]) ;
                firstCoef = Integer.parseInt(value11Mod12[j]) ;
                firstType = Integer.parseInt(type11Mod12[j]) ;
                intermediateValue = firstValue * firstPrime ;
                if(intermediateValue > TARGET / 47)
                {
                    break ;
                }
                for(int k = j + 1 ; k < primes11Mod12.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes11Mod12[k]) ;
                    secondCoef = Integer.parseInt(value11Mod12[k]) ;
                    secondType = Integer.parseInt(type11Mod12[k]) ;
                    combinedValue = intermediateValue * secondPrime ;
                    if(combinedValue > TARGET || combinedValue < 0)
                    {
                        break ;
                    }
                    
                    //1 and 1 means we have an extra sqrt(-23)^2 = -23
                    if(firstType == 1 && secondType == 1)
                    {
                        finalX = xVal * firstCoef * secondCoef * -23 ;
                        finalY = yVal * firstCoef * secondCoef * -23 ;
                    }
                    //2 and 2 means we have sqrt(-138)^2 = -138
                    else if(firstType == 2 && secondType == 2)
                    {
                        finalX = xVal * firstCoef * secondCoef * -138 ;
                        finalY = yVal * firstCoef * secondCoef * -138 ;
                    }
                    //1 and 2 means we have sqrt(-23) * sqrt(-138) = -23 * sqrt(6)
                    //So multiply both by -23, and y by 6 and swap the x and y values.
                    else
                    {
                        finalX = yVal * firstCoef * secondCoef * -23 * 6 ;
                        finalY = xVal * firstCoef * secondCoef * -23 ;
                    }
                    
                    finalX = adjustedMod(finalX , 11) ;
                    finalY = adjustedMod(finalY , 11) ;
                    
                    combinedValues.add(combinedValue) ;
                    combinedXValues.add(finalX) ;
                    combinedYValues.add(finalY) ;
                }
            }
        }
        
        //We can also use an even power of an 11 mod 12 prime.
        for(int i = 0 ; i < powerTable11Mod12.length ; i++)
        {
            firstPrime = Integer.parseInt(primes11Mod12[i]) ;
            firstType = Integer.parseInt(type11Mod12[i]) ;
            for(int j = 0 ; j < powerTable11Mod12[i].length ; j += 2)
            {
                primePower11Mod12 = (int)Math.pow(firstPrime , j + 2) ;
                firstCoef = powerTable11Mod12[i][j] ;
                if(firstCoef == -1)
                {
                    break ;
                }
                for(int k = 0 ; k < combined5and7Value.length ; k++)
                {
                    firstValue = Integer.parseInt(combined5and7Value[k]) ;
                    xVal = Integer.parseInt(combined5and7X[k]) ;
                    yVal = Integer.parseInt(combined5and7Y[k]) ;
                    
                    if(primePower11Mod12 > TARGET / firstValue)
                    {
                        continue ;
                    }

                    finalX = xVal * firstCoef ;
                    finalY = yVal * firstCoef ;
                    
                    combinedValues.add(firstValue * primePower11Mod12) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        ArrayList[] two11And5And7Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(two11And5And7Content , "combined5and7Mod12PrimesAndTwo11Mod12Primes.csv") ;
        
        // Here we are going to start combinations using powers of 5 and 7 mod 12 primes.
        int prime5Mod12 ;
        int primePower5Mod12 ;
        int combinedX ;
        int combinedY ;
        
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        //Even power of 5 with one 11.
        for(int i = 0 ; i < powerTable5Mod12.length ; i++)
        {
            prime5Mod12 = Integer.parseInt(primes5Mod12[i]) ;
            
            for(int j = 2 ; j < powerTable5Mod12[0].length ; j += 4)
            {
                primePower5Mod12 = (int)Math.pow(prime5Mod12 , (j / 2) + 1) ;
                
                if(powerTable5Mod12[i][j] == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < primes11Mod12.length ; k++)
                {
                    prime11Mod12 = Integer.parseInt(primes11Mod12[k]) ;
                    combinedValue = prime11Mod12 * primePower5Mod12 ;
                    firstType = Integer.parseInt(type11Mod12[k]) ;
                    if(combinedValue > TARGET)
                    {
                        break ;
                    }
                    
                    if(firstType == 1)
                    {
                        combinedX = powerTable5Mod12[i][j] * Integer.parseInt(value11Mod12[k]) ;
                        combinedY = powerTable5Mod12[i][j + 1] * Integer.parseInt(value11Mod12[k]) ;
                    }
                    else
                    {
                        combinedX = powerTable5Mod12[i][j + 1] * Integer.parseInt(value11Mod12[k]) * 6 ;
                        combinedY = powerTable5Mod12[i][j] * Integer.parseInt(value11Mod12[k]) ;
                    }
                    
                    combinedX = adjustedMod(combinedX , 11) ;
                    combinedY = adjustedMod(combinedY , 11) ;
                    
                    combinedValues.add(combinedValue) ;
                    combinedXValues.add(combinedX) ;
                    combinedYValues.add(combinedY) ;
                }
            }
        }
        
        //We can also do combinations of 2 or 4 5 mod 12 primes with an 11 mod 12 prime.
        scanner = new Scanner(new File("combinedEvenPrimes5Mod12.csv")) ;
        String[] combined5Values = scanner.nextLine().split(",") ;
        String[] combinedEven5X = scanner.nextLine().split(",") ;
        String[] combinedEven5Y = scanner.nextLine().split(",") ;
        String[] first5Factor = scanner.nextLine().split(",") ;
        String[] second5Factor = scanner.nextLine().split(",") ;
        String[] third5Factor = scanner.nextLine().split(",") ;
        String[] fourth5Factor = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        //Even combination of 5 with 11 mod 12
        for(int i = 0 ; i < combined5Values.length ; i++)
        {
            firstValue = Integer.parseInt(combined5Values[i]) ;
            xVal = Integer.parseInt(combinedEven5X[i]) ;
            yVal = Integer.parseInt(combinedEven5Y[i]) ;
            
            if(firstValue > TARGET / Integer.parseInt(primes11Mod12[0]))
            {
                continue ;
            }
            
            for(int j = 0 ; j < primes11Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes11Mod12[j]) ;
                firstCoef = Integer.parseInt(value11Mod12[j]) ;
                firstType = Integer.parseInt(type11Mod12[j]) ;
                
                if(firstValue > TARGET / firstPrime)
                {
                    break ;
                }
                
                if(firstType == 1)
                {
                    finalX = xVal * firstCoef ;
                    finalY = yVal * firstCoef ;
                }
                else
                {
                    finalX = yVal * firstCoef * 6 ;
                    finalY = xVal * firstCoef ;
                }
                
                combinedValues.add(firstPrime * firstValue) ;
                combinedXValues.add(adjustedMod(finalX , 11)) ;
                combinedYValues.add(adjustedMod(finalY , 11)) ;
            }
        }
                
        ArrayList[] evenPowers5And11Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(evenPowers5And11Content , "combinedEvenPrimePowers5Mod12andPrimes11Mod12.csv") ;
        
        //We can also do an even power or number of 5 mod 12 with a 5 and 7 combined.
        //Need to be careful to not make an odd power of a 5 mod 12 though.
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        //Even power 5 mod 12 with combined 5 and 7 mod 12.
        for(int i = 0 ; i < powerTable5Mod12.length ; i++)
        {
            prime5Mod12 = Integer.parseInt(primes5Mod12[i]) ;
            
            for(int j = 2 ; j < powerTable5Mod12[0].length ; j += 4)
            {
                primePower5Mod12 = (int)Math.pow(prime5Mod12 , (j / 2) + 1) ;
                
                if(powerTable5Mod12[i][j] == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < combined5and7Value.length ; k++)
                {
                    firstValue = Integer.parseInt(combined5and7Value[k]) ;
                    combinedValue = firstValue * primePower5Mod12 ;
                    xVal = Integer.parseInt(combined5and7X[k]) ;
                    yVal = Integer.parseInt(combined5and7Y[k]) ;
                    if(primePower5Mod12 > TARGET / firstValue || firstValue % prime5Mod12 == 0)
                    {
                        continue ;
                    }
                    
                    combinedX = powerTable5Mod12[i][j] * xVal + 6 * powerTable5Mod12[i][j+1] * yVal ;
                    combinedY = powerTable5Mod12[i][j] * yVal + powerTable5Mod12[i][j+1] * xVal ;
                    
                    combinedX = adjustedMod(combinedX , 11) ;
                    combinedY = adjustedMod(combinedY , 11) ;
                    
                    combinedValues.add(combinedValue) ;
                    combinedXValues.add(combinedX) ;
                    combinedYValues.add(combinedY) ;
                }
            }
        }
        
        int firstFactor ;
        int secondFactor ;
        int thirdFactor ;
        int fourthFactor ;
        int firstXVal ;
        int firstYVal ;
        int secondValue ;
        
        //Even number of 5 mod 12 with combined 5 and 7 mod 12.
        for(int i = 0 ; i < combined5Values.length ; i++)
        {
            firstValue = Integer.parseInt(combined5Values[i]) ;
            firstXVal = Integer.parseInt(combinedEven5X[i]) ;
            firstYVal = Integer.parseInt(combinedEven5Y[i]) ;
            firstFactor = Integer.parseInt(first5Factor[i]) ;
            secondFactor = Integer.parseInt(second5Factor[i]) ;
            thirdFactor = Integer.parseInt(third5Factor[i]) ;
            fourthFactor = Integer.parseInt(fourth5Factor[i]) ;
            
            for(int j = 0 ; j < combined5and7Value.length ; j++)
            {
                secondValue = Integer.parseInt(combined5and7Value[j]) ;
                xVal = Integer.parseInt(combined5and7X[j]) ;
                yVal = Integer.parseInt(combined5and7Y[j]) ;
                
                if(firstValue > TARGET / secondValue)
                {
                    continue ;
                }
                
                //We need to be careful to not make a 5^2, because they are defined differently.
                if(secondValue % firstFactor == 0 || secondValue % secondFactor == 0)
                {
                    continue ;
                }
                if(thirdFactor != -1)
                {
                    if(secondValue % thirdFactor == 0 || secondValue % fourthFactor == 0)
                    {
                        continue ;
                    }
                }
                
                finalX = firstXVal * xVal + 6 * firstYVal * yVal ;
                finalY = firstXVal * yVal + firstYVal * xVal ;
                
                combinedValues.add(firstValue * secondValue) ;
                combinedXValues.add(adjustedMod(finalX , 11)) ;
                combinedYValues.add(adjustedMod(finalY , 11)) ;
            }
        }
        
        ArrayList[] even5with5and7Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(even5with5and7Content , "combinedEvenPrimePower5And5and7.csv") ;
        
        //Then we can take those values and combine them with a 1 mod 12 prime.
        scanner = new Scanner(new File("combinedEvenPrimePower5And5and7.csv")) ;
        String[] even5and5and7Value = scanner.nextLine().split(",") ;
        String[] even5and5and7X = scanner.nextLine().split(",") ;
        String[] even5and5and7Y = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        //Even 5 with combined 5 and 7 with 1 mod 12.
        for(int i = 0 ; i < even5and5and7Value.length ; i++)
        {
            firstValue = Integer.parseInt(even5and5and7Value[i]) ;
            xVal = Integer.parseInt(even5and5and7X[i]) ;
            yVal = Integer.parseInt(even5and5and7Y[i]) ;
            if(firstValue < TARGET / 13)
            {
                for(int j = 0 ; j < primes1Mod12.length ; j++)
                {
                    firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                    firstCoef = Integer.parseInt(value1Mod12[j]) ;
                    firstType = Integer.parseInt(type1Mod12[j]) ;
                    
                    if(firstValue > TARGET / firstPrime)
                    {
                        break ;
                    }
                    
                    if(firstType == 1)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else
                    {
                        finalX = yVal * firstCoef * 6 ;
                        finalY = xVal * firstCoef ;
                    }
                    
                    combinedValues.add(firstValue * firstPrime) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        //Powers of 1 also work.
        for(int i = 0 ; i < even5and5and7Value.length ; i++)
        {
            firstValue = Integer.parseInt(even5and5and7Value[i]) ;
            xVal = Integer.parseInt(even5and5and7X[i]) ;
            yVal = Integer.parseInt(even5and5and7Y[i]) ;
            if(firstValue < TARGET / 13)
            {
                for(int j = 0 ; j < powerTable1Mod12.length ; j++)
                {
                    firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                    firstType = Integer.parseInt(type1Mod12[j]) ;
                    for(int k = 0 ; k < powerTable1Mod12[0].length ; k++)
                    {
                        primePower1Mod12 = (int)Math.pow(firstPrime , k + 2) ;
                        firstCoef = powerTable1Mod12[j][k] ;
                        if(firstValue > TARGET / primePower1Mod12)
                        {
                            break ;
                        }
                        if(firstCoef == -1)
                        {
                            break ;
                        }

                        if(firstType == 1)
                        {
                            finalX = xVal * firstCoef ;
                            finalY = yVal * firstCoef ;
                        }
                        else if(firstType == 2 && j % 2 != 0)
                        {
                            finalX = yVal * firstCoef * 6 ;
                            finalY = xVal * firstCoef ;
                        }
                        else
                        {
                            finalX = xVal * firstCoef ;
                            finalY = yVal * firstCoef ;
                        }

                        combinedValues.add(firstValue * primePower1Mod12) ;
                        combinedXValues.add(adjustedMod(finalX , 11)) ;
                        combinedYValues.add(adjustedMod(finalY , 11)) ;
                    }
                }
            }
        }
        
        ArrayList[] combined5571Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(combined5571Content , "combinedEven5and5and7and1Mod12.csv") ;
        
        //Even powers of 7 mod 12 primes are also 1 mod 12, so we can make similar
        //combinations here.
        int prime7Mod12 ;
        int primePower7Mod12 ;
       
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        //Even power of 7 mod 12 with a 11 mod 12 prime.
        for(int i = 0 ; i < powerTable7Mod12.length ; i++)
        {
            prime7Mod12 = Integer.parseInt(primes7Mod12[i]) ;
            
            for(int j = 2 ; j < powerTable7Mod12[0].length ; j += 4)
            {
                primePower7Mod12 = (int)Math.pow(prime7Mod12 , (j / 2) + 1) ;
                
                if(powerTable7Mod12[i][j] == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < primes11Mod12.length ; k++)
                {
                    prime11Mod12 = Integer.parseInt(primes11Mod12[k]) ;
                    combinedValue = prime11Mod12 * primePower7Mod12 ;
                    firstType = Integer.parseInt(type11Mod12[k]) ;
                    if(combinedValue > TARGET || combinedValue < 0)
                    {
                        break ;
                    }
                    
                    if(firstType == 1)
                    {
                        combinedX = powerTable7Mod12[i][j] * Integer.parseInt(value11Mod12[k]) ;
                        combinedY = powerTable7Mod12[i][j + 1] * Integer.parseInt(value11Mod12[k]) ;
                    }
                    else
                    {
                        combinedX = powerTable7Mod12[i][j + 1] * Integer.parseInt(value11Mod12[k]) * 6 ;
                        combinedY = powerTable7Mod12[i][j] * Integer.parseInt(value11Mod12[k]) ;
                    }
                    
                    combinedX = adjustedMod(combinedX , 11) ;
                    combinedY = adjustedMod(combinedY , 11) ;
                    
                    combinedValues.add(combinedValue) ;
                    combinedXValues.add(combinedX) ;
                    combinedYValues.add(combinedY) ;
                }
            }
        }
        //We can also include even combinations of 7 mod 12 primes in this list.
        scanner = new Scanner(new File("combinedEvenPrimes7Mod12.csv")) ;
        String[] combined7Values = scanner.nextLine().split(",") ;
        String[] combinedEven7X = scanner.nextLine().split(",") ;
        String[] combinedEven7Y = scanner.nextLine().split(",") ;
        String[] first7Factor = scanner.nextLine().split(",") ;
        String[] second7Factor = scanner.nextLine().split(",") ;
        String[] third7Factor = scanner.nextLine().split(",") ;
        String[] fourth7Factor = scanner.nextLine().split(",") ;
        
        scanner.close() ;
        
        //Even combinations of 7 mod 12 primes with 1 11 mod 12 prime.
        for(int i = 0 ; i < combined7Values.length ; i++)
        {
            firstValue = Integer.parseInt(combined7Values[i]) ;
            xVal = Integer.parseInt(combinedEven7X[i]) ;
            yVal = Integer.parseInt(combinedEven7Y[i]) ;
            
            if(firstValue > TARGET / Integer.parseInt(primes11Mod12[0]))
            {
                continue ;
            }
            
            for(int j = 0 ; j < primes11Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes11Mod12[j]) ;
                firstCoef = Integer.parseInt(value11Mod12[j]) ;
                firstType = Integer.parseInt(type11Mod12[j]) ;
                
                if(firstValue > TARGET / firstPrime)
                {
                    break ;
                }
                
                if(firstType == 1)
                {
                    finalX = xVal * firstCoef ;
                    finalY = yVal * firstCoef ;
                }
                else
                {
                    finalX = yVal * firstCoef * 6 ;
                    finalY = xVal * firstCoef ;
                }
                
                combinedValues.add(firstPrime * firstValue) ;
                combinedXValues.add(adjustedMod(finalX , 11)) ;
                combinedYValues.add(adjustedMod(finalY , 11)) ;
            }
        }
        
        ArrayList[] even7And11Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(even7And11Content , "combinedEvenPrimePowers7Mod12andPrimes11Mod12.csv") ;
        
        //Then we can take those values and combine them with a 1 mod 12 prime or even powers of 5 mod 12.
        scanner = new Scanner(new File("combinedEvenPrimePowers7Mod12andPrimes11Mod12.csv")) ;
        String[] even7and11Value = scanner.nextLine().split(",") ;
        String[] even7and11X = scanner.nextLine().split(",") ;
        String[] even7and11Y = scanner.nextLine().split(",") ;
        
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        for(int i = 0 ; i < even7and11Value.length ; i++)
        {
            firstValue = Integer.parseInt(even7and11Value[i]) ;
            xVal = Integer.parseInt(even7and11X[i]) ;
            yVal = Integer.parseInt(even7and11Y[i]) ;
            if(firstValue < TARGET / 13)
            {
                for(int j = 0 ; j < primes1Mod12.length ; j++)
                {
                    firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                    firstCoef = Integer.parseInt(value1Mod12[j]) ;
                    firstType = Integer.parseInt(type1Mod12[j]) ;
                    
                    if(firstValue > TARGET / firstPrime)
                    {
                        break ;
                    }
                    
                    if(firstType == 1)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else
                    {
                        finalX = yVal * firstCoef * 6 ;
                        finalY = xVal * firstCoef ;
                    }
                    
                    combinedValues.add(firstValue * firstPrime) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        ArrayList[] combined7571Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(combined7571Content , "combinedEven7and11and1Mod12.csv") ;
        
        //We can also do an even power of a 7 mod 12 with a 5 and 7 combined.
        //Need to be careful to not make an odd power of a 7 mod 12 though.
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        //Even power of 7 with 5 and 7 combined
        for(int i = 0 ; i < powerTable7Mod12.length ; i++)
        {
            prime7Mod12 = Integer.parseInt(primes7Mod12[i]) ;
            
            for(int j = 2 ; j < powerTable7Mod12[0].length ; j += 4)
            {
                primePower7Mod12 = (int)Math.pow(prime7Mod12 , (j / 2) + 1) ;
                
                if(powerTable7Mod12[i][j] == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < combined5and7Value.length ; k++)
                {
                    firstValue = Integer.parseInt(combined5and7Value[k]) ;
                    combinedValue = firstValue * primePower7Mod12 ;
                    xVal = Integer.parseInt(combined5and7X[k]) ;
                    yVal = Integer.parseInt(combined5and7Y[k]) ;
                    if(primePower7Mod12 > TARGET / firstValue || firstValue % prime7Mod12 == 0)
                    {
                        continue ;
                    }
                    
                    combinedX = powerTable7Mod12[i][j] * xVal + 6 * powerTable7Mod12[i][j+1] * yVal ;
                    combinedY = powerTable7Mod12[i][j] * yVal + powerTable7Mod12[i][j+1] * xVal ;
                    
                    combinedX = adjustedMod(combinedX , 11) ;
                    combinedY = adjustedMod(combinedY , 11) ;
                    
                    combinedValues.add(combinedValue) ;
                    combinedXValues.add(combinedX) ;
                    combinedYValues.add(combinedY) ;
                }
            }
        }
        
        //We can also do even 7 * combined 7 and 5.
        for(int i = 0 ; i < combined7Values.length ; i++)
        {
            firstValue = Integer.parseInt(combined7Values[i]) ;
            firstXVal = Integer.parseInt(combinedEven7X[i]) ;
            firstYVal = Integer.parseInt(combinedEven7Y[i]) ;
            firstFactor = Integer.parseInt(first7Factor[i]) ;
            secondFactor = Integer.parseInt(second7Factor[i]) ;
            thirdFactor = Integer.parseInt(third7Factor[i]) ;
            fourthFactor = Integer.parseInt(fourth7Factor[i]) ;
            
            for(int j = 0 ; j < combined5and7Value.length ; j++)
            {
                secondValue = Integer.parseInt(combined5and7Value[j]) ;
                xVal = Integer.parseInt(combined5and7X[j]) ;
                yVal = Integer.parseInt(combined5and7Y[j]) ;
                
                if(firstValue > TARGET / secondValue)
                {
                    continue ;
                }
                
                //We need to be careful to not make a 7^2, because they are defined differently.
                if(secondValue % firstFactor == 0 || secondValue % secondFactor == 0)
                {
                    continue ;
                }
                if(thirdFactor != -1)
                {
                    if(secondValue % thirdFactor == 0 || secondValue % fourthFactor == 0)
                    {
                        continue ;
                    }
                }
                
                finalX = firstXVal * xVal + 6 * firstYVal * yVal ;
                finalY = firstXVal * yVal + firstYVal * xVal ;
                
                combinedValues.add(firstValue * secondValue) ;
                combinedXValues.add(adjustedMod(finalX , 11)) ;
                combinedYValues.add(adjustedMod(finalY , 11)) ;
            }
        }
        
        //And we can take those and add a 1 mod 12 prime.
        count = combinedValues.size() ;
        for(int i = 0 ; i < count ; i++)
        {
            firstValue = combinedValues.get(i) ;
            xVal = combinedXValues.get(i) ;
            yVal = combinedYValues.get(i) ;
            
            for(int j = 0 ; j < primes1Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                firstCoef = Integer.parseInt(value1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;
                if(firstValue > TARGET / firstPrime)
                {
                    break ;
                }
                
                intermediateValue = firstPrime * firstValue ;
                
                if(firstType == 1)
                {
                    intermedLoopX = xVal * firstCoef ;
                    intermedLoopY = yVal * firstCoef ;
                }
                else
                {
                    intermedLoopX = yVal * firstCoef * 6 ;
                    intermedLoopY = xVal * firstCoef ;
                }
                
                intermedLoopX = adjustedMod(intermedLoopX , 11) ;
                intermedLoopY = adjustedMod(intermedLoopY , 11) ;
                
                combinedValues.add(intermediateValue) ;
                combinedXValues.add(intermedLoopX) ;
                combinedYValues.add(intermedLoopY) ;
                
                for(int k = j + 1 ; k < primes1Mod12.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes1Mod12[k]) ;
                    secondCoef = Integer.parseInt(value1Mod12[k]) ;
                    secondType = Integer.parseInt(type1Mod12[k]) ;
                    if(intermediateValue > TARGET / secondPrime)
                    {
                        break ;
                    }
                    if(secondType == 1)
                    {
                        finalX = intermedLoopX * secondCoef ;
                        finalY = intermedLoopY * secondCoef ;
                    }
                    else
                    {
                        finalX = intermedLoopY * secondCoef * 6 ;
                        finalY = intermedLoopX * secondCoef ;
                    }
                    
                    combinedValues.add(intermediateValue * secondPrime) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        //Then we need to do powers of 1 mod 12 primes.
        for(int i = 0 ; i < count ; i++)
        {
            firstValue = combinedValues.get(i) ;
            xVal = combinedXValues.get(i) ;
            yVal = combinedYValues.get(i) ;
            
            for(int j = 0 ; j < powerTable1Mod12.length ; j++)
            {
                prime1Mod12 = Integer.parseInt(primes1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;

                for(int k = 0 ; k < powerTable1Mod12[0].length ; k++)
                {
                    primePower1Mod12 = (int)Math.pow(prime1Mod12 , k + 2) ;
                    firstCoef = powerTable1Mod12[j][k] ;
                    
                    if(firstCoef == -1)
                    {
                        break ;
                    }
                    if(firstValue > TARGET / primePower1Mod12)
                    {
                        break ;
                    }
                    
                    if(firstType == 1)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else if(firstType == 2 && k % 2 == 0)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else
                    {
                        finalX = yVal * firstCoef * 6 ;
                        finalY = xVal * firstCoef ;
                    }
                    
                    combinedValues.add(primePower1Mod12 * firstValue) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        ArrayList[] even7with5and7Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(even7with5and7Content , "combinedEvenPrimePower7And5and7.csv") ;
        
        // Next we can some of the previous combinations add in other 1 mod 12 values.
        scanner = new Scanner(new File("combinedEvenPrimePowers5Mod12andPrimes11Mod12.csv")) ;
        String[] combinedEven5Values = scanner.nextLine().split(",") ;
        String[] xValues = scanner.nextLine().split(",") ;
        String[] yValues = scanner.nextLine().split(",") ;
        
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        // Even 5 * 11 mod 12 prime * 1 mod 12 prime
        for(int i = 0 ; i < combinedEven5Values.length ; i++)
        {
            firstValue = Integer.parseInt(combinedEven5Values[i]) ;
            xVal = Integer.parseInt(xValues[i]) ;
            yVal = Integer.parseInt(yValues[i]) ;
            
            //The smallest possible value to factor in is 13.
            if(firstValue > TARGET / 13)
            {
                continue ;
            }
            
            for(int j = 0 ; j < primes1Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                firstCoef = Integer.parseInt(value1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;
                intermediateValue = firstValue * firstPrime ;
                
                if(intermediateValue > TARGET || intermediateValue < 0)
                {
                    break ;
                }
                
                if(firstType == 1)
                {
                    finalX = firstCoef * xVal ;
                    finalY = firstCoef * yVal ; 
                }
                else
                {
                    finalX = firstCoef * yVal * 6 ;
                    finalY = firstCoef * xVal ;
                }
                
                intermedLoopX = adjustedMod(finalX , 11) ;
                intermedLoopY = adjustedMod(finalY , 11) ;
                
                combinedValues.add(intermediateValue) ;
                combinedXValues.add(intermedLoopX) ;
                combinedYValues.add(intermedLoopY) ;
                
                //And we should be able to fit 1 more 1 mod 12.
                for(int k = j + 1 ; k < primes1Mod12.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes1Mod12[k]) ;
                    secondCoef = Integer.parseInt(value1Mod12[k]) ;
                    secondType = Integer.parseInt(type1Mod12[k]) ;

                    if(intermediateValue > TARGET / secondPrime)
                    {
                        break ;
                    }

                    if(secondType == 1)
                    {
                        finalX = secondCoef * intermedLoopX ;
                        finalY = secondCoef * intermedLoopY ; 
                    }
                    else
                    {
                        finalX = secondCoef * intermedLoopY * 6 ;
                        finalY = secondCoef * intermedLoopX ;
                    }

                    combinedValues.add(intermediateValue * secondPrime) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        //And then prime powers of 1 mod 12.
        for(int i = 0 ; i < combinedEven5Values.length ; i++)
        {
            firstValue = Integer.parseInt(combinedEven5Values[i]) ;
            xVal = Integer.parseInt(xValues[i]) ;
            yVal = Integer.parseInt(yValues[i]) ;
            
            //The smallest possible value to factor in is 13^2.
            if(firstValue > TARGET / (13 * 13))
            {
                continue ;
            }
            
            for(int j = 0 ; j < powerTable1Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;
                for(int k = 0 ; k < powerTable1Mod12[0].length ; k++)
                {
                    firstCoef = powerTable1Mod12[j][k] ;
                    if(firstCoef == -1)
                    {
                        break ;
                    }
                    primePower1Mod12 = (int)Math.pow(firstPrime , k + 2) ;
                    if(firstValue > TARGET / primePower1Mod12)
                    {
                        break ;
                    }
                    if(firstType == 1)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else if(firstType == 2 && j % 2 == 0)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else
                    {
                        finalX = yVal * firstCoef * 6 ;
                        finalY = xVal * firstCoef ;
                    }
                    
                    combinedValues.add(primePower1Mod12 * firstValue) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
            
        ArrayList[] even5And11And1Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(even5And11And1Content , "combinedEven5and11and1Mod12.csv") ;
        
        //Then do the same with the combinations with 7
        scanner = new Scanner(new File("combinedEvenPrimePowers7Mod12andPrimes11Mod12.csv")) ;
        String[] combinedEven7Values = scanner.nextLine().split(",") ;
        xValues = scanner.nextLine().split(",") ;
        yValues = scanner.nextLine().split(",") ;
        
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        //Even 7 with different 1 mod 12 primes.
        for(int i = 0 ; i < combinedEven7Values.length ; i++)
        {
            firstValue = Integer.parseInt(combinedEven7Values[i]) ;
            xVal = Integer.parseInt(xValues[i]) ;
            yVal = Integer.parseInt(yValues[i]) ;
            
            //The smallest possible value to factor in is 13.
            if(firstValue > TARGET / 13)
            {
                continue ;
            }
            
            for(int j = 0 ; j < primes1Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                firstCoef = Integer.parseInt(value1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;
                intermediateValue = firstValue * firstPrime ;
                
                if(intermediateValue > TARGET || intermediateValue < 0)
                {
                    break ;
                }
                
                if(firstType == 1)
                {
                    finalX = firstCoef * xVal ;
                    finalY = firstCoef * yVal ;
                }
                else
                {
                    finalX = firstCoef * yVal * 6 ;
                    finalY = firstCoef * xVal ;
                }
                
                intermedLoopX = adjustedMod(finalX , 11) ;
                intermedLoopY = adjustedMod(finalY , 11) ;
                
                combinedValues.add(intermediateValue) ;
                combinedXValues.add(intermedLoopX) ;
                combinedYValues.add(intermedLoopY) ;
                
                for(int k = j+1 ; k < primes1Mod12.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes1Mod12[k]) ;
                    secondCoef = Integer.parseInt(value1Mod12[k]) ;
                    secondType = Integer.parseInt(type1Mod12[k]) ;
                    combinedValue = intermediateValue * secondPrime ;

                    if(combinedValue > TARGET || combinedValue < 0)
                    {
                        break ;
                    }

                    if(secondType == 1)
                    {
                        finalX = secondCoef * intermedLoopX ;
                        finalY = secondCoef * intermedLoopY ;
                    }
                    else
                    {
                        finalX = secondCoef * intermedLoopY * 6 ;
                        finalY = secondCoef * intermedLoopX ;
                    }

                    finalX = adjustedMod(finalX , 11) ;
                    finalY = adjustedMod(finalY , 11) ;

                    combinedValues.add(combinedValue) ;
                    combinedXValues.add(finalX) ;
                    combinedYValues.add(finalY) ;
                }
            }
        }
        
        //Then even 7 times 11 mod 12 time powers of 1 mod 12 primes.
        for(int i = 0 ; i < combinedEven7Values.length ; i++)
        {
            firstValue = Integer.parseInt(combinedEven7Values[i]) ;
            xVal = Integer.parseInt(xValues[i]) ;
            yVal = Integer.parseInt(yValues[i]) ;
            
            //The smallest possible value to factor in is 13^2.
            if(firstValue > TARGET / (13 * 13))
            {
                continue ;
            }
            
            for(int j = 0 ; j < powerTable1Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;
                for(int k = 0 ; k < powerTable1Mod12[0].length ; k++)
                {
                    firstCoef = powerTable1Mod12[j][k] ;
                    if(firstCoef == -1)
                    {
                        break ;
                    }
                    primePower1Mod12 = (int)Math.pow(firstPrime , k + 2) ;
                    if(firstValue > TARGET / primePower1Mod12)
                    {
                        break ;
                    }
                    if(firstType == 1)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else if(firstType == 2 && j % 2 == 0)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else
                    {
                        finalX = yVal * firstCoef * 6 ;
                        finalY = xVal * firstCoef ;
                    }
                    
                    combinedValues.add(primePower1Mod12 * firstValue) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        ArrayList[] even7And11And1Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(even7And11And1Content , "combinedEven7and11and1Mod12.csv") ;
        
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        //Even power 7 times 11 mod 12 times even 5.
        for(int i = 0 ; i < combinedEven7Values.length ; i++)
        {
            firstValue = Integer.parseInt(combinedEven7Values[i]) ;
            firstXVal = Integer.parseInt(xValues[i]) ;
            firstYVal = Integer.parseInt(yValues[i]) ;
            
            for(int j = 0 ; j < combined5Values.length ; j++)
            {
                secondValue = Integer.parseInt(combined5Values[j]) ;
                xVal = Integer.parseInt(combinedEven5X[j]) ;
                yVal = Integer.parseInt(combinedEven5Y[j]) ;
                
                if(firstValue > TARGET / secondValue)
                {
                    continue ;
                }
                
                finalX = firstXVal * xVal + 6 * firstYVal * yVal ;
                finalY = firstXVal * yVal + firstYVal * xVal ;
                
                combinedValues.add(firstValue * secondValue) ;
                combinedXValues.add(adjustedMod(finalX , 11)) ;
                combinedYValues.add(adjustedMod(finalY , 11)) ;
            }
        }
        
        //Even power of 7 times 11 times 5^2.
        for(int i = 0 ; i < combinedEven7Values.length ; i++)
        {
            firstValue = Integer.parseInt(combinedEven7Values[i]) ;
            firstXVal = Integer.parseInt(xValues[i]) ;
            firstYVal = Integer.parseInt(yValues[i]) ;
            
            for(int j = 0 ; j < powerTable5Mod12.length ; j++)
            {
                prime5Mod12 = Integer.parseInt(primes5Mod12[j]) ;

                for(int k = 2 ; k < powerTable5Mod12[0].length ; k += 4)
                {
                    primePower5Mod12 = (int)Math.pow(prime5Mod12 , (k / 2) + 1) ;
                    xVal = powerTable5Mod12[j][k] ;
                    yVal = powerTable5Mod12[j][k+1] ;
                    
                    if(xVal == -1)
                    {
                        break ;
                    }
                
                    if(firstValue > TARGET / primePower5Mod12)
                    {
                        break ;
                    }

                    finalX = firstXVal * xVal + 6 * firstYVal * yVal ;
                    finalY = firstXVal * yVal + firstYVal * xVal ;

                    combinedValues.add(firstValue * primePower5Mod12) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        //Then take the ones we just did and add in some 1 mod 12 primes.
        
        //We're going to be adding to the same list, so we want to keep track of
        //its size at the moment, and work off that. Otherwise we will try to combine
        //into values that have already been combined into.
        count = combinedValues.size() ;
        for(int i = 0 ; i < count ; i++)
        {
            firstValue = combinedValues.get(i) ;
            xVal = combinedXValues.get(i) ;
            yVal = combinedYValues.get(i) ;
            
            for(int j = 0 ; j < primes1Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                firstCoef = Integer.parseInt(value1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;
                if(firstValue > TARGET / firstPrime)
                {
                    break ;
                }
                
                intermediateValue = firstPrime * firstValue ;
                
                if(firstType == 1)
                {
                    intermedLoopX = xVal * firstCoef ;
                    intermedLoopY = yVal * firstCoef ;
                }
                else
                {
                    intermedLoopX = yVal * firstCoef * 6 ;
                    intermedLoopY = xVal * firstCoef ;
                }
                
                intermedLoopX = adjustedMod(intermedLoopX , 11) ;
                intermedLoopY = adjustedMod(intermedLoopY , 11) ;
                
                combinedValues.add(intermediateValue) ;
                combinedXValues.add(intermedLoopX) ;
                combinedYValues.add(intermedLoopY) ;
                
                for(int k = j + 1 ; k < primes1Mod12.length ; k++)
                {
                    secondPrime = Integer.parseInt(primes1Mod12[k]) ;
                    secondCoef = Integer.parseInt(value1Mod12[k]) ;
                    secondType = Integer.parseInt(type1Mod12[k]) ;
                    if(intermediateValue > TARGET / secondPrime)
                    {
                        break ;
                    }
                    if(secondType == 1)
                    {
                        finalX = intermedLoopX * secondCoef ;
                        finalY = intermedLoopY * secondCoef ;
                    }
                    else
                    {
                        finalX = intermedLoopY * secondCoef * 6 ;
                        finalY = intermedLoopX * secondCoef ;
                    }
                    
                    combinedValues.add(intermediateValue * secondPrime) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        //Then we need to do powers of 1 mod 12 primes.
        for(int i = 0 ; i < count ; i++)
        {
            firstValue = combinedValues.get(i) ;
            xVal = combinedXValues.get(i) ;
            yVal = combinedYValues.get(i) ;
            
            for(int j = 0 ; j < powerTable1Mod12.length ; j++)
            {
                prime1Mod12 = Integer.parseInt(primes1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;

                for(int k = 0 ; k < powerTable1Mod12[0].length ; k++)
                {
                    primePower1Mod12 = (int)Math.pow(prime1Mod12 , k+2) ;
                    firstCoef = powerTable1Mod12[j][k] ;
                    
                    if(firstCoef == -1)
                    {
                        break ;
                    }
                    if(firstValue > TARGET / primePower1Mod12)
                    {
                        break ;
                    }
                    
                    if(firstType == 1)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else if(firstType == 2 && k % 2 == 0)
                    {
                        finalX = xVal * firstCoef ;
                        finalY = yVal * firstCoef ;
                    }
                    else
                    {
                        finalX = yVal * firstCoef * 6 ;
                        finalY = xVal * firstCoef ;
                    }
                    
                    combinedValues.add(primePower1Mod12 * firstValue) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        ArrayList[] combinedEven7And11AndEven5Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(combinedEven7And11AndEven5Content , "combinedEven7Even5And11.csv") ;
        
        //Next, we can take odd powers of 5 with odd powers of 7 mod 12 primes.
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        int xVal5 ;
        int yVal5 ;
        int xVal7 ;
        int yVal7 ;
        
        for(int i = 0 ; i < powerTable5Mod12.length ; i++)
        {
            prime5Mod12 = Integer.parseInt(primes5Mod12[i]) ;
            
            for(int j = 0 ; j < powerTable5Mod12[0].length ; j += 4)
            {
                primePower5Mod12 = (int)Math.pow(prime5Mod12 , (j / 2) + 1) ;
                
                if(powerTable5Mod12[i][j] == -1)
                {
                    break ;
                }
                
                xVal5 = powerTable5Mod12[i][j] ;
                yVal5 = powerTable5Mod12[i][j+1] ;
                
                for(int k = 0 ; k < powerTable7Mod12.length ; k++)
                {
                    prime7Mod12 = Integer.parseInt(primes7Mod12[k]) ;
                    
                    if(primePower5Mod12 > TARGET / prime7Mod12)
                    {
                        break ;
                    }
                    
                    for(int m = 0 ; m < powerTable7Mod12[0].length ; m += 4)
                    {
                        primePower7Mod12 = (int)Math.pow(prime7Mod12 , (m/2) + 1) ;
                        combinedValue = primePower5Mod12 * primePower7Mod12 ;
                        
                        //We already did all the combinations with 5^1 and 7^1, so they can get skipped.
                        if(j == 0 && m == 0)
                        {
                            continue ;
                        }                        
                        
                        if(combinedValue > TARGET || combinedValue < 0 || powerTable7Mod12[k][m] == -1)
                        {
                            break ;
                        }
                        
                        xVal7 = powerTable7Mod12[k][m] ;
                        yVal7 = powerTable7Mod12[k][m + 1] ;
                        
                        //Similarly to the 5 and 7 combined values, we have an additional
                        //sqrt(-23)(-2+sqrt(6)) that needs to be factored in.
                        combinedX = -2 * xVal5 * xVal7 - 12 * yVal5 * yVal7 + 6 * xVal5 * yVal7 + 6 * yVal5 * xVal7 ;
                        combinedY = -2 * xVal5 * yVal7 - 2 * yVal5 * xVal7 + xVal5 * xVal7 + 6 * yVal5 * yVal7 ;
                        
                        combinedX = adjustedMod(combinedX , 11) ;
                        combinedY = adjustedMod(combinedY , 11) ;
                        
                        combinedValues.add(combinedValue) ;
                        combinedXValues.add(combinedX) ;
                        combinedYValues.add(combinedY) ;
                    }
                }
            }
        }
        
        //Then we can combine these with the even 5 or even 7 combinations to get
        //more 11 mod 12 values. Need to be careful about creating additional powers.
        count = combinedValues.size() ;
        for(int i = 0 ; i < count ; i++)
        {
            firstValue = combinedValues.get(i) ;
            firstXVal = combinedXValues.get(i) ;
            firstYVal = combinedYValues.get(i) ;
            for(int j = 0 ; j < combined5Values.length ; j++)
            {
                secondValue = Integer.parseInt(combined5Values[j]) ;
                xVal = Integer.parseInt(combinedEven5X[j]) ;
                yVal = Integer.parseInt(combinedEven5Y[j]) ;
                firstFactor = Integer.parseInt(first5Factor[j]) ;
                secondFactor = Integer.parseInt(second5Factor[j]) ;
                thirdFactor = Integer.parseInt(third5Factor[j]) ;
                fourthFactor = Integer.parseInt(fourth5Factor[j]) ;
                
                if(firstValue % firstFactor == 0 || firstValue % secondFactor == 0)
                {
                    continue ;
                }
                if(thirdFactor != -1)
                {
                    if(firstValue % thirdFactor == 0 || firstValue % fourthFactor == 0)
                    {
                        continue ;
                    }
                }
                if(firstValue > TARGET / secondValue)
                {
                    continue ;
                }
                
                finalX = firstXVal * xVal + 6 * firstYVal * yVal ;
                finalY = firstXVal * yVal + firstYVal * xVal ;
                combinedValues.add(firstValue * secondValue) ;
                combinedXValues.add(adjustedMod(finalX , 11)) ;
                combinedYValues.add(adjustedMod(finalY , 11)) ;
            }
        }
        
        //Then do the same thing, but with the 7 values instead.
        for(int i = 0 ; i < count ; i++)
        {
            firstValue = combinedValues.get(i) ;
            firstXVal = combinedXValues.get(i) ;
            firstYVal = combinedYValues.get(i) ;
            for(int j = 0 ; j < combined7Values.length ; j++)
            {
                secondValue = Integer.parseInt(combined7Values[j]) ;
                xVal = Integer.parseInt(combinedEven7X[j]) ;
                yVal = Integer.parseInt(combinedEven7Y[j]) ;
                firstFactor = Integer.parseInt(first7Factor[j]) ;
                secondFactor = Integer.parseInt(second7Factor[j]) ;
                thirdFactor = Integer.parseInt(third7Factor[j]) ;
                fourthFactor = Integer.parseInt(fourth7Factor[j]) ;
                
                if(firstValue % firstFactor == 0 || firstValue % secondFactor == 0)
                {
                    continue ;
                }
                if(thirdFactor != -1)
                {
                    if(firstValue % thirdFactor == 0 || firstValue % fourthFactor == 0)
                    {
                        continue ;
                    }
                }
                if(firstValue > TARGET / secondValue)
                {
                    continue ;
                }
                
                finalX = firstXVal * xVal + 6 * firstYVal * yVal ;
                finalY = firstXVal * yVal + firstYVal * xVal ;
                combinedValues.add(firstValue * secondValue) ;
                combinedXValues.add(adjustedMod(finalX , 11)) ;
                combinedYValues.add(adjustedMod(finalY , 11)) ;
            }
        }
        
        ArrayList[] oddPowers5and7Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(oddPowers5and7Content , "combinedOddPrimePowers5Mod12and7Mod12.csv") ;
        
        //Then we can take all of those and add some 1 mod 12 values.
        scanner = new Scanner(new File("combinedOddPrimePowers5Mod12and7Mod12.csv")) ;
        String[] combinedOdd5and7Values = scanner.nextLine().split(",") ;
        String[] combinedOdd5and7X = scanner.nextLine().split(",") ;
        String[] combinedOdd5and7Y = scanner.nextLine().split(",") ;
        
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        for(int i = 0 ; i < combinedOdd5and7Values.length ; i++)
        {
            firstValue = Integer.parseInt(combinedOdd5and7Values[i]) ;
            xVal = Integer.parseInt(combinedOdd5and7X[i]) ;
            yVal = Integer.parseInt(combinedOdd5and7Y[i]) ;
            if(firstValue > TARGET / 13)
            {
                continue ;
            }
            for(int j = 0 ; j < primes1Mod12.length ; j++)
            {
                firstPrime = Integer.parseInt(primes1Mod12[j]) ;
                firstCoef = Integer.parseInt(value1Mod12[j]) ;
                firstType = Integer.parseInt(type1Mod12[j]) ;
                intermediateValue = firstValue * firstPrime ;
                if(intermediateValue > TARGET || intermediateValue < 0)
                {
                    break ;
                }
                
                if(firstType == 1)
                {
                    intermedLoopX = firstCoef * xVal ;
                    intermedLoopY = firstCoef * yVal ;
                }
                else
                {
                    intermedLoopX = firstCoef * yVal * 6 ;
                    intermedLoopY = firstCoef * xVal ;
                }
                
                combinedValues.add(intermediateValue) ;
                combinedXValues.add(adjustedMod(intermedLoopX , 11)) ;
                combinedYValues.add(adjustedMod(intermedLoopY , 11)) ;
                
                if(intermediateValue < TARGET / 37)
                {
                    for(int k = j + 1 ; k < primes1Mod12.length ; k++)
                    {
                        secondPrime = Integer.parseInt(primes1Mod12[k]) ;
                        secondCoef = Integer.parseInt(value1Mod12[k]) ;
                        secondType = Integer.parseInt(type1Mod12[k]) ;
                        combinedValue = firstValue * firstPrime * secondPrime ;
                        
                        if(combinedValue > TARGET || combinedValue < 0)
                        {
                            break ;
                        }
                        
                        if(secondType == 1)
                        {
                            finalX = intermedLoopX * secondCoef ;
                            finalY = intermedLoopY * secondCoef ;
                        }
                        else
                        {
                            finalX = intermedLoopY * secondCoef * 6 ;
                            finalY = intermedLoopX * secondCoef ;
                        }
                        
                        combinedValues.add(combinedValue) ;
                        combinedXValues.add(adjustedMod(finalX , 11)) ;
                        combinedYValues.add(adjustedMod(finalY , 11)) ;
                    }
                }
            }
        }
        
        //Then we can start again with powers of 1 mod 12 primes.
        for(int i = 0 ; i < powerTable1Mod12.length ; i++)
        {
            prime1Mod12 = Integer.parseInt(primes1Mod12[i]) ;
            
            for(int j = 0 ; j < powerTable1Mod12[0].length ; j++)
            {
                primePower1Mod12 = (int)Math.pow(prime1Mod12 , j+2) ;
                
                if(powerTable1Mod12[i][j] == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < combinedOdd5and7Values.length ; k++)
                {
                    firstValue = Integer.parseInt(combinedOdd5and7Values[k]) ;
                    combinedValue = firstValue * primePower1Mod12 ;
                    if(firstValue > TARGET / primePower1Mod12)
                    {
                        continue ;
                    }
                    
                    xVal = Integer.parseInt(combinedOdd5and7X[k]) ;
                    yVal = Integer.parseInt(combinedOdd5and7Y[k]) ;
                    
                    if(type1Mod12[i].equals("1"))
                    {
                        finalX = xVal * powerTable1Mod12[i][j] ;
                        finalY = yVal * powerTable1Mod12[i][j] ;
                    }
                    else
                    {
                        if(j % 2 == 0)
                        {
                            finalX = xVal * powerTable1Mod12[i][j] ;
                            finalY = yVal * powerTable1Mod12[i][j] ;
                        }
                        else
                        {
                            finalX = yVal * powerTable1Mod12[i][j] * 6 ;
                            finalY = xVal * powerTable1Mod12[i][j] ;
                        }
                    }
                    
                    combinedValues.add(combinedValue) ;
                    combinedXValues.add(adjustedMod(finalX , 11)) ;
                    combinedYValues.add(adjustedMod(finalY , 11)) ;
                }
            }
        }
        
        ArrayList[] odd5and7WithSome1Mod12Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(odd5and7WithSome1Mod12Content , "combinedOddPrimePowers5and7WithSome1Mod12.csv") ;
        
        //And we can do the same thing again with two 11 mod 12 primes, including even powers.
        ArrayList<Integer> even11Mod12Value = new ArrayList() ;
        ArrayList<Integer> even11Mod12Coef = new ArrayList() ;
        ArrayList<Integer> even11Mod12Type = new ArrayList() ;
        
        for(int i = 0 ; i < primes11Mod12.length ; i++)
        {
            firstPrime = Integer.parseInt(primes11Mod12[i]) ;
            firstCoef = Integer.parseInt(value11Mod12[i]) ;
            firstType = Integer.parseInt(type11Mod12[i]) ;
            
            for(int j = i + 1 ; j < primes11Mod12.length ; j++)
            {
                secondPrime = Integer.parseInt(primes11Mod12[j]) ;
                secondCoef = Integer.parseInt(value11Mod12[j]) ;
                secondType = Integer.parseInt(type11Mod12[j]) ;
                
                if(firstPrime > TARGET / secondPrime)
                {
                    break ;
                }
                
                intermediateValue = firstPrime * secondPrime ;
                combinedCoef = firstCoef * secondCoef ;
                switch(firstType + secondType)
                {
                    //1 and 1 gives -23
                    case 2:
                        combinedCoef *= -23 ;
                        overallType = 1 ;
                        break ;
                    //1 and 2 gives -23 * sqrt(6)
                    case 3:
                        combinedCoef *= -23 ;
                        overallType = 2 ;
                        break ;
                    // 2 and 2 gives -138
                    case 4:
                        combinedCoef *= -138 ;
                        overallType = 1 ;
                        break ;
                    default:
                        //in case something goes wrong
                        overallType = -100 ;
                }
                
                even11Mod12Value.add(intermediateValue) ;
                even11Mod12Coef.add(adjustedMod(combinedCoef , 11)) ;
                even11Mod12Type.add(overallType) ;
                
                for(int k = j + 1 ; k < primes11Mod12.length ; k++)
                {
                    thirdPrime = Integer.parseInt(primes11Mod12[k]) ;
                    thirdCoef = Integer.parseInt(value11Mod12[k]) ;
                    thirdType = Integer.parseInt(type11Mod12[k]) ;
                    
                    for(int m = k + 1 ; m < primes11Mod12.length ; m++)
                    {
                        fourthPrime = Integer.parseInt(primes11Mod12[m]) ;
                        fourthCoef = Integer.parseInt(value11Mod12[m]) ;
                        fourthType = Integer.parseInt(type11Mod12[m]) ;

                        if(intermediateValue > (TARGET / thirdPrime) / fourthPrime)
                        {
                            break ;
                        }

                        combinedValue = firstPrime * secondPrime * thirdPrime * fourthPrime ;
                        combinedCoef = firstCoef * secondCoef * thirdCoef * fourthCoef ;
                        switch(firstType + secondType + thirdType + fourthType)
                        {
                            //1, 1, 1, 1 gives -23^2 = 529
                            case 4:
                                combinedCoef *= (529 % 11) ;
                                overallType = 1 ;
                                break ;
                            //1, 1, 1, 2 gives -23 * -23 * sqrt(6) = 529 * sqrt(6)
                            case 5:
                                combinedCoef *= (529 % 11) ;
                                overallType = 2 ;
                                break ;
                            //1, 1, 2, 2 gives -23 * -138 = 3174
                            case 6:
                                combinedCoef *= (3174 % 11) ;
                                overallType = 1 ;
                                break ;
                            //1, 2, 2, 2 gives -138 * -23 * sqrt(6) = 3174 * sqrt(6)    
                            case 7:
                                combinedCoef *= (3174 % 11) ;
                                overallType = 2 ;
                                break ;
                            //2, 2, 2, 2 gives -13 * -138 = 19044
                            case 8 :
                                combinedCoef *= (19044 % 11) ;
                                overallType = 1 ;
                                break ;
                            default:
                                //in case something goes wrong
                                overallType = -100 ;
                                break ;
                        }
                        
                            even11Mod12Value.add(combinedValue) ;
                            even11Mod12Coef.add(adjustedMod(combinedCoef , 11)) ;
                            even11Mod12Type.add(overallType) ;
                    }
                }
            }
        }
        //Then we can also add the even powers of 11 mod 12 primes to this list.
        for(int i = 0 ; i < powerTable11Mod12.length ; i++)
        {
            firstPrime = Integer.parseInt(primes11Mod12[i]) ;
            for(int j = 0 ; j < powerTable11Mod12[0].length ; j+=2)
            {
                if(powerTable11Mod12[i][j] == -1)
                {
                    break ;
                }
                primePower11Mod12 = (int)Math.pow(firstPrime , j + 2) ;
                even11Mod12Value.add(primePower11Mod12) ;
                even11Mod12Coef.add(powerTable11Mod12[i][j]) ;
                //Since these are even powers they wont have any hanging sqrt(6)
                even11Mod12Type.add(1) ;
            }
        }
        
        //Now that we have all of those together, we can combine them with odd prime powers of 5 and 7 mod 12 primes
        int cutoff ;
        
        for(int i = 0 ; i < even11Mod12Value.size() ; i++)
        {
            firstValue = even11Mod12Value.get(i) ;
            firstCoef = even11Mod12Coef.get(i) ;
            firstType = even11Mod12Type.get(i) ;
            cutoff = TARGET / firstValue ;
            
            for(int j = 0 ; j < combinedOdd5and7Values.length ; j++)
            {
                secondValue = Integer.parseInt(combinedOdd5and7Values[j]) ;
                if(secondValue > cutoff)
                {
                    continue ;
                }
                
                combinedValue = firstValue * secondValue ;
                xVal = Integer.parseInt(combinedOdd5and7X[j]) ;
                yVal = Integer.parseInt(combinedOdd5and7Y[j]) ;
                
                if(firstType == 1)
                {
                    finalX = firstCoef * xVal ;
                    finalY = firstCoef * yVal ;
                }
                else
                {
                    finalX = firstCoef * yVal * 6 ;
                    finalY = firstCoef * xVal ;
                }
                
                combinedValues.add(combinedValue) ;
                combinedXValues.add(adjustedMod(finalX , 11)) ;
                combinedYValues.add(adjustedMod(finalY , 11)) ;
            }
        }
        
        ArrayList[] odd5and7WithEven11Mod12Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(odd5and7WithEven11Mod12Content , "combinedOddPrimePowers5and7WithEven11Mod12.csv") ;
        
        combinedValues.clear() ;
        combinedXValues.clear() ;
        combinedYValues.clear() ;
        
        //Next is odd 5 and 7 with a 5 or 7 squared.
        for(int i = 0 ; i < powerTable5Mod12.length ; i++)
        {
            prime5Mod12 = Integer.parseInt(primes5Mod12[i]) ;
            
            for(int j = 2 ; j < powerTable5Mod12[0].length ; j += 4)
            {
                primePower5Mod12 = (int)Math.pow(prime5Mod12 , (j / 2) + 1) ;
                firstXVal = powerTable5Mod12[i][j] ;
                firstYVal = powerTable5Mod12[i][j + 1] ;
                
                if(firstXVal == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < combinedOdd5and7Values.length ; k++)
                {
                    firstValue = Integer.parseInt(combinedOdd5and7Values[k]) ;
                    
                    if(primePower5Mod12 > TARGET / firstValue || firstValue % prime5Mod12 == 0)
                    {
                        continue ;
                    }
                    
                    xVal = Integer.parseInt(combinedOdd5and7X[k]) ;
                    yVal = Integer.parseInt(combinedOdd5and7Y[k]) ;
                    
                    combinedX = firstXVal * xVal + 6 * firstYVal * yVal ;
                    combinedY = firstXVal * yVal + firstYVal * xVal ;
                    
                    combinedX = adjustedMod(combinedX , 11) ;
                    combinedY = adjustedMod(combinedY , 11) ;
                    
                    combinedValues.add(primePower5Mod12 * firstValue) ;
                    combinedXValues.add(combinedX) ;
                    combinedYValues.add(combinedY) ;
                }
            }
        }
        
        for(int i = 0 ; i < powerTable7Mod12.length ; i++)
        {
            prime7Mod12 = Integer.parseInt(primes7Mod12[i]) ;
            
            for(int j = 2 ; j < powerTable7Mod12[0].length ; j += 4)
            {
                primePower7Mod12 = (int)Math.pow(prime7Mod12 , (j / 2) + 1) ;
                firstXVal = powerTable7Mod12[i][j] ;
                firstYVal = powerTable7Mod12[i][j + 1] ;
                
                if(firstXVal == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < combinedOdd5and7Values.length ; k++)
                {
                    firstValue = Integer.parseInt(combinedOdd5and7Values[k]) ;
                    
                    if(primePower7Mod12 > TARGET / firstValue || firstValue % prime7Mod12 == 0)
                    {
                        continue ;
                    }
                    
                    xVal = Integer.parseInt(combinedOdd5and7X[k]) ;
                    yVal = Integer.parseInt(combinedOdd5and7Y[k]) ;
                    
                    combinedX = firstXVal * xVal + 6 * firstYVal * yVal ;
                    combinedY = firstXVal * yVal + firstYVal * xVal ;
                    
                    combinedX = adjustedMod(combinedX , 11) ;
                    combinedY = adjustedMod(combinedY , 11) ;
                    
                    combinedValues.add(primePower7Mod12 * firstValue) ;
                    combinedXValues.add(combinedX) ;
                    combinedYValues.add(combinedY) ;
                }
            }
        }
        
        //The last combination of this type should be a 5^2 times 7^2 times a 5 and 7 combined
        int secondXVal ;
        int secondYVal ;
        
        for(int i = 0 ; i < powerTable7Mod12.length ; i++)
        {
            prime7Mod12 = Integer.parseInt(primes7Mod12[i]) ;
            
            for(int j = 2 ; j < powerTable7Mod12[0].length ; j += 4)
            {
                primePower7Mod12 = (int)Math.pow(prime7Mod12 , (j / 2) + 1) ;
                firstXVal = powerTable7Mod12[i][j] ;
                firstYVal = powerTable7Mod12[i][j + 1] ;
                
                if(firstXVal == -1)
                {
                    break ;
                }
                
                for(int k = 0 ; k < powerTable5Mod12.length ; k++)
                {       
                    prime5Mod12 = Integer.parseInt(primes5Mod12[k]) ;
            
                    for(int m = 2 ; m < powerTable5Mod12[0].length ; m += 4)
                    {
                        primePower5Mod12 = (int)Math.pow(prime5Mod12 , (m / 2) + 1) ;
                        secondXVal = powerTable5Mod12[k][m] ;
                        secondYVal = powerTable5Mod12[k][m + 1] ;

                        if(firstXVal == -1 || primePower7Mod12 > TARGET / primePower5Mod12)
                        {
                            break ;
                        }
                        
                        intermedLoopX = firstXVal * secondXVal + 6 * firstYVal * secondYVal ;
                        intermedLoopY = firstXVal * secondYVal + firstYVal * secondXVal ;
                        intermediateValue = primePower7Mod12 * primePower5Mod12 ;
                        
                        for(int n = 0 ; n < combined5and7Value.length ; n++)
                        {
                            firstValue = Integer.parseInt(combined5and7Value[n]) ;
                            xVal = Integer.parseInt(combined5and7X[n]) ;
                            yVal = Integer.parseInt(combined5and7Y[n]) ;
                            if(intermediateValue > TARGET / firstValue || firstValue % prime5Mod12 == 0 || firstValue % prime7Mod12 == 0)
                            {
                                continue ;
                            }
                            
                            finalX = intermedLoopX * xVal + 6 * intermedLoopY * yVal ;
                            finalY = intermedLoopX * yVal + intermedLoopY * xVal ;
                            
                            combinedValues.add(intermediateValue * firstValue) ;
                            combinedXValues.add(adjustedMod(finalX , 11)) ;
                            combinedYValues.add(adjustedMod(finalY , 11)) ;
                        } 
                    }
                }
            }
        }
        
        ArrayList[] odd5and7withEvenPower5or7Content = {combinedValues , combinedXValues , combinedYValues} ;
        writeToFile(odd5and7withEvenPower5or7Content , "combinedOdd5and7WithEvenPower5or7Mod12.csv") ;
    }
    
    /**
     * @param lines An array of array lists or other arrays.
     * @param fileName The name of the output file as a string.
     * @throws IOException 
     * 
     * Takes an array of array lists and writes them in order to the given file, 
     * separating each element by a comma and each list on a different line.
     */
    public static void writeToFile(ArrayList[] lines , String fileName) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName))) ;
        for(int i = 0 ; i < lines.length ; i++)
        {
            for(int j = 0 ; j < lines[i].size() ; j++)
            {
                writer.write(lines[i].get(j) + ",") ;
            }
            writer.write("\n");
        }
        
        writer.close() ;
    }
    
    public static void writeToFile(int[][] lines , String fileName) throws IOException
    {
        BufferedWriter writer = new BufferedWriter(new FileWriter(new File(fileName))) ;
        for(int i = 0 ; i < lines.length ; i++)
        {
            for(int j = 0 ; j < lines[i].length ; j++)
            {
                writer.write(lines[i][j] + ",") ;
            }
            writer.write("\n");
        }
        
        writer.close() ;
    }
     
    /**
     * @param n top value
     * @param k bottom value
     * @return returns -2 as an error value, otherwise 1, 0, or -1.
     * 
     * I had some issues with the original implementation of the function to calculate
     * the Legendre symbol. I re-implemented it as the Jacobi symbol, which functions the
     * same when k is an odd prime, which it will always be in the context of this project.
     */
    public static int jacobi(int n , int k)
    {
        //need k > 0 and k odd.
        if(k <= 0 && k % 2 != 1)
        {
            return -2 ;
        }
        
        n = adjustedMod(n , k) ;
        int r ;
        int temp ;
        int t = 1 ;
        
        while(n != 0)
        {
            while(n % 2 == 0)
            {
                n = n / 2 ;
                r = k % 8 ;
                if(r == 3 || r == 5)
                {
                    t = -t ;
                }
            }
            temp = n ;
            n = k ;
            k = temp ;
            
            if(adjustedMod(n , 4) == 3 && adjustedMod(k , 4) == 3)
            {
                t = -t ;
            }
            n = adjustedMod(n , k) ;
        }
        if(k == 1)
        {
            return t ;
        }
        else
        {
            return 0 ;
        }
    }
    
    /**
     * @param n top value
     * @param k bottom value
     * @return TRUE if jacobi(n,k) = 1, and FALSE otherwise.
     */
    public static boolean jacobiBool(int n , int k)
    {
        return jacobi(n , k) == 1 ;
    }
    
    /**
     * @param x
     * @param base
     * @return log_base(x)
     * 
     * Because Java's math package apparently only supports log_10 and ln.
     */
    public static double logBase(int x , int base)
    {
        return (Math.log(x) / Math.log(base)) ;
    }
    
    /**
     * @param x first variable
     * @param y second variable
     * @return The value of the chi function for x and y.
     */
    public static int chi(int x , int y)
    {
        int chiX = adjustedMod(x , 6) ;
        int chiY = adjustedMod(y , 2) ;
        
        if((chiX == 1 && chiY == 0) || (chiX == 2 && chiY == 1))
        {
            return 1 ;
        }
        else if((chiX == 5 && chiY == 0) || (chiX == 4 && chiY == 1))
        {
            return -1 ;
        }
        
        return -1 ;
    }
    
    /**
     * @param x The first value as a double
     * @param y The second value as a double
     * @return x % y, but adjusted so it always returns a positive value.
     * 
     * Java's implementation of the % operator returns the remainder with the same
     * sign as the x value. This is inconsistent with the definition of congruence
     * classes that this project uses. This is a common fix that allows the first
     * value to be negative and have the output be consistent.
     */
    public static int adjustedMod(double x , double y)
    {
        return (int)(((x % y) + y) % y) ;
    }
    
    /**
     * @param args the command line arguments
     * Does everything.
     */
    public static void main(String[] args) 
    { 
        //Unfortunately, pretty much everything has the chance to break if it is
        //unable to access the files correctly.
        try
        {
            //Generate all the primes and sort them into their groups.
            sievePrimes(TARGET) ;
            int[] primes = readPrimesFromFile() ;
            filterPrimes(primes) ;
            
            //Calculate the x and y values for the quadratic forms of each type of prime.
            solvePrimes("primes1Mod12.csv" , "solvedPrimes1Mod12.csv" , 1) ;
            solvePrimes("primes11Mod12.csv" , "solvedPrimes11Mod12.csv" , 11) ;
            solvePrimes("primes5Mod12.csv" , "solvedPrimes5Mod12.csv" , 5) ;
            solvePrimes("primes7Mod12.csv" , "solvedPrimes7Mod12.csv" , 7) ;
            
            //Use the previous x and y values to compute a(p) for each of the primes.
            calculatePrimes("solvedPrimes1Mod12.csv" , "calculatedPrimes1Mod12.csv" , 1) ;
            calculatePrimes("solvedPrimes11Mod12.csv" , "calculatedPrimes11Mod12.csv" , 11) ;
            calculatePrimes("solvedPrimes5Mod12.csv" , "calculatedPrimes5Mod12.csv" , 5) ;
            calculatePrimes("solvedPrimes7Mod12.csv" , "calculatedPrimes7Mod12.csv" , 7) ;
            
            //Use those a(p) values to find the prime powers of each prime, and some other useful
            //combinations.
            calculatePrimePowers1And11Mod12("calculatedPrimes1Mod12.csv" , "primePowers1Mod12.csv" , 1) ;
            calculatePrimePowers1And11Mod12("calculatedPrimes11Mod12.csv" , "primePowers11Mod12.csv" , 11) ;
            calculatePrimePowers5And7Mod12("calculatedPrimes5Mod12.csv" , "primePowers5Mod12.csv" , 5) ;
            calculatePrimePowers5And7Mod12("calculatedPrimes7Mod12.csv" , "primePowers7Mod12.csv" , 7) ;
            calculateEven5or7Mod12("calculatedPrimes5Mod12.csv" , "primePowers5Mod12.csv" , "combinedEvenPrimes5Mod12.csv" , 5) ;
            calculateEven5or7Mod12("calculatedPrimes7Mod12.csv" , "primePowers7Mod12.csv" , "combinedEvenPrimes7Mod12.csv" , 7) ;
            
            //Take all the primes, prime powers, and useful combinations to find the a(p) for
            //everything up to the target.
            combinePrimes1And11Mod12() ;
            combinePrimes5And7Mod12() ;
            combinePrimesAndPowers() ;
            
            //Check all of the computed a(p) values against their expected value.
            coefTest.checkCoefficients() ;
        }
        catch(IOException ioe)
        {
            //If something goes wrong reading/writing to any of the files, it should tell you the issue.
            System.out.println("Something went wrong writing the file. " + ioe) ;
        }
        
    }   
}