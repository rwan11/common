package com.rwan.im.server.example.xml;

import java.util.List;

/**
 * @author johnlog
 * @date 2018/12/26
 */
public class Customer {


    private long customerNo;

    private String firstName;

    private String lastName;

    private List<String> middleNames;

    public long getCustomerNo() {
        return customerNo;
    }

    public void setCustomerNo(long customerNo) {
        this.customerNo = customerNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<String> getMiddleNames() {
        return middleNames;
    }

    public void setMiddleNames(List<String> middleNames) {
        this.middleNames = middleNames;
    }
}
