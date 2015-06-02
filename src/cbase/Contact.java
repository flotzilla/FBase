package cbase;

/**
 * @author obyte
 */
public final class Contact {

    private String name;
    private String phoneNumber;
    private String address;

    public Contact(String name, String phoneNumber, String adress) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.address = adress;
    }

    public Contact() {
    }

    public void addParam(String param) {
        if (name == null) {
            name = param;
        } else if (phoneNumber == null) {
            phoneNumber = param;
        } else if (address == null) {
            address = param;
        } else {
            System.out.println("**All known params already exists");
            System.out.println("unknown value = " + param);
        }
    }

    public String combineItems() {
        if (name == null || phoneNumber == null || address == null) {
            System.out.println("**Cannot combine strings, init them first");
//            throw new NullPointerException(); ?
            return "";
        }
        //StringBuilder?
        return name + "\t" + phoneNumber + "\t" + address;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setName(String Name) {
        this.name = Name;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddress(String adress) {
        this.address = adress;
    }
}
