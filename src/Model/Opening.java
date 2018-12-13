package Model;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Opening {
    ArrayList<String> chaine = new ArrayList<>();
    String[] moves;
    String fichier="Ressource/opening.txt";
    boolean begining;

    public Opening() {
        this.chaine = openFile(this.chaine);
        this.begining=true;
    }

    public ArrayList openFile(ArrayList chaine) {
        {
            //lecture du fichier texte
            try {
                InputStream ips = new FileInputStream(this.fichier);
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

    public String isRevelant() {
        for(int ligne=0; ligne<chaine.size(); ligne++){
            String[] output = chaine.get(ligne).split(" ");
            boolean identical=true;
            int move=0;
            if (output.length > moves.length) {
                while (identical && move+3 < moves.length) {
                    if (!output[move].equals(moves[move + 3])) {
                        identical = false;
                    }
                    move++;
                }
                if (identical) {
                    return output[move];
                }
            }
        }
        return null;
    }
}
