package tg.defitech.tinytorrent.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import tg.defitech.tinytorrent.HashValue;
import tg.defitech.tinytorrent.HashValue;
import tg.defitech.tinytorrent.api.RemoteClient;

/**
 * Un tracker maintient pour chacune des ressouces partagées une liste des
 * clients disposant d'une partie ou de l'intégralité de cette ressource.
 */
public interface Tracker extends Remote {

    /**
     * Enregistre un client partageant une ressource (identifiée par sa valeur
     * de hachage) en partie ou en totalité. Si le tracker contient déjà le
     * client spécifié pour la resource identifiée par la valeur de hachage
     * {@code resourceHashValue} alors cet appel de méthode ne fait rien.
     * 
     * @param resourceHashValue
     *            la valeur de hachage associée à la resource à partager.
     * 
     * @param client
     *            le client partageant une partie ou la totalité de la resource.
     */
    void register(HashValue resourceHashValue, RemoteClient client)
            throws RemoteException;

    /**
     * Permet d'indiquer au tracker qu'un {@code client} ne gère plus la
     * ressource identifiée par {@code resourceHashValue}.
     * 
     * @param client
     *            la référence client à supprimer.
     */
    void unregister(HashValue resourceHashValue, RemoteClient client)
            throws RemoteException;

    /**
     * Renvoie un ensemble contenant les ressources (identifiées par leur valeur
     * de hachage) gérées par le tracker.
     * 
     * @return un ensemble contenant les ressources (identifiées par leur valeur
     *         de hachage) gérées par le tracker.
     */
    Set<HashValue> findResourceHashValues() throws RemoteException;

    /**
     * Renvoie l'ensemble des ressources (identifiées par leur valeur de
     * hachage) gérées par le tracker pour un client donné.
     * 
     * @return l'ensemble des ressources (identifiées par leur valeur de
     *         hachage) gérées par le tracker pour un client donné.
     */
    Set<HashValue> findResourceHashValues(RemoteClient client)
            throws RemoteException;

    /**
     * Renvoie l'ensemble des clients s'étant enregistrés sur le tracker et
     * disposant de la ressource identifiée par la valeur de hachage
     * {@code resourceHashValue}.
     * 
     * @param resourceHashValue
     *            le valeur de hachage identifiant une resource.
     * 
     * @return l'ensemble des clients s'étant enregistrés sur le tracker et
     *         disposant de la ressource identifiée par la valeur de hachage
     *         {@code resourceHashValue}.
     */
    Set<RemoteClient> findClients(HashValue resourceHashValue)
            throws RemoteException;

}
