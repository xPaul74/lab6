import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import org.w3c.dom.ls.LSOutput;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void scriere(List<Angajat> lista) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            File file = new File("src/main/resources/angajati.json");
            mapper.writeValue(file, lista);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static List<Angajat> citire() {
        try {
            File file = new File("src/main/resources/angajati.json");
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            List<Angajat> angajati = mapper.readValue(file, new TypeReference<List<Angajat>>() {
            });
            return angajati;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void main(String[] args) {
        List<Angajat>lista = citire();
        int opt;
        Scanner scanner = new Scanner(System.in);
        do {
            System.out.println("\nCerinta: ");
            opt = scanner.nextInt();
            switch(opt) {
                case 1:
                    lista.forEach(System.out::println);
                    break;
                case 2:
                    lista
                            .stream()
                            .filter(a->a.getSalariu() > 2500)
                            .forEach(System.out::println);
                    break;
                case 3:
                    List<Angajat>angajati_aprilie = lista
                            .stream()
                            .filter(a->a.getData_angajarii().getMonth().equals(Month.APRIL) &&
                                    a.getData_angajarii().getYear() == LocalDate.now().getYear()-1 &&
                                    (a.getPost().contains("sef") || a.getPost().contains("director")))
                            .collect(Collectors.toList());

                    angajati_aprilie.forEach(System.out::println);
                    break;
                case 4:
                    lista
                            .stream()
                            .filter(a->!(a.getPost().equals("sef") || a.getPost().equals("director")))
                            .sorted((a, b)-> Float.compare(b.getSalariu(), a.getSalariu()))
                            .forEach(System.out::println);
                    break;
                case 5:
                    var angajati = lista
                            .stream()
                            .map(Angajat::getNume)
                            .map(String::toUpperCase)
                            .toList();
                    System.out.println(angajati);
                    break;
                case 6:
                    lista
                            .stream()
                            .map(Angajat::getSalariu)
                            .filter(a->a<3000)
                            .forEach(System.out::println);
                    break;
                case 7:
                    Optional<Angajat>primulAngajat = lista
                        .stream()
                        .min((a, b)->a.getData_angajarii().compareTo(b.getData_angajarii()));
                    if(primulAngajat.isPresent())
                        System.out.println(primulAngajat.get());
                    else
                        System.out.println("Nu exista angajati");
                    break;
                case 8:
                    var dateSalarii = lista
                            .stream()
                            .map(Angajat::getSalariu)
                            .collect(Collectors.summarizingDouble(a->a));
                    System.out.println(dateSalarii.getMin() + " " + dateSalarii.getAverage() + " " + dateSalarii.getMax());
                    break;
                case 9:
                    lista
                            .stream()
                            .map(Angajat::getNume)
                            .filter(a->a.equalsIgnoreCase("ion"))
                            .findAny()
                            .ifPresentOrElse(a->System.out.println("Firma are Ioni"), ()-> System.out.println("Firma nu are niciun Ion"));
                    break;
                case 10:
                    long nrAngajati = lista
                            .stream()
                            .map(Angajat::getData_angajarii)
                            .filter(a->a.getMonth().equals(Month.JUNE) ||
                                    a.getMonth().equals(Month.JULY) ||
                                    a.getMonth().equals(Month.AUGUST))
                            .filter(a->a.getYear() == LocalDate.now().getYear() - 1)
                            .count();
                    System.out.println(nrAngajati + " angajati noi vara trecuta");
                    break;
                default:
                    break;
            }
        } while (true);

    }
}
