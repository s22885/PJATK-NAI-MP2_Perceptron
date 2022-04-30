import java.util.ArrayList;

public class PerceptData {
    private ArrayList<String> name;
    private ArrayList<ArrayList<Double>> dims;

    public PerceptData() {
        name = new ArrayList<>();
        dims = new ArrayList<>();
    }

    public void add(ArrayList<Double> dim, String name) {
        this.dims.add(dim);
        this.name.add(name);
    }

    public String getName(int i) {
        if (!(0 <= i & i < name.size())) {
            return null;
        }
        return name.get(i);
    }

    public ArrayList<Double> getDim(int i) {
        if (!(0 <= i & i < dims.size())) {
            return null;
        }
        return dims.get(i);


    }
    public double[] getDimAr(int i){
        if (!(0 <= i & i < dims.size())) {
            return null;
        }
        double[] res =new double[dims.get(i).size()];
        for (int j = 0; j < dims.get(i).size(); j++) {
            res[j]=dims.get(i).get(j);
        }
        return res;
    }

    public int getSize() {
        return Math.min(dims.size(), name.size());
    }
}
