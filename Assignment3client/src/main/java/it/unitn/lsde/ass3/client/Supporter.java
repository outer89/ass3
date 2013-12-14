
package it.unitn.lsde.ass3.client;

import it.unitn.lsde.ass3.service.Healthprofile;
import it.unitn.lsde.ass3.service.History;
import it.unitn.lsde.ass3.service.Person;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class Supporter {

    private static final SimpleDateFormat Hpformat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final SimpleDateFormat BDformat = new SimpleDateFormat("yyyy-MM-dd");

    public static void printPerson(Person p) {
        System.out.println("Print person");
        System.out.println("ID:" + p.getIdperson());
        System.out.println("NAME:" + p.getName());
        System.out.println("SURNAME:" + p.getSurname());
        System.out.println("BIRTHDAY:" + p.getBirthday());
        if (p.getHealthprofile() != null) {
            System.out.println("Print profiles");
            for (Healthprofile hp : p.getHealthprofile()) {
                printHealthProfile(hp);
            }
        }
        System.out.println(SEPARATOR);
    }

    public static void printHealthProfile(Healthprofile hp) {
        
        System.out.println("ID:" + hp.getIdtabhealthprofile());
        System.out.println("HEIGHT:" + hp.getHeight());
        System.out.println("WEIGHT:" + hp.getWeight());
        System.out.println("STEPS:" + hp.getSteps());
        System.out.println("CALORIES:" + hp.getCalories());
        System.out.println("DATE:" + hp.getDate());
        System.out.println(SEPARATOR);
    }

    public static void printHistory(History h) {
        System.out.println("Print profiles");
        for (Healthprofile hp : h.getHealthprofile()) {
            printHealthProfile(hp);
        }
    }

    public static Person generateRandomPerson() {
        int randomName;
        int randomSurname;
        Healthprofile profile;
        Person p;
        String name;
        String surname;
        Random r = new Random();
        randomName = (int) (Math.random() * (namesList.length) + 1);
        randomSurname = (int) (Math.random() * (surnameList.length) + 1);
        name = namesList[randomName-1];
        surname =surnameList[randomSurname-1];
        profile = generateRandomHp();
        Calendar bDay = Calendar.getInstance();
        int year = Supporter.createrandomWithRange(1900, 2010);
        bDay.set(Calendar.YEAR, year);
        int day = Supporter.createrandomWithRange(1, bDay.getActualMaximum(bDay.DAY_OF_YEAR));
        bDay.set(Calendar.DAY_OF_YEAR, day);
        p = new Person();
        p.setName(name);
        p.setSurname(surname);
        p.setBirthday(BDformat.format(bDay.getTime()));
        p.getHealthprofile().add(profile);
        return p;
    }

    public static Healthprofile generateRandomHp() {
        Healthprofile hp = new Healthprofile();
        int steps;
        int calories;
        int randomWeigth;
        double randomHeigth;
        String randomValue;
        Random r = new Random();
        randomHeigth = MINHE + (r.nextDouble() * (MAXHE - MINHE));
        hp.setHeight((Math.round(randomHeigth * 1e2) / 1e2));
        randomWeigth = createrandomWithRange(MINWE, MAXWE);
        hp.setWeight(randomWeigth);
        steps = createrandomWithRange(1, 1000);
        hp.setSteps(steps);
        calories = createrandomWithRange(100, 800);
        hp.setCalories(calories);
        Calendar hpDate = Calendar.getInstance();
        int year = Supporter.createrandomWithRange(2010, 2013);
        hpDate.set(Calendar.YEAR, year);
        int day = Supporter.createrandomWithRange(1, hpDate.getActualMaximum(hpDate.DAY_OF_YEAR));
        hpDate.set(Calendar.DAY_OF_YEAR, day);
        hp.setDate(Hpformat.format(hpDate.getTime()));
        return hp;
    }

    private static int createrandomWithRange(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    /**
     * used for random generation
     */
    private static final double MINHE = 1.40;
    private static final double MAXHE = 2.10;
    private static final int MINWE = 40;
    private static final int MAXWE = 120;
    private static final String[] namesList = {"Jamila", "Lashay", "Charis", "Hank ", "Broderick", "Gennie", "Raye ", "Vivian", "Elvin ", "Nikole", "Tomoko", "Rena",
        "Ronda ", "Josphine", "Hilary ", "Yolando ", "Bertie", "Sherill", "Maren", "Kirby", "Salvatore", "Jona", "Shayna", "Chandra", "Margert", "Augustina",
        "Terrence", "Maryann", "Tori", "Aubrey", "Leia", "Zachariah", "Belia", "Wen", "Marianela", "Drusilla", "Justine", "Janina", "Kit", "Cleora", "Shalon",
        "Karry", "Babette", "Emmy", "Larue", "Hannah", "Pamila", "Nila", "Merlene ", "Johnnie"};
    private static final String[] surnameList = {"Gow", "Marlowe", "Whitehurst", "Rowen", "Husby", "Punch", "Mcintyre", "Bibeau", "Royals", "Boddie", "Carn",
        "Colman", "Metayer", "Kurth", "Newton", "Mulherin", "Hagen", "Mckain", "Mallow", "Aderholt", "Daniele", "Coldren", "Scarpa", "Thoms", "Maricle",
        "Ayers", "Ferrell", "Frisk", "Fuquay", "Mendel", "Jeffers", "Reyna", "Hertzler", "Carr", "Koster", "Hsieh", "Casale", "Buzbee", "Dunne", "Stilson",
        "Devault", "Zeiler", "Klink", "Fishman", "Klawitter", "Mcsorley", "Brumer", "Allums", "Covert", "July",};

    private static final String SEPARATOR = "**********************************";
}
