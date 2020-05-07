package com.assigned.printart.Model;

public class EndUsers
{
    private String Name, Password, PhoneNumber, DP, Address, Pincode;

public EndUsers()
{

}

    public EndUsers(String name, String password, String phoneNumber, String DP, String address, String pincode) {
        Name = name;
        Password = password;
        PhoneNumber = phoneNumber;
        this.DP = DP;
        Address = address;
        Pincode = pincode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getDP() {
        return DP;
    }

    public void setDP(String DP) {
        this.DP = DP;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPincode() {
        return Pincode;
    }

    public void setPincode(String pincode) {
        Pincode = pincode;
    }
}
