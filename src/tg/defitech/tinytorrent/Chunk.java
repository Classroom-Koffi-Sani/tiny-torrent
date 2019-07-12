package tg.defitech.tinytorrent;



import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

/**
 * Un chunk représente un morceau d'une ressource. Chaque morceau est identifié
 * par un index afin d'avoir la possibilité de les rassembler dans le bon ordre.
 */
public class Chunk implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    private final int index;

    private final byte[] payload;

    /**
     * Construit un chunk à partir de sa représentation sur le disque.
     * 
     * @param file
     *            le fichier associé au morceau.
     * 
     * @param index
     *            le numéro de séquence associé au morceau.
     */
    public Chunk(File file, int index) {
        if (!file.isFile() || !file.toString().contains(".chunk")) {
            throw new IllegalArgumentException(
                    "The specified file is not a chunk: " + file);
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            int charRead = 0;
            while ((charRead = fis.read()) != -1) {
                baos.write(charRead);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.name = file.getName();
        this.index = index;
        this.payload = baos.toByteArray();
    }

    /**
     * Construit un chunk à partir de son numéro de séquence et les données
     * qu'il doit contenir.
     * 
     * @param resourceFilename
     *            le nom de fichier associé à la ressource.
     * 
     * @param chunkPayload
     *            les données contenues par le morceau.
     * 
     * @param index
     *            le numéro de séquence associé au morceau.
     */
    public Chunk(String resourceFilename, byte[] chunkPayload, int index) {
        this.name = Chunk.suffix(resourceFilename, index);
        this.index = index;
        this.payload = chunkPayload;
    }

    /**
     * Renvoie le numéro de séquence associé au morceau.
     * 
     * @return le numéro de séquence associé au morceau.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Renvoie les données contenu par le morceau.
     * 
     * @return les données contenu par le morceau.
     */
    public byte[] getPayload() {
        return this.payload;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return 31 * (31 * (31 + this.index) + Arrays.hashCode(this.payload))
                + this.name.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Chunk) && this.index == ((Chunk) obj).index
                && this.name == ((Chunk) obj).name
                && Arrays.equals(this.payload, ((Chunk) obj).payload);
    }

    /**
     * Sérialise le morceau {@code chunk} spécifié dans le dossier
     * {@code outputDirectory}.
     * 
     * @param chunk
     *            le morceau à sérialiser.
     * 
     * @param outputDirectory
     *            le dossier où le fichier torrent sera écrit.
     * 
     * @return le chemin absolu vers le fichier créé.
     */
    public static File write(Chunk chunk, File outputDirectory) {
        File chunkFile = new File(outputDirectory, chunk.name);

        BufferedOutputStream bos = null;
        try {
            bos = new BufferedOutputStream(new FileOutputStream(chunkFile));
            bos.write(chunk.payload);
            return chunkFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Renvoie un nouveau nom de fichier en suffixant le nom de fichier
     * {@code filename} avec un suffixe contenant le numéro de morceau
     * {@code chunkIndex}.
     * 
     * @param filename
     *            le nom de fichier à suffixer.
     * 
     * @param chunkIndex
     *            l'index associé au morceau.
     * 
     * @return un nouveau nom de fichier en suffixant le nom de fichier
     *         {@code filename} avec un suffixe contenant le numéro de morceau
     *         {@code chunkIndex}.
     */
    public static String suffix(String filename, int chunkIndex) {
        StringBuilder result = new StringBuilder(filename);
        result.append(".chunk");
        result.append(chunkIndex);

        return result.toString();
    }

}
