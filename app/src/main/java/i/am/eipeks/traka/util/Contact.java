package i.am.eipeks.traka.util;


public class Contact {


    private String contactName, contactNumber, _id;

    public Contact(String _id, String contactName, String contactNumber) {
        this.contactName = contactName;
        this.contactNumber = contactNumber;
        this._id = _id;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public String get_id() {
        return _id;
    }
}
