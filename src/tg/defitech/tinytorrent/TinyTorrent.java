package tg.defitech.tinytorrent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Cette classe correspond à la représentation objet d'un fichier
 * {@code .tinytorrent}.
 */
public class TinyTorrent {

    public static final String EXTENSION_NAME = "tinytorrent";

    private final String trackerURL;

    private final String resourceName;

    private final HashValue resourceHashValue;

    private final HashValue[] chunkHashValues;

    public TinyTorrent(String trackerURL, String resourceName,
            HashValue resourceHashValue, HashValue[] chunkHashValues) {
        this.trackerURL = trackerURL;
        this.resourceName = resourceName;
        this.resourceHashValue = resourceHashValue;
        this.chunkHashValues = chunkHashValues;
    }

    public String getTrackerURL() {
        return this.trackerURL;
    }

    public String getResourceName() {
        return this.resourceName;
    }

    public HashValue getResourceHashValue() {
        return this.resourceHashValue;
    }

    public HashValue[] getChunkHashValues() {
        return this.chunkHashValues;
    }

    public HashValue getChunkHashValue(int chunkIndex) {
        return this.chunkHashValues[chunkIndex];
    }

    /**
     * Crée une instance de {@link TinyTorrent} depuis le fichier
     * {@code tinytorrentFile} issue d'un appel à
     * {@link TinyTorrent#write(TinyTorrent, File)}.
     * 
     * @param tinytorrentFile
     *            le fichier tinytorrent à parser.
     *            
     * @return une instance de {@link TinyTorrent} créée depuis le fichier
     *         {@code tinytorrentFile} issue d'un appel à
     *         {@link TinyTorrent#write(TinyTorrent, File)}.
     */
    public static TinyTorrent read(File tinytorrentFile) {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(tinytorrentFile));

            String resourceName = br.readLine();
            String resourceHashValue = br.readLine();
            String trackerURL = br.readLine();

            List<String> chunkStringHashValues = new ArrayList<String>();

            String line;
            while ((line = br.readLine()) != null) {
                chunkStringHashValues.add(line);
            }

            HashValue[] chunkHashValues =
                    new HashValue[chunkStringHashValues.size()];
            for (int i = 0; i < chunkStringHashValues.size(); i++) {
                chunkHashValues[i] =
                        new HashValue(chunkStringHashValues.get(i), true);
            }

            return new TinyTorrent(trackerURL, resourceName, new HashValue(
                    resourceHashValue, true), chunkHashValues);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Crée un fichier tinytorrent pour une instance {@code torrent} de
     * {@link TinyTorrent} dans le dossier {@code outputDirectory}.
     * 
     * @param torrent
     *            le tinytorrent à matérialiser sous la forme d'un fichier.
     * 
     * @param outputDirectory
     *            le dossier où le fichier torrent sera écrit.
     * 
     * @return le chemin absolu vers le fichier torrent créé.
     * 
     * @throws IOException
     *             si une erreur d'écriture se produit durant l'éxécution de la
     *             méthode.
     */
    public static File write(TinyTorrent torrent, File outputDirectory) {
        File torrentFile =
                new File(outputDirectory, torrent.getResourceName() + "."
                        + EXTENSION_NAME);

        PrintWriter pw = null;
        try {
            pw =
                    new PrintWriter(new BufferedWriter(new FileWriter(
                            torrentFile)));
            pw.write(torrent.getResourceName());
            pw.write("\n");
            pw.write(torrent.getResourceHashValue().getValue());
            pw.write("\n");
            pw.write(torrent.getTrackerURL());
            pw.write("\n");

            for (HashValue chunkHashValue : torrent.getChunkHashValues()) {
                pw.write(chunkHashValue.getValue());
                pw.write("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            pw.close();
        }

        return torrentFile;
    }
 
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TinyTorrent) {
            TinyTorrent torrent = (TinyTorrent) obj;

            return Arrays.equals(this.chunkHashValues, torrent.chunkHashValues)
                    && this.resourceHashValue.equals(torrent.resourceHashValue)
                    && this.resourceName.equals(torrent.getResourceName())
                    && this.trackerURL.equals(torrent.getTrackerURL());
        }

        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31
                * (31 * (31 * (31 + Arrays.hashCode(this.chunkHashValues)) + ((this.resourceHashValue == null)
                        ? 0 : this.resourceHashValue.hashCode())) + ((this.resourceName == null)
                        ? 0 : this.resourceName.hashCode()))
                + ((this.trackerURL == null)
                        ? 0 : this.trackerURL.hashCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "TinyTorrent[trackerURL=" + this.trackerURL + ", resourceName="
                + this.resourceName + ", resourceHashValue="
                + this.resourceHashValue + ", chunkHashValues="
                + Arrays.toString(this.chunkHashValues) + "]";
    }

}
