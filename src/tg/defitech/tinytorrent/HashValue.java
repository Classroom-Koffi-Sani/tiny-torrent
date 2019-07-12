package tg.defitech.tinytorrent;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Cette classe permet de créer une représentation de la valeur de hachage SHA-1
 * associée à une ressource.
 */
public class HashValue implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String value;

    /**
     * Construit une valeur de hachage à partir du contenu du fichier
     * {@code file} spécifié.
     */
    public HashValue(File file) {
        this.value = SHA1(file);
    }

    /**
     * Construit une valeur de hachage à partir d'une chaîne de caractères
     * quelconque.
     * 
     * @param value
     *            la chaîne de caractère à hacher.
     */
    public HashValue(String value) {
        this(value.getBytes(), false);
    }

    /**
     * Construit une valeur de hachage à partir d'une chaîne de caractères
     * représentant soit une valeur hachage ({@code isHashValue=true}) soit une
     * chaîne de caractère quelconque dont la valeur de hachage doit être
     * recalculée.
     * 
     * @param value
     *            la chaîne de caractère à hacher.
     * 
     * @param isHashValue
     *            indique si la payload passée en paramètre est déjà une valeur
     *            de hachage ({@code isHashValue=true}) auquel cas une instance
     *            de HashValue est créée en plaçant cette valeur en tant que
     *            valeur de hachage à l'intérieur de l'objet. Dans le cas
     *            contraire la valeur de hachage associée à la payload spécifiée
     *            est calculée et une nouvelle instance de HashValue est créée
     *            avec cette valeur.
     */
    public HashValue(String value, boolean isHashValue) {
        this(value.getBytes(), isHashValue);
    }

    /**
     * Construit une valeur de hachage à partir du tableau d'octets spécifié.
     * 
     * @param payload
     *            les octets représentant la resource.
     * 
     * @param isHashValue
     *            indique si la payload passée en paramètre est déjà une valeur
     *            de hachage ( {@code isHashValue=true}) auquel cas une instance
     *            de HashValue est créée en plaçant cette valeur en tant que
     *            valeur de hachage à l'intérieur de l'objet. Dans le cas
     *            contraire la valeur de hachage associée à la payload spécifiée
     *            est calculée et une nouvelle instance de HashValue est créée
     *            avec cette valeur.
     */
    public HashValue(byte[] payload, boolean isHashValue) {
        if (isHashValue) {
            this.value = new String(payload);
        } else {
            this.value = SHA1(payload);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return obj instanceof HashValue
                && this.value.equals(((HashValue) obj).value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return this.value;
    }

    /**
     * Renvoie la valeur de hachage SHA-1 encodée en héxadécimal.
     * 
     * @return la valeur de hachage SHA-1 encodée en héxadécimal.
     */
    public String getValue() {
        return this.value;
    }

    public static HashValue[] hash(File... files) {
        HashValue[] fileHashValues = new HashValue[files.length];

        for (int i = 0; i < files.length; i++) {
            fileHashValues[i] = new HashValue(files[i]);
        }

        return fileHashValues;
    }

    private static String SHA1(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            FileInputStream fis = new FileInputStream(file);

            byte[] dataBytes = new byte[1024];

            int nread = 0;
            while ((nread = fis.read(dataBytes)) != -1) {
                md.update(dataBytes, 0, nread);
            };

            return toHexadecimal(md.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String SHA1(byte[] payload) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(payload);
            byte[] digest = md.digest();

            return toHexadecimal(digest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String toHexadecimal(byte[] payload) {
        StringBuilder result = new StringBuilder();
        // convert the bytes to hex format
        for (int i = 0; i < payload.length; i++) {
            result.append(Integer.toHexString(0xFF & payload[i]));
        }

        return result.toString().toUpperCase();
    }

}
