package pgdp.net;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class PinguDatabase {
    static Path dataFile = Path.of("db", "penguins.csv");
    public List<DatingPingu> datingPinguList;

    public PinguDatabase() {
        try {
            datingPinguList = Files.lines(dataFile).map(DatingPingu::parse).collect(Collectors.toList());
        } catch (IOException e) {
            return;
        }
    }

    public synchronized boolean add(DatingPingu datingPingu) {//append in List and CSV
        for (int i = 0; datingPinguList.size() > i; i++) {
            if (datingPinguList.get(i).getId() == datingPingu.getId()) {
                return false;
            }
        }
        datingPinguList.add(datingPingu);
        try {
            Files.write(dataFile, datingPingu.toCsvRow().getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public Optional<DatingPingu> lookupById(long id) {
        Optional<DatingPingu> opt = Optional.empty();
        opt = datingPinguList.stream().filter(x -> x.getId() == id).findFirst();
        return opt;
    }

    public List<DatingPingu> findMatchesFor(SearchRequest request) {
        System.out.println(request.getHobbies());
        return datingPinguList.stream()
                .peek(x -> System.out.println(x.getHobbies()))
                .filter(x ->
                        !Collections.disjoint(request.getHobbies(), x.getHobbies()) &&
                                x.getAge() >= request.getMinAge() &&
                                x.getAge() <= request.getMaxAge())
                .filter(y ->
                        y.getSexualOrientation().equals(request.getSexualOrientation())
                        ||  y.getSexualOrientation().equals("any"))
                .collect(Collectors.toList());
    }

}
