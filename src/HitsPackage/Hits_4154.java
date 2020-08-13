package HitsPackage;

import java.text.DecimalFormat;
import java.util.*;
import java.io.*;
import java.lang.*;

import static java.lang.Math.*;

public class Hits_4154 {

    private static double errorRate;
    private static int iterations;

    private static int noOfVertices;
    private static DecimalFormat df = new DecimalFormat("0.0000000");
    private static double[] prevHubValue, prevAuthValue, currHubValue, currAuthValue;

    private static MatrixProvider_4154 nodesSet;
    private static final String base = "Base";
    private static final String itr = "Itr";

    public static void main(String[] args) {

        if (args.length < 3) {
            System.out.println("Please enter valid command line argument!!!");
            return;
        }

        //Read the arguments
        String filename = args[2];

        double initialValue = 0;
        int iter;
        try {
            initialValue = Integer.parseInt(args[1]);
            iter = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            System.out.println("Number format exception in command line args");
            return;
        }

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            String ln = br.readLine();

            // read Headers
            int[] headers = extractLineValues(ln);
            br.close();

            noOfVertices = headers[0];
            int noOfEdges = headers[1];

            nodesSet = new MatrixProvider_4154(noOfVertices);

            Scanner scanner = new Scanner(new File(filename));
            scanner.nextInt();
            scanner.nextInt();

            while (scanner.hasNextInt()) {
                nodesSet.addEdge(scanner.nextInt(), scanner.nextInt());
            }
            assignIterations(iter);

            currHubValue = new double[noOfVertices];
            currAuthValue = new double[noOfVertices];
            prevHubValue = new double[noOfVertices];
            prevAuthValue = new double[noOfVertices];

            double init = 0;
            if (initialValue == 0) {
                init = 0;
            } else if (initialValue == 1) {
                init = 1;
            } else if (initialValue == -1) {
                init = 1.0 / noOfVertices;
            } else if (initialValue == -2) {
                init = 1.0 / Math.sqrt(noOfVertices);
            } else {
                System.out.println("invalid initial value");
                return;
            }

            for (int i = 0; i < noOfVertices; i++) {
                currHubValue[i] = init;
                currAuthValue[i] = init;
            }


            if (noOfVertices > 10) {
                visGreater();
            } else {
                initialisation();
                System.out.printf("%-3s : %3d", base, 0);
                 for (int i = 0; i < noOfVertices; i++) {
                    System.out.printf("   A/H[ %d]= %s/%s", i, df.format(currAuthValue[i]), df.format(currHubValue[i]));
                }

                if (iter > 0) {
                    iterGreater();
                } else {
                    iterSmall();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error in file reading");
        }
    }

    private static void printVal() {
        for (int i = 0; i < noOfVertices; i++) {
            System.out.printf("  A/H[ %d]= %s/%s", i, df.format(currAuthValue[i]), df.format(currHubValue[i]));
        }
    }

    private static void iterSmall() {
        int i = 0;
        do {
            for (int r = 0; r < noOfVertices; r++) {
                prevAuthValue[r] = currAuthValue[r];
                prevHubValue[r] = currHubValue[r];
            }

            stepAuth();
            stepHub();

            scaleAuth();
            scaleHub();

            i++;
            System.out.printf("%n%-4s : %3d ", itr, i);
            printVal();

        } while (!isConverged(currAuthValue, prevAuthValue) || !isConverged(currHubValue, prevHubValue));
        System.out.println();
    }


    private static void iterGreater() {
        for (int i = 0; i < iterations; i++) {

            stepAuth();
            stepHub();

            scaleAuth();
            scaleHub();

            System.out.printf("%n%-4s : %3d ", itr, i);
            printVal();
        }
        System.out.println();
    }

    private static void initialisation() {
        for (int i = 0; i < noOfVertices; i++) {
            prevHubValue[i] = currHubValue[i];
            prevAuthValue[i] = currAuthValue[i];
        }
    }

    private static void visGreater() {
        iterations = 0;
        double val = 1.0 / noOfVertices;
        for (int i = 0; i < noOfVertices; i++) {
            currHubValue[i] = val;
            currAuthValue[i] = val;
            prevHubValue[i] = val;
            prevAuthValue[i] = val;
        }

        int i = 0;
        do {
            for (int r = 0; r < noOfVertices; r++) {
                prevAuthValue[r] = currAuthValue[r];
                prevHubValue[r] = currHubValue[r];
            }

            stepAuth();
            stepHub();

            scaleAuth();
            scaleHub();


            i++;
        } while (!isConverged(currAuthValue, prevAuthValue) || !isConverged(currHubValue, prevHubValue));
        System.out.printf("%n%-4s : %3d ", itr, i);
        printVal();
        System.out.println();

    }

    private static void stepAuth() {
        for (int j = 0; j < noOfVertices; j++) {
            currAuthValue[j] = 0.0;
            for (int k = 0; k < noOfVertices; k++) {
                if (nodesSet.get(k, j) == 1) {
                    currAuthValue[j] += currHubValue[k];
                }
            }
        }
    }


    private static void stepHub() {
        for (int j = 0; j < noOfVertices; j++) {
            currHubValue[j] = 0.0;
            for (int k = 0; k < noOfVertices; k++) {
                if (nodesSet.get(j, k) == 1) {
                    currHubValue[j] += currAuthValue[k];
                }
            }
        }
    }

    private static void scaleAuth() {
        double authScaleFactor = 0.0;
        double authSumSqr = 0.0;
        for (int s = 0; s < noOfVertices; s++) {
            authSumSqr += currAuthValue[s] * currAuthValue[s];
        }
        authScaleFactor = Math.sqrt(authSumSqr);
        for (int s = 0; s < noOfVertices; s++) {
            currAuthValue[s] = currAuthValue[s] / authScaleFactor;
        }
    }

    private static void scaleHub() {
        double hubScaleFactor = 0.0;
        double hubSumSqr = 0.0;
        for (int s = 0; s < noOfVertices; s++) {
            hubSumSqr += currHubValue[s] * currHubValue[s];
        }
        hubScaleFactor = Math.sqrt(hubSumSqr);
        for (int s = 0; s < noOfVertices; s++) {
            currHubValue[s] = currHubValue[s] / hubScaleFactor;
        }
    }

    private static void assignIterations(int itrs) {
        errorRate = (itrs < 0) ? Math.pow(10, itrs) : Math.pow(10, -5);
        iterations = itrs;
    }

    private static int[] extractLineValues(String line) throws Exception {
        String[] row = line.split(" ");

        if (row.length != 2) {
            throw new Exception("Invalid Input found ");
        }
        int[] result = new int[2];
        result[0] = Integer.parseInt(row[0]);
        result[1] = Integer.parseInt(row[1]);

        return result;
    }

    static boolean isConverged(double[] src, double[] target) {
        for (int i = 0; i < noOfVertices; i++) {
            if (abs(src[i] - target[i]) > errorRate)
                return false;
        }
        return true;
    }
}