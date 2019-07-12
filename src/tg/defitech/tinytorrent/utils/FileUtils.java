package tg.defitech.tinytorrent.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;

import tg.defitech.tinytorrent.Chunk;

/**
 * Classe utilitaire permettant de manipuler des fichiers.
 */
public class FileUtils {

    /**
     * Découpe le fichier indiqué en morceaux de taille maximum {@code 1Mio}.
     * Chacun des morceaux créés est écrit dans le dossier
     * {@code outputDirectory} en le suffixant avec le numéro d'index associé au
     * chunk.
     * 
     * @param file
     *            le fichier à découper en morceaux.
     * 
     * @param outputDirectory
     *            le dosier ou sont écrit les différents fichiers associés aux
     *            morceaux.
     * 
     * @return un tableau contenant le chemin absolu vers chacun des chunks
     *         créés.
     */
    public static File[] createChunks(File file, File outputDirectory) {
        return createChunks(file, outputDirectory, 1024 * 1024);
    }

    /**
     * Découpe le fichier {@code file} en morceaux de taille maximum
     * {@code chunkSize} octets. Les morceaux créés sont placés dans le dossier
     * {@code outputDirectory}.
     * 
     * @param file
     *            le fichier à découper en morceaux.
     * 
     * @param chunkSize
     *            la taille maximum de chacun des morceaux.
     * 
     * @param outputDirectory
     *            le dossier ou sont écrits les différents fichiers associés aux
     *            morceaux.
     * 
     * @return un tableau contenant le chemin absolu vers chacun des chunks
     *         créés.
     */
    public static File[] createChunks(File file, File outputDirectory,
                                      int chunkSize) {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);

            FileChannel fc = fis.getChannel();
            ByteBuffer bb = ByteBuffer.allocate(chunkSize);

            int chunkIndex = 0;
            int nbBytesRead = 0;

            File[] result =
                    new File[(int) ((file.length() / chunkSize) + ((file.length() % chunkSize) != 0
                            ? 1 : 0))];

            while ((nbBytesRead = fc.read(bb)) >= 0) {
                File chunkFile =
                        new File(outputDirectory, Chunk.suffix(
                                file.getName(), chunkIndex));
                FileOutputStream fos = new FileOutputStream(chunkFile);
                fos.write(bb.array(), 0, nbBytesRead);
                fos.close();

                result[chunkIndex] = chunkFile;
                chunkIndex++;
                bb.clear();
            }
            return result;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    /**
     * Remplit le fichier {@code file} de données aléatoires jusqu'à atteindre
     * une taille de {@code size} octets. Si le fichier existe pas il est créé.
     * Dans le cas ou le fichier existe déjà, les données existantes sont
     * effacées.
     * 
     * @param file
     *            le fichier à créer.
     * 
     * @param size
     *            la taille du fichier.
     */
    public static void fill(File file, int size) {
        SecureRandom random = new SecureRandom();

        byte[] payload = new byte[size];
        random.nextBytes(payload);

        file.getParentFile().mkdirs();

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(payload);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fusionne le contenu des fichiers {@code files} dans le fichier
     * {@code file} en suivant l'ordre dans lequel les fichiers {@code files}
     * sont spécifiés.
     * 
     * @param file
     *            le fichier dans lequel sont fusionnés les fichiers spécifiés.
     * 
     * @param files
     *            les fichiers à fusionner.
     */
    public static void merge(File file, File... files) {
        for (File f : files) {
            write(file, f, true);
        }
    }

    /**
     * Copie le fichier {@code file} dans le dossier {@code location}. Si le nom
     * de fichier associé à {@code file} existe déjà dans {@code location} son
     * contenu est remplacé par celui du fichier {@code file}.
     * 
     * @param file
     *            le chemin absolu vers le fichier à copier.
     * 
     * @param location
     *            le dossier où doit être copié le fichier.
     * 
     * @return le chemin absolu vers le fichier copié.
     */
    public static File copy(File file, File location) {
        File fileCopy = new File(location, file.getName());

        write(fileCopy, file, false);

        return fileCopy;
    }

    /**
     * Ecrit le contenu du fichier {@code file2} dans le fichier {@code file1}.
     * 
     * @param file1
     *            le fichier auquel on va concaténer des données.
     * 
     * @param file2
     *            le fichier contenant les données à concaténer.
     * 
     * @param append
     *            un booléen indiquant si le contenu du fichier {@code file2}
     *            est concaténé au fichier {@code file1} ou si le le contenu du
     *            fichier {@code file2} remplace le contenu du fichier
     *            {@code file1}.
     */
    public static void write(File file1, File file2, boolean append) {
        if (!file1.exists()) {
            if (!file1.getParentFile().exists()) {
                file1.getParentFile().mkdirs();
            }
            try {
                file1.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(file2));
            bos = new BufferedOutputStream(new FileOutputStream(file1, append));

            int len;
            byte[] buf = new byte[1024];
            while ((len = bis.read(buf)) > 0) {
                bos.write(buf, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bis.close();
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Supprime de manière récursive un dossier ainsi que son contenu.
     * 
     * @param path
     *            le fichier à supprimer.
     * 
     * @return {@code true} si le dossier ainsi que son contenu a été supprimé,
     *         {@code false} sinon.
     * 
     * @throws FileNotFoundException
     *             si le fichier {@code path} indiqué existe pas.
     */
    public static boolean delete(File path) throws FileNotFoundException {
        if (!path.exists()) {
            throw new FileNotFoundException(path.getAbsolutePath());
        }

        boolean ret = true;
        if (path.isDirectory()) {
            for (File f : path.listFiles()) {
                ret = ret && FileUtils.delete(f);
            }
        }
        return ret && path.delete();
    }

}
