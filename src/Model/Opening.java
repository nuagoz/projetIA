package Model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Opening {
    ArrayList chaine = new ArrayList();
    String fichier="Ressource/opening.txt";

    public ArrayList openFile(ArrayList chaine) {
        {
            //lecture du fichier texte
            try {
                InputStream ips = new FileInputStream(fichier);
                InputStreamReader ipsr = new InputStreamReader(ips);
                BufferedReader br = new BufferedReader(ipsr);
                String ligne;
                while ((ligne = br.readLine()) != null) {
                    chaine.add(ligne + "\n");
                }
                br.close();
            } catch (Exception e) {
                System.out.println(e.toString());
            }
        }
        return chaine;
    }
}
