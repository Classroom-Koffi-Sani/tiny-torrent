package tg.defitech.tinytorrent.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Set;

import tg.defitech.tinytorrent.Chunk;
import tg.defitech.tinytorrent.HashValue;
import tg.defitech.tinytorrent.exceptions.ChunkNotAvailableException;

/**
 * Interface définissant les méthodes d'un client pouvant être appelées de
 * manière distante.
 */
public interface RemoteClient extends Remote {

    /**
     * Méthode appelée par un autre client pour demander au client distant de
     * lui renvoyer le morceau (chunk) associé à la ressource identifiée par la
     * valeur de hachage {@code resourceHashValue} et ayant pour index le numéro
     * de morceau {@code chunkIndex}.
     * 
     * @param resourceHashValue
     *            la valeur de hachage identifiant la ressource pour laquelle un
     *            chunk est demandé.
     * 
     * @param chunkIndex
     *            l'index du chunk demandé.
     * 
     * @return le chunk demandé.
     * 
     * @throws RemoteException
     */
    Chunk leech(HashValue resourceHashValue, int chunkIndex)
            throws RemoteException, ChunkNotAvailableException;

    /**
     * Renvoie un ensemble contenant l'index des morceaux (chunks) qui sont
     * disponibles. Si aucun morceau est disponible la valeur {@code null} est
     * renvoyée.
     * 
     * @param resourceHashValue
     *            la valeur de hachage indiquant la resource par laquelle le
     *            client est intéressé.
     * 
     * @return un ensemble contenant l'index des morceaux (chunks) qui sont
     *         disponibles.
     * 
     * @throws RemoteException
     */
    Set<Integer> findAvailableChunks(HashValue resourceHashValue)
            throws RemoteException;

    /**
     * Renvoie une chaîne de caractères sensée identifier de manière unique un
     * client.
     * 
     * @return une chaîne de caractères sensée identifier de manière unique un
     *         client.
     * 
     * @throws RemoteException
     */
    String getIdentifier() throws RemoteException;

}
