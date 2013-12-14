package it.unitn.lsde.ass3.dao;

import it.unitn.lsde.ass3.model.Healthprofile;
import it.unitn.lsde.ass3.model.Person;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

public class PersonDao extends DaoBase {

    /**
     * This function search in the database a person and return it. The query
     * will be done using a filter thanks to which we are able to get the person
     * and only his last healthprofile (based on the date field)
     *
     * @param id ID of the person
     * @return a person corrisponding to the id
     */
    public Person getPerson(int id) {
        Person res = null;
        Session s = getSession();
        Transaction tx = null;
        try {

            tx = s.beginTransaction();
            s.enableFilter("datefilter").setParameter("idtemp", id);
            Criteria c = s.createCriteria(Person.class);
            c.add(Restrictions.idEq(id));
            res = (Person) c.uniqueResult();
            tx.commit();
            s.close();
            return res;
        } catch (HibernateException e) {
            //  tx.rollback();
            Logger.getLogger(PersonDao.class.getName()).log(Level.SEVERE, null, e);
            return null;
        }

    }

    /**
     * this function update a person The webservice will update the person and
     * in case some heathprofile are passed in the body message it will either
     * update the healthprofile or create a new healthprofile for the person
     * (depending on the hpid). In the case a new healthprofile is created,
     * depending on the date the old one will become part of the history.
     *
     * @param p person ID to be updated
     * @return id of the person updated or -1 in case of database exceptions
     */
    public Integer updatePerson(Person p) {
        Session s = getSession();
        Transaction tx = null;
        if (p.getTabhealthprofiles() != null) {
            Healthprofile hp;
            for (Object object : p.getTabhealthprofiles()) {
                hp = (Healthprofile) object;
                hp.setTabperson(p);
            }
        }
        try {
            tx = s.beginTransaction();
            s.update(p);
            s.flush();
            tx.commit();
            return p.getIdperson();
        } catch (HibernateException e) {
            e.printStackTrace();
            tx.rollback();
            return -1;
        } finally {
            s.close();
        }
    }

    /**
     *
     * @param id of the person to be deleted
     * @return -1 if person does not exist, -2 if database exceptions, 0 if
     * successfully deleted the person
     */
    public Integer deletePerson(Integer id) {
        Session s = getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            Person p = getPerson(id);
            if (p == null) {
                return -1;
            }
            s.delete(p);
            s.flush();
            tx.commit();
            return 0;
        } catch (HibernateException e) {
            tx.rollback();
            return -2;
        } finally {
            s.close();
        }
    }

    /**
     * Id of person is overwritten and returned if person can be created We add
     * the possibility to passing together with the person an associated
     * healthprofile (only one).
     *
     * @param p
     * @return id of the person created, -1 in case of database problems
     */
    public Integer createPerson(Person p) {
        Session s = getSession();
        Transaction t = null;
        try {
            t = s.beginTransaction();
            if (p.getTabhealthprofiles() != null) {
                if (p.getTabhealthprofiles().size() == 1) {
                    Healthprofile hp = (Healthprofile) p.getTabhealthprofiles().toArray()[0];
                    hp.setTabperson(p);
                    p.getTabhealthprofiles().clear();
                    p.getTabhealthprofiles().add(hp);

                }
            }
            s.save(p);
            s.flush();
            t.commit();
            System.out.println(p.getIdperson());
            return p.getIdperson();
        } catch (HibernateException e) {
            e.printStackTrace();
            t.rollback();
            return -1;
        } finally {
            s.close();
        }
    }

    /**
     * this function is used to return a person without eventually healthprofile
     * associated. It's used only for logic purposes.
     *
     * @param id
     * @return
     */
    protected Person getPersonNofilter(int id) {
        Person res = null;
        Session s = getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            Criteria c = s.createCriteria(Person.class);
            c.add(Restrictions.idEq(id));
            res = (Person) c.uniqueResult();
            s.close();
            tx.commit();
        } catch (HibernateException e) {
            tx.rollback();
            e.printStackTrace();
            res = null;
        } finally {
            return res;
        }

    }
}
