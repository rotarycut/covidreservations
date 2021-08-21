package com.sherman.covid19.reservationtool.models.external;

public class IncomingBooking {
    private String vac_centre_name;
    private String slot;
    private String vac_date;
    private String personName;

    public String getVac_centre_name() {
        return vac_centre_name;
    }

    public void setVac_centre_name(String vac_centre_name) {
        this.vac_centre_name = vac_centre_name;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getVac_date() {
        return vac_date;
    }

    public void setVac_date(String vac_date) {
        this.vac_date = vac_date;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }
}
