package de.undefinedhuman.projectcreate.core.noise;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;

public class Test {

    private static final int WIDTH = 1000;
    private static final int HEIGHT = 400;

    public static void main(String[] args) {

        System.out.println(Arrays.toString(Solution.solution(new int[][]{{0, 1, 0, 0, 0, 1}, {4, 0, 0, 3, 2, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}, {0, 0, 0, 0, 0, 0}})));
        System.out.println(Arrays.toString(Solution.solution(new int[][] {{0, 2, 1, 0, 0}, {0, 0, 0, 3, 4}, {0, 0, 0, 0, 0}, {0, 0, 0, 0,0}, {0, 0, 0, 0, 0}})));

        System.out.println(Arrays.toString(Solution.solution(new int[][] {
                {1, 2, 3, 0, 0, 0},
                {4, 5, 6, 0, 0, 0},
                {7, 8, 9, 1, 0, 0},
                {0, 0, 0, 0, 1, 2},
                {0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0}
        })));
        // 1 2 3

        System.out.println(Arrays.toString(Solution.solution(new int[][] {
                {0, 0, 12, 0, 15, 0, 0, 0, 1, 8},
                {0, 0, 60, 0, 0, 7, 13, 0, 0, 0},
                {0, 15, 0, 8, 7, 0, 0, 1, 9, 0},
                {23, 0, 0, 0, 0, 1, 0, 0, 0, 0},
                {37, 35, 0, 0, 0, 0, 3, 21, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        })));
        // [1, 2, 3, 4, 5, 15]

        System.out.println(Arrays.toString(Solution.solution(new int[][]{{0}})));

        System.out.println(Arrays.toString(Solution.solution(new int[][] {
                {0, 7, 0, 17, 0, 1, 0, 5, 0, 2},
                {0, 0, 29, 0, 28, 0, 3, 0, 16, 0},
                {0, 3, 0, 0, 0, 1, 0, 0, 0, 0},
                {48, 0, 3, 0, 0, 0, 17, 0, 0, 0},
                {0, 6, 0, 0, 0, 1, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        })));
    // [4, 5, 5, 4, 2, 20]

        System.out.println(Arrays.toString(Solution.solution(new int[][] {
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        })));
        // [1, 1, 1, 1, 1, 5]

        System.out.println(Arrays.toString(Solution.solution(new int[][] {
                {1, 1, 1, 0, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 1, 1, 0, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 1, 1, 0, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 1, 1, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {1, 0, 1, 0, 1, 0, 1, 0, 1, 1},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        })));
        // [2, 1, 1, 1, 1, 6]

        System.out.println(Arrays.toString(Solution.solution(new int[][] {
                {0, 86, 61, 189, 0, 18, 12, 33, 66, 39},
                {0, 0, 2, 0, 0, 1, 0, 0, 0, 0},
                {15, 187, 0, 0, 18, 23, 0, 0, 0, 0},
                {1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        })));
        // [6, 44, 4, 11, 22, 13, 100]

        System.out.println(Arrays.toString(Solution.solution(new int[][] {
                {0, 0, 0, 0, 3, 5, 0, 0, 0, 2},
                {0, 0, 4, 0, 0, 0, 1, 0, 0, 0},
                {0, 0, 0, 4, 4, 0, 0, 0, 1, 1},
                {13, 0, 0, 0, 0, 0, 2, 0, 0, 0},
                {0, 1, 8, 7, 0, 0, 0, 1, 3, 0},
                {1, 7, 0, 0, 0, 0, 0, 2, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
                {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
        })));
    // [1, 1, 1, 2, 5]

        /*float[][] blockData = new float[WIDTH][HEIGHT];
        // FastNoise noise = new FastNoise(1337);
        /*noise.SetNoiseType(FastNoise.NoiseType.ValueCubic);
        noise.SetFractalType(FastNoise.FractalType.FBm);
        noise.SetFractalOctaves(6);
        noise.SetFractalLacunarity(2f);
        noise.SetFractalGain(0.5f);

        OpenSimplex2F openSimplex = new OpenSimplex2F(1337);

        Noise noise = new Noise(openSimplex::noise2);
        noise.setFrequency(0.1f);
        noise.setOctaves(1);
        noise.setLacunarity(2f);
        noise.setGain(0.01f);

        long startTime = System.currentTimeMillis();

        for(int i = 0; i < WIDTH; i++) {
            for(int j = 0; j < HEIGHT; j++) {
                // blockData[i][j] = noise.select(0.6f, noise.gradient(i, j, HEIGHT, 0)) ? 1f : 0f;
            }
        }

        System.out.println(System.currentTimeMillis() - startTime);

        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
        for(int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                float data = Tools.clamp(blockData[i][j], 0, 1f);
                image.setRGB(i, j, new Color(data, data, data).getRGB());
            }
        }

        try {
            ImageIO.write(image, "png", new File("noise.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    private static int fib(int max) {
        int f1 = 0, f2 = 1, temp = 0;
        int s = 2;
        int count = 2;

        while(s < max) {
            temp = f1 + f2;
            f1 = f2;
            f2 = temp;
            if(temp + s > max)
                return count;
            s += temp;
            count++;
        }
        return count;
    }

    public static int solution(String x) {
        CACHE.put(BigInteger.ONE, 0);
        return recursion(new BigInteger(x));
    }

    private static BigInteger TWO = new BigInteger("2");
    private static HashMap<BigInteger, Integer> CACHE = new HashMap<>();

    private static int recursion(BigInteger n) {
        if(CACHE.containsKey(n))
            return CACHE.get(n);
        int solution = 0;
        if(n.mod(TWO).equals(BigInteger.ZERO))
            solution = recursion(n.divide(TWO)) + 1;
        else
            solution = Math.min(recursion(n.add(BigInteger.ONE).divide(TWO)) + 2, recursion(n.subtract(BigInteger.ONE).divide(TWO)) + 2);
        CACHE.put(n, solution);
        return solution;
    }

}

class Solution {
    public static int[] solution(int[][] m) {
        if(m.length == 1)
            return new int[] { 1, 1 };

        int[] sums = calculateSums(m);
        rearrangeStates(m, sums);

        Matrix matrix = new Matrix(m.length, m.length);
        for(int i = 0; i < m.length; i++)
            for(int j = 0; j < m.length; j++)
                if(m[i][j] > 0)
                    matrix.set(i, j, m[i][j], sums[i]);

        int nonTerminalStates = 0;
        for (int sum : sums)
            if (sum != 0)
                nonTerminalStates++;

        Matrix identity = Matrix.identity(nonTerminalStates, nonTerminalStates);

        Matrix Q = matrix.subMatrix(0, nonTerminalStates-1, 0, nonTerminalStates-1);
        Matrix IQ = identity.subtract(Q);
        Matrix N = IQ.inverse();
        Matrix R = matrix.subMatrix(0, nonTerminalStates-1, nonTerminalStates, matrix.getCols()-1);
        Matrix NR = N.multiply(R);

        return convertToOutput(NR.getRow(0));
    }

    private static int[] calculateSums(int[][] matrix) {
        int[] sums = new int[matrix.length];
        for(int i = 0; i < matrix.length; i++) {
            sums[i] = 0;
            for(int j = 0; j < matrix.length; j++)
                sums[i] += matrix[i][j];
        }
        return sums;
    }

    private static void rearrangeStates(int[][] matrix, int[] sums) {
        for(int i = 0; i < matrix.length; i++) {
            for(int j = 0; j < matrix.length - i - 1; j++) {
                if(sums[j] == 0 && sums[j+1] != 0) {
                    sums[j] = sums[j+1];
                    sums[j+1] = 0;
                    swapRows(matrix, j, j+1);
                    swapCols(matrix, j, j+1);
                }
            }
        }
    }

    private static void swapRows(int[][] matrix, int row1, int row2) {
        for(int col = 0; col < matrix.length; col++) {
            int temp = matrix[row1][col];
            matrix[row1][col] = matrix[row2][col];
            matrix[row2][col] = temp;
        }
    }

    private static void swapCols(int[][] matrix, int col1, int col2) {
        for(int row = 0; row < matrix.length; row++) {
            int temp = matrix[row][col1];
            matrix[row][col1] = matrix[row][col2];
            matrix[row][col2] = temp;
        }
    }

    private static int[] convertToOutput(Fraction[] fractions) {
        long currentLCM = 0;
        for (Fraction fraction : fractions) {
            if (fraction.getDenominator() == 0)
                continue;
            if (currentLCM == 0)
                currentLCM = fraction.getDenominator();
            else currentLCM = Utils.findLCM(fraction.getDenominator(), currentLCM);
        }
        int[] solution = new int[fractions.length+1];
        for(int i = 0; i < fractions.length; i++)
            solution[i] = (int) (fractions[i].getDenominator() != 0 ? (fractions[i].getNumerator()) * (currentLCM / fractions[i].getDenominator()) : 0);
        solution[solution.length-1] = (int) currentLCM;
        return solution;
    }

}

class Utils {
    public static long findLCM(long a, long b) {
        return (a * b) / findGCF(a, b);
    }

    public static long findGCF(long a, long b) {
        return b == 0 ? a : findGCF(b, a % b);
    }
}

class Matrix {

    private Fraction[][] data;
    private int rows, cols;

    public Matrix(int rows, int cols) {
        this.data = new Fraction[rows][cols];
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                data[i][j] = new Fraction(0, 0);
        this.rows = rows;
        this.cols = cols;
    }

    public void set(int row, int col, Fraction fraction) {
        this.data[row][col].set(fraction.getNumerator(), fraction.getDenominator());
    }

    public void set(int row, int col, int numerator, int denominator) {
        this.data[row][col].set(numerator, denominator);
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Fraction get(int row, int col){
        return data[row][col];
    }

    public Matrix multiply(Matrix other) {
        Matrix product = new Matrix(other.rows, other.cols);
        for(int i = 0; i < product.rows; i++)
            for(int j = 0; j < product.cols; j++) {
                Fraction sum = new Fraction();
                for(int k = 0; k < cols; k++)
                    sum.add(new Fraction(get(i, k)).multiply(other.get(k, j)));
                product.set(i, j, sum.simplify());
            }
        return product;
    }

    // inclusive
    public Matrix subMatrix(int startRow, int endRow, int startCol, int endCol) {
        Matrix subMatrix = new Matrix(endRow-startRow+1, endCol-startCol+1);
        for(int i = startRow; i <= endRow; i++)
            for(int j = startCol; j <= endCol; j++) {
                subMatrix.set(i - startRow, j - startCol, data[i][j]);
            }
        return subMatrix;
    }

    public Matrix subtract(Matrix matrix) {
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                data[i][j].sub(matrix.get(i, j));
        return this;
    }

    public void simplify() {
        for(int i = 0; i < rows; i++)
            for(int j = 0; j < cols; j++)
                data[i][j].simplify();
    }

    public Fraction[] getRow(int index) {
        return data[index];
    }

    public static Matrix identity(int rows, int cols) {
        Matrix matrix = new Matrix(rows, cols);
        for(int i = 0; i < rows; i++)
            matrix.set(i, i, 1, 1);
        return matrix;
    }

    public Matrix inverse() {
        Matrix augmentedMatrix = new Matrix(rows, cols*2);
        for(int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++)
                augmentedMatrix.set(i, j, data[i][j]);
            augmentedMatrix.set(i, data.length + i, 1, 1);
        }
        augmentedMatrix.simplify();

        for(int col = 0; col < cols; col++) {
            for(int i = col+1; i < rows; i++) {
                if(augmentedMatrix.get(col, col).equals(new Fraction(1, 1)))
                    break;
                if(augmentedMatrix.get(i, col).compareTo(augmentedMatrix.get(col, col)) > 0)
                    augmentedMatrix.swapRows(col, i);
            }
            augmentedMatrix.simplify();
            Fraction pivot = augmentedMatrix.get(col, col);
            if(!pivot.simplify().equals(new Fraction(1, 1)))
                augmentedMatrix.multiplyRow(col, pivot.getDenominator(), pivot.getNumerator());
            augmentedMatrix.simplify();
            for(int i = 0; i < rows; i++) {
                if(i == col)
                    continue;
                augmentedMatrix.addToRow(col, i, augmentedMatrix.get(i, col).getNumerator(), augmentedMatrix.get(i, col).getDenominator());
            }
            augmentedMatrix.simplify();
        }

        return augmentedMatrix.subMatrix(0, rows-1, cols, augmentedMatrix.getCols()-1);
    }

    public void swapRows(int row1, int row2) {
        Fraction temp = new Fraction(0, 0);
        for(int i = 0; i < cols; i++) {
            temp.set(data[row1][i]);
            data[row1][i].set(data[row2][i]);
            data[row2][i].set(temp);
        }
    }

    private void multiplyRow(int row, long numerator, long denominator) {
        for(int i = 0; i < cols; i++) {
            data[row][i].multiply(numerator, denominator);
        }
    }

    private void addToRow(int row1, int row2, long numerator, long denominator) {
        for(int i = 0; i < cols; i++) {
            data[row2][i].add(-1 * (data[row1][i].getNumerator() * numerator), (data[row1][i].getDenominator() * denominator));
        }
    }
}

class Fraction {

    private long numerator, denominator;

    public Fraction() {
        this(0, 0);
    }

    public Fraction(Fraction fraction) {
        this(fraction.numerator, fraction.denominator);
    }

    public Fraction(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
        simplify();
    }

    public void set(Fraction fraction) {
        set(fraction.getNumerator(), fraction.getDenominator());
    }

    public void set(long numerator, long denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    public long getNumerator() {
        return numerator;
    }

    public long getDenominator() {
        return denominator;
    }

    public Fraction multiply(Fraction other) {
        return this.multiply(other.getNumerator(), other.getDenominator());
    }

    public Fraction multiply(long otherNumerator, long otherDenominator) {
        numerator *= otherNumerator;
        denominator *= otherDenominator;
        simplify();
        return this;
    }

    public Fraction add(Fraction fraction) {
        return this.add(fraction.getNumerator(), fraction.getDenominator());
    }

    public Fraction add(long otherNumerator, long otherDenominator) {
        if(otherDenominator == 0 || otherNumerator == 0) return this;
        if(denominator == 0 || numerator == 0) {
            set(otherNumerator, otherDenominator);
            return this;
        }
        long lcd = Utils.findLCM(denominator, otherDenominator);
        set((numerator)*(lcd/denominator) + (otherNumerator)*(lcd/otherDenominator), lcd);
        simplify();
        return this;
    }

    public Fraction sub(Fraction fraction) {
        return this.sub(fraction.numerator, fraction.denominator);
    }

    public Fraction sub(long otherNumerator, long otherDenominator) {
        if(otherDenominator == 0 || otherNumerator == 0)
            return this;
        if(denominator == 0 || numerator == 0) {
            set(-otherNumerator, otherDenominator);
            return this;
        }
        long lcd = Utils.findLCM(denominator, otherDenominator);
        set((numerator)*(lcd/denominator) - (otherNumerator)*(lcd/otherDenominator), lcd);
        simplify();
        return this;
    }

    public long compareTo(Fraction fraction) {
        return compareTo(fraction.numerator, fraction.denominator);
    }

    public int compareTo(long otherNumerator, long otherDenominator) {
        if (denominator == 0 || numerator == 0) return -1;
        if (otherDenominator == 0 || otherNumerator == 0) return 1;
        long lcd = Utils.findLCM(denominator, otherDenominator);
        long x = numerator * (lcd / denominator);
        long y = otherNumerator * (lcd / otherDenominator);
        return Long.compare(x, y);
    }

    public Fraction simplify() {
        if(numerator == 0 || denominator == 0) {
            set(0, 0);
            return this;
        }
        boolean isNegative = (numerator < 0) != (denominator < 0);
        long gcf = Utils.findGCF(numerator = Math.abs(numerator), denominator = Math.abs(denominator));
        numerator /= gcf;
        denominator /= gcf;
        if(isNegative)
            numerator *= -1;
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fraction fraction = (Fraction) o;
        return numerator == fraction.numerator && denominator == fraction.denominator;
    }
}

class Version {

    private int[] data;

    public Version(String data) {
        String[] stringData = data.split("\\.");
        this.data = new int[stringData.length];
        for(int i = 0; i < stringData.length; i++) {
            this.data[i] = Integer.parseInt(stringData[i]);
        }
    }

    public int[] getData() {
        return data;
    }

    @Override
    public String toString() {
        if(data.length == 0)
            return "";
        String s = String.valueOf(data[0]);
        for(int i = 1; i < data.length; i++) {
            s += "." + data[i];
        }
        return s;
    }

    public int compareTo(Version other) {
        if(other == null) {
            return 1;
        }
        int length = Math.max(data.length, other.getData().length);
        for(int i = 0; i < length; i++) {
            int data = i < this.data.length ? this.data[i] : -1;
            int otherData = i < other.getData().length ? other.getData()[i] : -1;
            if(data < otherData) {
                return -1;
            }
            if(data > otherData) {
                return 1;
            }
        }
        return 0;
    }

}
