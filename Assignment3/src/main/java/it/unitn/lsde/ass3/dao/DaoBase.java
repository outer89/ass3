
package it.unitn.lsde.ass3.dao;

import org.hibernate.Session;

public abstract class DaoBase {

    public static Session getSession() {
        return ConnectionManager.getInstance().getSession();
    }
}
