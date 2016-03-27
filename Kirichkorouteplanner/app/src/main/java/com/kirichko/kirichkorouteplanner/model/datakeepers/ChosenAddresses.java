package com.kirichko.kirichkorouteplanner.model.datakeepers;


import android.location.Address;

/**
 * Created by Киричко on 26.03.2016.
 */
public class ChosenAddresses {
    private static Address addressA;
    private static Address addressB;

    public ChosenAddresses(Address addressA, Address addressB) {
        this.addressA = addressA;
        this.addressB = addressB;
    }

    public Address getAddressA() {
        return addressA;
    }

    public void setAddressA(Address addressA) {
        this.addressA = addressA;
    }

    public Address getAddressB() {
        return addressB;
    }

    public void setAddressB(Address addressB) {
        this.addressB = addressB;
    }
}
