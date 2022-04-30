import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;

public class Core {
    private FileLoader treningData = null;
    private FileLoader testData = null;
    private Perceptron perceptron = null;
    private FileExport fileExport = null;
    private LinkedList<String> exportData = new LinkedList<>();


    public Core() {
        exportData.add("liczba bodźców trenujących;trafność");

    }

    public void start() {
        AtomicBoolean run = new AtomicBoolean(true);
        Scanner sc = new Scanner(new InputStreamReader(System.in));
        while (run.get()) {
            System.out.println("proszę podać komendę główną");
            switch (sc.nextLine()) {
                case "stop":
                case "exit":
                    run.set(false);
                    break;
                case "set":
                    set(sc, run);
                    break;
                case "load":
                    load(sc, run);
                    break;
                case "manualtest": {
                    if (perceptron == null) {
                        System.out.println("brak perceptronu");
                    } else {
                        int size = perceptron.getSize();
                        boolean test = true;
                        double[] dim = new double[size];
                        String name = "";
                        while (test) {
                            System.out.println("proszę podać komende");
                            String command = sc.nextLine();
                            switch (command) {
                                case "exit" -> test = false;
                                case "name" -> {
                                    System.out.println("prosze podać nazwę");
                                    String nametmp = sc.nextLine();
                                    name = nametmp;
                                }
                                case "info" -> {
                                    Arrays.stream(dim).forEach(e -> System.out.print(e + "; "));
                                    System.out.print(name + "\n");
                                }
                                case "set" -> {
                                    System.out.println("prosze podać miejsce zmiany od 1 do " + size);
                                    try {
                                        int place = Integer.parseInt(sc.next());
                                        if (place > 0 && place <= size) {
                                            System.out.println("proszę podać wartość");
                                            double varTmp = Double.parseDouble(sc.next());
                                            dim[place - 1] = varTmp;
                                        } else {
                                            System.out.println("błąd");
                                        }
                                    } catch (NumberFormatException e) {
                                        System.out.println("błąd");
                                    }
                                }
                                case "push" -> {
                                    switch (perceptron.test(dim,name)){
                                        case INVALID -> System.out.println("błędna nazwa");
                                        case TRUE -> System.out.println("poprawny przydział");
                                        case FALSE -> System.out.println("błędny przydział");
                                    }
                                }
                            }

                        }
                    }

                }
                break;
                case "getdata":
                    exportData.forEach(System.out::println);
                    break;
            }

        }
    }

    private void load(Scanner sc, AtomicBoolean run) {
        if (perceptron == null) {
            System.out.println("brak perceptronu");
            return;
        }
        switch (sc.nextLine()) {
            case "stop", "exit" -> run.set(false);
            case "training" -> push(sc, run, true);
            case "testing" -> push(sc, run, false);
        }
    }

    private void push(Scanner sc, AtomicBoolean run, boolean training) {
        if (training) {
            if (treningData == null) {
                System.out.println("brak danych treningowych do wczytania");
            }
        } else {
            if (testData == null) {
                System.out.println("brak danych testowych do wczytania");
            }
        }
        PerceptData loaderData = training ? treningData.getData() : testData.getData();


        if (training) {
            trainPerceptron(sc, loaderData);
        } else {
            testPerceptron(loaderData);
        }

    }

    private void testPerceptron(PerceptData loaderData) {
        int tr = 0;
        int fal = 0;
        for (int j = 0; j < loaderData.getSize(); j++) {
            switch (perceptron.test(loaderData.getDimAr(j), loaderData.getName(j))) {
                case TRUE -> tr++;
                case FALSE -> fal++;
            }
        }
        double trd = tr;
        double fald = fal;
        double sum = trd + fald;
        if (sum != 0)
            exportData.add(perceptron.getWywolania() + ";" + trd / sum);


    }

    private void trainPerceptron(Scanner sc, PerceptData loaderData) {
        System.out.println("ile razy trenować danymi perceptron podaj liczbę od 1 do 1000");
        String tmp = sc.nextLine();
        try {
            int ile = Integer.parseInt(tmp);
            if (ile > 0 && ile <= 1000) {
                for (int i = 0; i < ile; i++) {
                    for (int j = 0; j < loaderData.getSize(); j++) {
                        perceptron.training(loaderData.getDimAr(j), loaderData.getName(j));
                    }

                }
            }
        } catch (NumberFormatException e) {
            System.out.println("błędny format liczby");
        }
    }

    private void set(Scanner sc, AtomicBoolean run) {
        System.out.println("co ustawiasz?");
        switch (sc.nextLine()) {
            case "stop", "exit" -> run.set(false);
            case "training" -> {
                System.out.println("proszę podać nazwę pliku");
                String tmp = sc.nextLine();
                switch (tmp) {
                    case "stop", "exit" -> run.set(false);
                    default -> {
                        treningData = new FileLoader(tmp);
                        if (treningData.checkAccess()) {
                            System.out.println("plik treningowy załadowany poprawnie");
                        } else {
                            System.out.println("plik niepoprawny");
                            treningData = null;
                        }
                    }
                }
            }
            case "testing" -> {
                System.out.println("proszę podać nazwę pliku");
                String tmp = sc.nextLine();
                switch (tmp) {
                    case "stop", "exit" -> run.set(false);
                    default -> {
                        testData = new FileLoader(tmp);
                        if (testData.checkAccess()) {
                            System.out.println("plik testowy załadowany poprawnie");
                        } else {
                            System.out.println("plik niepoprawny");
                            testData = null;
                        }
                    }
                }
            }
            case "perceptron" -> {
                exportData.clear();
                exportData.add("liczba bodźców trenujących;trafność");
                System.out.println("proszę podać: <ilość wymiarów> <współczynnik alfa>");
                String dimNalfa = sc.nextLine();
                switch (dimNalfa) {
                    case "stop", "exit" -> run.set(false);
                    default -> {
                        String[] stAr = dimNalfa.split(" ");
                        if (stAr.length == 2) {
                            try {
                                int dim = Integer.parseInt(stAr[0]);
                                double alfa = Double.parseDouble(stAr[1]);
                                if (alfa <= 0) {
                                    System.out.println("niepoprawny format");
                                } else {
                                    try {
                                        perceptron = new Perceptron(dim, alfa);
                                    } catch (InvalidNumberException e) {
                                        System.out.println("niepoprawny format");
                                    }
                                }


                            } catch (NumberFormatException e) {
                                System.out.println("niepoprawny format");
                            }
                        } else {
                            System.out.println("niepoprawny format");
                        }
                    }
                }

            }
            case "export" -> {
                System.out.println("proszę podać nazwę pliku");
                String tmp = sc.nextLine();
                switch (tmp) {
                    case "stop", "exit" -> run.set(false);
                    default -> {
                        fileExport = new FileExport(tmp);
                        if (fileExport.writeAccess()) {
                            System.out.println("plik wyjsciowy dodany poprawnie");
                        } else {
                            System.out.println("plik niepoprawny");
                            fileExport = null;
                        }
                    }
                }
            }
        }
    }

}
