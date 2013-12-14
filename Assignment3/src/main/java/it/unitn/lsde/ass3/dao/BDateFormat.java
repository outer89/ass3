/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package it.unitn.lsde.ass3.dao;

/**
 *
 * @author Massimiliano
 */
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

public class BDateFormat extends XmlAdapter<String, Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }
    @Override
    public Date unmarshal(String v) throws Exception {
        return dateFormat.parse(v);
    }

}