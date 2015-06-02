package cbase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author obyte
 */
public class DataBaseFile {

    private static DataBaseFile dataBaseInstance = null;
    private final String FILE_NAME = "database.txt";
    private File dataBase;

    public static DataBaseFile getInstance() {
        return dataBaseInstance == null ? dataBaseInstance = new DataBaseFile() : null;
    }

    private DataBaseFile() {
        dataBase = new File(FILE_NAME);
        createNewDataBaseFile();
    }

    private void createNewDataBaseFile() {
        try {
            if (dataBase.createNewFile()) {
                System.out.println("**New database file was created");
            } else {
                System.out.println("**Database file already exists");
                if (dataBase.length() > 0) {
                    System.out.println("  and not empty");
                }
            }
        } catch (IOException e) {
            System.out.println("**Cannot crate new database file");
        }
    }

    public boolean writeItemToBase(String str) {
        if (!StringParser.isStringHaveRightFormat(str)) {
            System.out.println("**Incorrect string format for saving");
            return false;
        }
        try (FileWriter fw = new FileWriter(dataBase, true)) {
            fw.append(str);
            fw.append(System.getProperty("line.separator"));
            System.out.println("**New item has been added to " + FILE_NAME);
            return true;
        } catch (IOException ex) {
            System.out.println("**Cannot write line to file");
            return false;
        }
    }

    public List<Contact> showAllItemsFromBase() {
        String line;
        List<Contact> fileData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(dataBase))) {
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                fileData.add(StringParser.parseStringforCreatingContact(line));
            }
        } catch (IOException ioe) {
            System.out.println("**Cannot read from file");
            return Collections.emptyList();
        }
        return fileData;
    }

    public List<Contact> findItemsByParam(String paramToFind) {
        if (!StringParser.isSearchStringCorrect(paramToFind)) {
            System.out.println("**Wrong search paremeters");
            return Collections.emptyList();
        }

        List<Contact> fileData = new ArrayList();
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(dataBase))) {
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                if (StringParser.isConsists(paramToFind, line)) {
                    fileData.add(StringParser.parseStringforCreatingContact(line));
                }
            }
        } catch (IOException ioe) {
            System.out.println("**Cannot read from dataBase file");
            return Collections.emptyList();
        }
        return fileData;
    }

    public boolean removeItemFromBase(String lineToRemove) {
        if (!StringParser.isSearchStringCorrect(lineToRemove)) {
            System.out.println("**Wrong parameter");
            return false;
        }

        File tmpFile = new File(dataBase.getAbsolutePath() + ".tmp");

        String line;
        BufferedReader br = null;
        PrintWriter pw = null;
        try {
            br = new BufferedReader(new FileReader(dataBase));
            pw = new PrintWriter(new FileWriter(tmpFile));
            while ((line = br.readLine()) != null) {
                if (!line.trim().equals(lineToRemove)) {
                    pw.println(line);
                    pw.flush();
                }
            }
            pw.close();
            br.close();

            if (!dataBase.delete()) {
                System.out.println("**Cannot delete the file");
                return false;
            }
            if (!tmpFile.renameTo(dataBase)) {
                System.out.println("**Could not rename file");
                return false;
            }
            System.out.println("**Item was deleted");
            return true;
        } catch (IOException ioe) {
            System.out.println("**Cannot read from dataBase file");
            pw.close();
            try {
                br.close();
                return false;
            } catch (IOException ex) {
                System.out.println("**Error in process of closing file");
                return false;
            }
        }
    }
}
