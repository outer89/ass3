
package it.unitn.lsde.ass3.client;

import it.unitn.lsde.ass3.service.Exception_Exception;
import it.unitn.lsde.ass3.service.Healthprofile;
import it.unitn.lsde.ass3.service.History;
import it.unitn.lsde.ass3.service.Person;
import it.unitn.lsde.ass3.service.Services;
import it.unitn.lsde.ass3.testinterface.ServiceTest;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.Service;

public class Client implements ServiceTest {

    private Services ass3;

    public static void main(String[] args) {
        Client c = new Client();
        c.executeTest();
    }

    public Client() {
        init();
    }

    public void init() {
        try {
            URL url = new URL("http://localhost:5914/ws/soap?wsdl");
            QName qname = new QName("http://service.ass3.lsde.unitn.it/", "soapService");
            Service service = Service.create(url, qname);
            ass3 = service.getPort(Services.class);

        } catch (MalformedURLException ex) {
            System.out.println("Errore url");
            ex.printStackTrace();
        }
    }

    public void executeTest() {
        int testIdPerson = 1;
        int i = createPerson();
        readPerson(testIdPerson);
        if (i > 0) {
            deletePerson(i);
        }
        updatePerson(testIdPerson);
        getHistory(testIdPerson);
        addHealthprofile(1);
        updateHealthprofile(1);

    }

    public void readPerson(int id) {
        System.out.println("Calling soap service readPerson for ID:" + id);
        try {
            Person p = ass3.readPerson(id);
            Supporter.printPerson(p);
        } catch (Exception_Exception ex) {
            System.out.println("NO SUCH PERSON");
        }

    }

    public int createPerson() {
        System.out.println("Calling soap service createPerson");
        int response = -1;
        try {
            Person p = Supporter.generateRandomPerson();
            int res = ass3.createPerson(p);
            response = res;
            System.out.println("Result: " + res);
            return res;
        } catch (Exception_Exception ex) {
            //nothing
            return response;
        }

    }

    public void deletePerson(int id) {
        System.out.println("Calling soap service deletePerson for ID:" + id);
        Holder<Integer> h = new Holder<Integer>(id);
        try {
            ass3.deletePerson(h);
            System.out.println("DELETED");
        } catch (Exception_Exception ex) {
            //nothing, this should not report exception
        }
    }

    /**
     * this function update a person. First we get the person using readPerson
     * service, then we change some person's fields in order to call
     * updatePerson. The webservice will update the person and in case some
     * heathprofile are passed in the body message it will either update the
     * healthprofile or create a new healthprofile for the person (depending on
     * the hpid). In the case a new healthprofile is created, depending on the
     * date the old one will become part of the history.
     *
     * @param id person ID
     */
    public void updatePerson(int id) {
        System.out.println("Calling soap service updatePerson for ID:" + id);
        try {
            System.out.println("1) Update person with same healthprofiles");
            Person p = ass3.readPerson(id);
            p.setName("newName");
            p.setSurname("newSurname");
            int res = ass3.updatePerson(p);
            System.out.println("Result: " + res);
            System.out.println("2) Update person with a new healthprofile");
            p.getHealthprofile().clear();
            p.getHealthprofile().add(Supporter.generateRandomHp());
            res = ass3.updatePerson(p);
            System.out.println("Result: " + res);
        } catch (Exception_Exception ex) {
            System.out.println("NO SUCH PERSON");
        }

    }

    public void getHistory(int id) {
        System.out.println("Calling soap service getHistory for ID:" + id);
        try {
            History h = ass3.getHistory(id);
            Supporter.printHistory(h);
        } catch (Exception_Exception ex) {
            //nothing
        }
    }

    /**
     * This function update an healthprofile for person ID. If the hp ID is
     * specified, if it exists the healthprofile will be updated, otherwise a
     * new healthprofile will be created. If the hp ID is not specified, a new
     * healthprofile will be created.
     *
     * @param id
     */
    public void updateHealthprofile(int id) {
        System.out.println("Calling soap service updatePersonHealthProfile");
        Healthprofile hp = null;
        try {
            Person p = ass3.readPerson(id);
            if (p.getHealthprofile() != null) {
                if (p.getHealthprofile().size() > 0) {
                    hp = (Healthprofile) p.getHealthprofile().toArray()[0];
                    int tempId = hp.getIdtabhealthprofile();
                    System.out.println("1) Update of an existing healthprofile (ID:" + tempId + ")");
                    hp = Supporter.generateRandomHp();
                    hp.setIdtabhealthprofile(tempId);
                    int res = ass3.updatePersonHealthProfile(id, hp);
                    System.out.println("Result: " + res);
                }
            }
            System.out.println("2) Update with a new healthprofile");
            hp = Supporter.generateRandomHp();
            int res = ass3.updatePersonHealthProfile(id, hp);
            System.out.println("Result: " + res);
        } catch (Exception_Exception ex) {
            System.out.println("NO SUCH PERSON");
        }

    }

    /**
     * create a new healthprofile for person ID. If no hp id is specified, a new
     * healthprofile will be created. If a hp id is specified and the id is
     * already present the function will return -1. If the hp id is specified
     * and no healthprofile is present with that id, it will add a new
     * healthprofile to person id
     */
    public void addHealthprofile(int id) {
        System.out.println("Calling soap service addPersonHealthProfile");
        try {
            Healthprofile hp = Supporter.generateRandomHp();
            int res = ass3.addPersonHealthProfile(id, hp);
            System.out.println("Result: " + res);
        } catch (Exception_Exception ex) {
            //nothing
        }
    }

}
