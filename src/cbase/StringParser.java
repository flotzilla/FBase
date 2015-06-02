package cbase;

import java.util.ArrayList;
import java.util.List;

/**
 * @author obyte
 */
public class StringParser {

    public static boolean isSearchStringCorrect(String str) {
        return (str != null && str.length() >= 1) ? true : false;
    }

    public static boolean isConsists(String paramToSearch, String searchInString) {
        return (searchInString.toLowerCase().contains(paramToSearch.toLowerCase())) ? true : false;
    }

    public static boolean isStringHaveRightFormat(String str) {
        char[] charMass = str.toCharArray();
        int numberOfRepeats = 0;
        for (int i = 0; i < charMass.length; i++) {
            if (charMass[i] == '\t') {
                numberOfRepeats++;
            }
        }
        return (numberOfRepeats == 2) ? true : false;
    }

    public static Contact parseStringforCreatingContact(String str) {
        char[] toCharArray = str.toCharArray();

        String word;
        Contact contact = new Contact();
        for (int i = 0, lastWordBegin = 0; i < toCharArray.length; i++) {
            if (toCharArray[i] == '\t') {
                word = str.substring(lastWordBegin, i);
                contact.addParam(word);
                lastWordBegin = i + 1;
            }
            if (i == toCharArray.length - 1) {
                word = str.substring(lastWordBegin, i + 1);
                contact.addParam(word);
            }
        }
        return contact;
    }

    public static List<String> parseCommandLine(String command) {
        List<String> list = new ArrayList();
        if (!command.contains(" ")) {
            list.add(command);
            return list;
        }
        
        if (command.contains("name=\"") && command.contains("\" phone=\"") &&
                command.contains("\" address=\"")) {
            list.add(command.substring(0, command.indexOf(" ")));
            list.add(command.substring(command.lastIndexOf("name=\"") +6 ,
                    command.lastIndexOf("\" phone=")));
            list.add(command.substring(command.lastIndexOf("phone=\"")+7,
                    command.lastIndexOf("\" address")));
            list.add(command.substring(command.lastIndexOf("address=\"") +9, 
                    command.lastIndexOf("\"")));            
            return list;        
        }
        
        char[] toCharArray = command.toLowerCase().toCharArray();
        String word;
        for (int i = 0, lastWordBegin = 0; i < toCharArray.length; i++) {
            if (i == toCharArray.length - 1) {
                word = command.substring(lastWordBegin, i + 1);
                list.add(word);
                break;
            }

            if (toCharArray[i] == ' ' && toCharArray[i + 1] == ' ') {
                String x = command.substring(0, i) + command.substring(i, command.length());
                toCharArray = x.toCharArray();
                continue;
            }

            if (toCharArray[i] == ' ') {
                word = command.substring(lastWordBegin, i);
                list.add(word);
                lastWordBegin = i + 1;
            }
        }
        return list;
    }
}
