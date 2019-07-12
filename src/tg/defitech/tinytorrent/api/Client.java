package tg.defitech.tinytorrent.api;



import java.io.File;

import tg.defitech.tinytorrent.HashValue;
import tg.defitech.tinytorrent.TinyTorrent;

/**
 * Interface définissant les méthodes d'un client pouvant être appelées localement
 * Elle sert pour implémenter des tests et il n'est pas obligatoire de l'utiliser dans
 * le projet
 */
public interface Client {

    /**
     * Partage le fichier {@code file} indiqué. Cela consiste tout d'abord à
     * créer un fichier {@code .tinytorrent} associé à {@code file}. Ensuite à
     * découper le fichiers en morceaux. Enfin le client doit contacter le
     * tracker via la méthode {@link Tracker#register(HashValue, RemoteClient)}
     * afin de l'informer de la ressource à partager.
     * 
     * @param file
     *            le nom du fichier identifiant le fichier à partager. Le
     *            fichier à partager est supposé se trouver dans le dossier
     *            partagé sur client (c.f. {@link #getSharedFolder()}.
     * 
     * @param trackerURL
     *            l'URL du tracker à contacter et utilisée pour créer le
     *            torrent.
     * 
     * @return le chemin absolu vers le torrent qui a été créé.
     */
    File share(File file, String trackerURL);

    /**
     * Télécharge une ressource à partir des informations disponibles depuis le
     * {@code torrent} spécifié. Les morceaux récupérés et la ressource recréée
     * se trouvent dans le même dossier que le fichier {@code .tinytorrent}.
     * 
     * @param torrent
     *            le torrent contenant les informations nécessaires pour
     *            télécharger la ressource souhaitée.
     * 
     * @return le chemin absolu vers la nouvelle ressource qui a été
     *         téléchargée.
     * 
     */
    File download(TinyTorrent torrent);

    /**
     * Renvoie le chemin absolu vers le dossier ou sont stockés les ressources
     * et les fichiers intermédiaires qui sont téléchargés.
     * 
     * @return le chemin absolu vers le dossier ou sont stockés les ressources
     *         et les fichier intermédiaires qui sont téléchargés.
     */
    File getSharedFolder();

}
