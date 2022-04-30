import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class FileExport {
    private String path;

    public FileExport(String path) {
        this.path = path;
    }
    public void write(List<String> data){
        try {
            Files.write(Paths.get(path),data);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean writeAccess() {
        return Files.isWritable(Paths.get(path));
    }

}
