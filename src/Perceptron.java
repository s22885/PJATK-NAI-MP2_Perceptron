import java.util.Arrays;

public class Perceptron {
    public enum Result {
        TRUE, FALSE, INVALID
    }

    private int wywolania=0;
    private int dimSize;
    private double[] weights;
    private double alfa;
    private String[] specs = new String[2];

    public Perceptron(int dim, double alfa) throws InvalidNumberException {
        if (dim < 2) throw new InvalidNumberException("invalid number 1 or less");

        this.dimSize = dim;
        this.alfa = alfa;

        weights = new double[dim + 1];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = Math.random() + 0.01;
        }
    }

    public int getSize() {
        return dimSize;
    }

    public Result test(double[] data, String spec) {
        if (data.length != dimSize)
            return Result.INVALID;



        double tmp = -1 * weights[weights.length - 1];
        for (int i = 0; i < dimSize; i++) {
            tmp += weights[i] * data[i];
        }

        if (tmp >= 0) {
            if (specs[0] == null)
                return Result.INVALID;

            if (spec.equals(specs[0])) return Result.TRUE;

        } else {
            if (specs[1] == null) return Result.INVALID;

            if (spec.equals(specs[1])) return Result.TRUE;

        }

        return Result.FALSE;

    }

    public void training(double[] data, String spec) {
        if (data.length != dimSize) return;


        //alloc specs
        if (specs[0] == null) {
            specs[0] = spec;
        } else if (specs[1] == null) {
            if (!specs[0].equals(spec)) {
                specs[1] = spec;
            }
        }

        //spec != specs<
        boolean contains = false;
        for (String s : specs) {
            if (s != null && s.equals(spec)) {
                contains = true;
                break;
            }
        }
        if (!contains) return;

        wywolania++;


        //weight calc
        double tmp = -1 * weights[weights.length - 1];
        for (int i = 0; i < dimSize; i++) {
            tmp += weights[i] * data[i];
        }
        //vali...
        if (tmp >= 0) {
            if (!specs[0].equals(spec)) {
                changeWeights(0, 1, data);
            }
        } else {
            if (specs[1] != null && !specs[1].equals(spec)) {
                changeWeights(1, 0, data);
            }
        }


    }

    public int getWywolania() {
        return wywolania;
    }

    private void changeWeights(double validVal, double resVal, double[] data) {
        for (int i = 0; i < weights.length - 1; i++) {
            weights[i] = weights[i] + (validVal - resVal) * alfa * data[i];
        }
        weights[weights.length - 1] = weights[weights.length - 1] + (validVal - resVal) * alfa * -1;
//        System.out.println(Arrays.toString(weights));
    }


}
