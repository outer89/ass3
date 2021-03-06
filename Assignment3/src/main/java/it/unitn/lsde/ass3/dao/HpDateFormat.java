

package it.unitn.lsde.ass3.dao;

import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.bind.annotation.adapters.XmlAdapter;


public class HpDateFormat extends XmlAdapter<String, Date> {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public String marshal(Date v) throws Exception {
        return dateFormat.format(v);
    }
    @Override
    public Date unmarshal(String v) throws Exception {
        return dateFormat.parse(v);
    }
    
}
