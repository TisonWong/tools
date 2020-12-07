package temp;

import java.io.File;
import java.util.Scanner;

public class ChangeFileName {
    public static void main(String[] args) {

        System.out.println("Entry Folder Path:");
        Scanner scannerPath = new Scanner(System.in);
        String folderPath = scannerPath.nextLine();

        System.out.println("Entry Append Chars:");
        String append = scannerPath.nextLine();

        File folder = new File(folderPath);
        String newName = null;
        String exp =null;
        for (File file : folder.listFiles()) {
            newName = file.getName().substring(0,file.getName().lastIndexOf("."));
            exp = file.getName().substring(file.getName().lastIndexOf("."));
            file.renameTo(new File(folderPath,newName+append+exp));
        }
    }
}
