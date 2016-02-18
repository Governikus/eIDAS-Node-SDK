package eidassaml.starterkit.person_attributes;

import eidassaml.starterkit.EidasAttribute;

/**
 * Created by Yuri Meiburg on 15-2-2016.
 */
public interface EidasPersonAttributes {
    public EidasAttribute getInstance();
    public String getName();
    public String getFriendlyName();

}
