package cbase;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @author obyte
 */
public class ContactCommander {

    private Scanner sc;
    private static final DataBaseFile dBaseFile = DataBaseFile.getInstance();
    private List<Contact> onScreenList;

    public ContactCommander() {
        sc = new Scanner(System.in);
    }

    public void printResults(List<Contact> contactList) {
        onScreenList = contactList;
        System.out.format("%-2s %-25s %-15s %-15s %n", "№", "Name", "Phone", "Adress");
        for (int i = 0; i < contactList.size(); i++) {
            System.out.format("%-2d %-25s %-15s %-15s %n",
                    i + 1, onScreenList.get(i).getName(),
                    onScreenList.get(i).getPhoneNumber(),
                    onScreenList.get(i).getAddress());
        }
    }

    public void printContact(Contact cont) {
        System.out.format("%-25s %-15s %-15s %n", "Name", "Phone", "Adress");
        System.out.format("%-25s %-15s %-15s %n", cont.getName(),
                cont.getPhoneNumber(), cont.getAddress());
    }

    private boolean yesNoAnswer() {
        System.out.println(" y/n?");
        String answer = sc.nextLine().toLowerCase();
        return (answer.startsWith("y")) ? true : false;
    }

    public void commandParser(List<String> list) {
        if (list.isEmpty()) {
            System.out.println("**Wrong command syntax");
            return;
        }

        String lowerCaseCommand = list.get(0).toLowerCase();
        switch (lowerCaseCommand) {
            case "-quit":
                System.out.println("Bye");
                System.exit(0);
            case "-showall":
                printResults(dBaseFile.showAllItemsFromBase());
                break;
            case "-find":
                if (list.size() == 2) {
                    findContact(list.get(1));
                } else {
                    findContact();
                }
                break;
            case "-edit":
                if (list.size() == 2 && isDigit(list.get(1))) {
                    editContact(Integer.parseInt(list.get(1)));
                } else {
                    System.out.println("**Nothing to edit. Enter line number");
                }
                break;
            case "-delete":
                if (list.size() == 2 && isDigit(list.get(1))) {
                    deleteContact(Integer.parseInt(list.get(1)));
                } else {
                    System.out.println("**Nothing to delete");
                }
                break;
            case "-add":
                if (list.size() == 4) {
                    addNewContact(new Contact(list.get(1), list.get(2), list.get(3)));
                } else {
                    addNewContact();
                }
                break;
            default:
                System.out.println("**Wrong command syntax");
                break;
        }
    }

    private boolean isDigit(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isLineNumberCorrect(int lineNumber) {
        if (onScreenList == null) {
            return false;
        } else {
            if (onScreenList.size() < lineNumber) {
                System.out.println("**Wrong line number");
                return false;
            }
            return true;
        }
    }

    public void addNewContact() {
        System.out.println("Enter new contact name");
        String name = sc.nextLine();
        System.out.println("Enter phone number");
        String phoneNumber = sc.nextLine();
        System.out.println("Enter contact adress");
        String adress = sc.nextLine();

        System.out.println("Save contact?");
        if (yesNoAnswer()) {
            Contact contact = new Contact(name, phoneNumber, adress);
            addNewContact(contact);
        }
    }

    public void addNewContact(Contact contact) {
        List<Contact> similarList = new ArrayList();
        similarList.addAll(dBaseFile.findItemsByParam(contact.getName()));
        similarList.addAll(dBaseFile.findItemsByParam(contact.getPhoneNumber()));
        similarList.addAll(dBaseFile.findItemsByParam(contact.getAddress()));

        if (similarList.isEmpty()) {
            System.out.println("**Absolutely new contact ");
            dBaseFile.writeItemToBase(contact.combineItems());
        } else {
            System.out.println("**Some similar contact's has been found in database");
            printResults(similarList);
            System.out.println("Save new contact in database anyway?");
            if (yesNoAnswer()) {
                dBaseFile.writeItemToBase(contact.combineItems());
                System.out.println("Successfully");
            }

            System.out.println("Edit exist's contact? (on screen)");
            if (yesNoAnswer()) {
                System.out.println("Enter contact line number on a screen"
                        + "or \"enter\" to do nothing");
                String line = sc.nextLine();
                if (isDigit(line)) {
                    int parseInt = Integer.parseInt(line);
                    if (onScreenList.size() >= parseInt) {
                        editContact(parseInt);
                    }
                }
            }
        }
    }

    public void editContact(int lineNumber) {
        if (!isLineNumberCorrect(lineNumber)) {
            return;
        }

        Contact newContact = null;
        System.out.println("**Your contact to edit is:");
        printContact(onScreenList.get(lineNumber - 1));

        System.out.println("**You really wanna to edit this contact?");
        if (yesNoAnswer()) {
            newContact = contactEditor(onScreenList.get(lineNumber - 1));

            System.out.println("**Edit again?");
            while (yesNoAnswer()) {
                newContact = contactEditor(onScreenList.get(lineNumber - 1));
                System.out.println("**Edit again?");
            }
        }

        if (newContact != null) {
            dBaseFile.removeItemFromBase(onScreenList.get(lineNumber - 1).combineItems());
            dBaseFile.writeItemToBase(newContact.combineItems());
            onScreenList.clear();
        }
    }

    private Contact contactEditor(Contact c) {
        System.out.println("**Enter new values or press enter to save old values");

        System.out.println("Enter new name:");
        String newName = sc.nextLine();

        System.out.println("Enter new phone number:");
        String newPhone = sc.nextLine();

        System.out.println("Enter new address:");
        String newAdress = sc.nextLine();

        Contact contact = new Contact();

        contact.setName(newName.isEmpty() ? c.getName() : newName);
        contact.setPhoneNumber(newPhone.isEmpty() ? c.getPhoneNumber() : newPhone);
        contact.setAddress(newAdress.isEmpty() ? c.getAddress() : newAdress);

        return contact;
    }

    public void findContact(String str) {
        List<Contact> similarAnswersList = dBaseFile.findItemsByParam(str);
        System.out.println("**Here is the similar rezults");
        printResults(similarAnswersList);
    }

    public void findContact() {
        System.out.println("**Enter your search parameter");
        findContact(sc.nextLine());
    }

    public void deleteContact(int lineNumber) {
        if (isLineNumberCorrect(lineNumber)) {

            System.out.println("**Your contact to delete is");
            printContact(onScreenList.get(lineNumber - 1));
            System.out.print("**Are you realy wanna to delete this contact?");
            if (yesNoAnswer()) {
                dBaseFile.removeItemFromBase(onScreenList.get(lineNumber - 1).combineItems());
                onScreenList.clear();
            }
        }
    }

    public void getCommand() {
        System.out.println("");
        System.out.println("**Enter your command");
        String nextLine = sc.nextLine();
        if (!nextLine.isEmpty()) {
            commandParser(StringParser.parseCommandLine(nextLine));
            getCommand();
        } else {
            System.out.println("**Wrong command syntax");
            getCommand();
        }
    }
}
