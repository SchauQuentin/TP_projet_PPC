/*
 * Copyright (C) 2021 EA 6300 ERL CNRS 7002
 * Laboratoire d'Informatique Fondamentale et Appliquée de Tours
 * Équipe Recherche Opérationnelle, Ordonnancement et Transport
 *
 * This file is part of 1rjLmax.
 *
 * 1rjLmax is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * 1rjLmax is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with 1rjLmax. If not, see <https://www.gnu.org/licenses/>.
 *
 */

// update this line according to your project architecture
import java.io.File;
import java.io.PrintWriter;
import java.util.Random;
import java.util.Scanner;

/**
 * The Instance class represents an instance of the 1|rj|Lmax problem.
 * @author  Ronan Bocquillon
 * @version 1.0 Nov. 2021
 */
public class Instance {

    // release dates
    private final int[] r;

    // processing times
    private final int[] p;

    // due dates
    private final int[] dd;

    // sum p[i]
    private final int P;

    // max r[i]
    private final int R;

    /**
     * Generates an instance of n tasks.
     * @param  n the size of the instance to be generated.
     * @param  K the parameter controlling the range of release times and due dates.
     * @throws IllegalArgumentException if n or K is null or negative.
     * See [Y. Pan, L. Shi, Branch-and-bound algorithms for solving hard instances of
     * the one-machine sequencing problem, European J. Oper. Res. 168 (2006) 1030–1039]
     * for more details.
     */
    public Instance (int n, double K) throws IllegalArgumentException {
        if (n <= 0) {
            throw new IllegalArgumentException("n: Is negative");
        }
        if (K <= 0) {
            throw new IllegalArgumentException("K: Is negative");
        }
        Random random = new Random();
        final int rbound = (int)Math.ceil(K*n);
        r  = new int[n];
        p  = new int[n];
        dd = new int[n];
        int _P = 0;
        int _R = 0;
        for (int i = 0; i < n; ++i) {
            r[i]  =     random.nextInt(rbound+1); // [0,nK]
            p[i]  = 1 + random.nextInt(  50    ); // [1,50]
            _P   += p[i];                         // sum p[i]
            _R    = Math.max(_R, r[i]);           // max r[i]
        }
        P = _P;
        R = _R;
        final int lbd = (int)Math.floor(-K*n+P);
        for (int i = 0; i < n; ++i) {
            // [P-nK,P]
            dd[i] = lbd + random.nextInt(P-lbd+1);
        }
    }

    /**
     * Reads an instance from a text file.
     * @param  filename the path of the text file to be read.
     * @throws IllegalArgumentException if the file does not exist or is not correctly formatted.
     */
    public Instance (String filename) throws IllegalArgumentException {
        try {
            try (Scanner sc = new Scanner(new File(filename))) {
                final int n = Integer.parseInt(sc.nextLine());
                if (n <= 0) {
                    throw new IllegalArgumentException("n: Is negative");
                }
                r  = new int[n];
                p  = new int[n];
                dd = new int[n];
                int _P = 0;
                int _R = 0;
                for (int i = 0; i < n; ++i) {
                    String[] job = sc.nextLine().split(" ");
                    r [i] = Integer.parseInt(job[0]);
                    p [i] = Integer.parseInt(job[1]);
                    dd[i] = Integer.parseInt(job[2]);
                    _P   += p[i];
                    _R    = Math.max(_R,r[i]);
                }
                P = _P;
                R = _R;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(filename + ": Bad format");
        }
    }

    /**
     * Writes this instance to a text file.
     * @param  filename the path where the file will be written.
     * @param  dat indicates whether the file has to be formatted using the text
     *           format (false) or the OPL data format (true).
     * @throws IllegalArgumentException if the file cannot be created, opened or written to.
     */
    public void write (String filename, boolean dat) throws IllegalArgumentException {
        try {
            try (PrintWriter printwriter = new PrintWriter(new File(filename))) {
                final int n = getN();
                // dat
                if (dat) {
                    printwriter.printf("nbJobs = %d;", n);
                    printwriter.println();
                    printwriter.println("ri = [");
                    for (int i = 0; i < n; ++i) {
                        printwriter.println(  r[i] + ( i < n-1 ? "," : "" ) );
                    }
                    printwriter.println("];");
                    printwriter.println("pi = [");
                    for (int i = 0; i < n; ++i) {
                        printwriter.println(  p[i] + ( i < n-1 ? "," : "" ) );
                    }
                    printwriter.println("];");
                    printwriter.println("ddi = [");
                    for (int i = 0; i < n; ++i) {
                        printwriter.println( dd[i] + ( i < n-1 ? "," : "" ) );
                    }
                    printwriter.println("];");
                }
                // text
                else {
                    printwriter.println(n);
                    for (int i = 0; i < n; ++i) {
                        printwriter.printf("%d %d %d", r[i], p[i], dd[i]);
                        printwriter.println();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(filename + ": Bad format");
        }
    }

    /**
     * Writes this instance to a file.
     * @param  filename the path where the file will be written.
     * @throws IllegalArgumentException if the file cannot be created, opened or written to.
     */
    public void write (String filename) throws IllegalArgumentException {
        write(filename, false);
    }

    /**
     * Prints this instance.
     */
    public void println () {
        System.out.println("Instance -----------------------------------------");
        for (int i = 0, n = getN(); i < n; ++i) {
            System.out.printf("| r_%-4d = %-4d | p_%-4d = %-4d | dd_%-4d = %-4d |", i, r[i], i, p[i], i, dd[i]);
            System.out.println();
        }
        System.out.println("--------------------------------------------------");
    }

    /**
     * Returns the number of tasks.
     * @return the number of tasks.
     */
    public int getN () {
        return r.length;
    }

    /**
     * Returns the release date of task i.
     * @param  i the task id.
     * @return the release date of task i.
     * @throws IndexOutOfBoundsException if i is negative, greater than or equal to n.
     */
    public int getR (int i) throws IndexOutOfBoundsException {
        return r[i];
    }

    /**
     * Returns the processing time of task i.
     * @param  i the task id.
     * @return the processing time of task i.
     * @throws IndexOutOfBoundsException if i is negative, greater than or equal to n.
     */
    public int getP (int i) {
        return p[i];
    }

    /**
     * Returns the due date of task i.
     * @param  i the task id.
     * @return the due date of task i.
     * @throws IndexOutOfBoundsException if i is negative, greater than or equal to n.
     */
    public int getDD (int i) {
        return dd[i];
    }

    /**
     * Returns the sum of the processing times.
     * @return the sum of the processing times.
     */
    public int sumP () {
        return P;
    }

    /**
     * Returns the maximum of the release dates.
     * @return the maximum of the release dates.
     */
    public int maxR () {
        return R;
    }

}