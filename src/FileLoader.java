import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileLoader {

    private String path;

    public FileLoader(String path) {
        this.path = path;
    }

    public PerceptData getData() {
        if (!checkAccess()) return null;

        try {
            ArrayList<String> stringData = new ArrayList<>(Files.readAllLines(Path.of(path)));

            PerceptData exportData = new PerceptData();

            for (String st : stringData) {
                String[] stringAr = st.split(";");
                if (stringAr.length >= 1) {
                    String name = stringAr[stringAr.length - 1];

                    ArrayList<Double> doubles = new ArrayList<>();

                    boolean save = true;

                    for (int i = 0; i < stringAr.length - 1; i++) {
                        try {
                            doubles.add(Double.parseDouble(stringAr[i]));
                        }catch (NumberFormatException e){
                            save=false;
                        }
                    }

                    if (save) {
                        exportData.add(doubles, name);
                    }
                }

            }
            return exportData;
        } catch (IOException e) {
            return null;
        }
    }

    public boolean checkAccess() {
        return Files.isReadable(Paths.get(path));
    }
}
