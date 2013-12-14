package it.unitn.lsde.ass3.dao;

import it.unitn.lsde.ass3.model.Healthprofile;
import it.unitn.lsde.ass3.model.Person;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

public class HpDao extends DaoBase {

    /**
     * create a new healthprofile for person ID. If no hp id is specified,
     * a new healthprofile will be created. If a hp id is specified and the 
     * id is already present the function will return -1. If the hp id is
     * specified and no healthprofile is present with that id, it will add a
     * new healthprofile to person id
     * @param id
     * @param hp
     * @return id of hp else -1
     */
    public int addHp(int id, Healthprofile hp) {
        Session s = getSession();
        Transaction tx = null;
        boolean save = false;
        try {
            tx = s.beginTransaction();
            if (hp.getIdtabhealthprofile() == null) {
                save = true;
            } else if (gethp(hp.getIdtabhealthprofile()) != null) {
                //do nothing, I will not update the current heathprofile
                //use updatePersonHealthProfile instead
            } else {
                save = true;
            }
            if (save == true) {
                Person p = new PersonDao().getPersonNofilter(id);
                if (p == null) {
                    return -2;
                }
                hp.setTabperson(p);
                s.save(hp);
                s.flush();
                tx.commit();
                return hp.getIdtabhealthprofile();
            } else {
                return -1;
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return -3;
        } finally {
            s.close();
        }

    }

    /**
     * this function get a healthprofile. Its used only for business logic
     * purposes.
     *
     * @param id
     * @return
     */
    private Healthprofile gethp(int id) {
        Session s = getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            Criteria c = s.createCriteria(Healthprofile.class).add(Restrictions.idEq(id));
            Healthprofile hp = (Healthprofile) c.uniqueResult();
            s.flush();
            tx.commit();
            return hp;
        } catch (Exception e) {
            return null;
        } finally {
            s.close();
        }
    }

    /**
     * This function update an healthprofile for person ID. If the hp ID is 
     * specified, if it exists the healthprofile will be updated, otherwise 
     * a new healthprofile will be created. If the hp ID is not specified, 
     * a new healthprofile will be created.
     * @param id person ID 
     * @param hp healthp profile to be updated/createed
     * @return id of hp or -1 in case of error, -2 if person does not exist
     */
    public int updateHp(int id, Healthprofile hp) {
        Session s = getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            Person p = new PersonDao().getPersonNofilter(id);
            if (p == null) {
                return -2;
            }
            if(hp==null){
                System.out.println("lol");
            }
            if (hp.getIdtabhealthprofile() != null) {
                if (gethp(hp.getIdtabhealthprofile()) == null) {
                    hp.setIdtabhealthprofile(null);
                }
            }
            hp.setTabperson(p);
            s.saveOrUpdate(hp);
            s.flush();
            tx.commit();
            return hp.getIdtabhealthprofile();
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
     * @param idPerson ID of the person
     * @return the healthprofile history for the corrisponding ID
     */
    public List getHistory(int idPerson) {
        Session s = getSession();
        Transaction tx = null;
        try {
            tx = s.beginTransaction();
            Criteria c = s.createCriteria(Healthprofile.class);
            Person p = new Person();
            p.setIdperson(idPerson);
            c.add(Restrictions.eq("tabperson", p));
            return c.list();
        } catch (HibernateException e) {
            e.printStackTrace();
            tx.rollback();
            return null;
        } finally {
            s.close();
        }

    }
}
