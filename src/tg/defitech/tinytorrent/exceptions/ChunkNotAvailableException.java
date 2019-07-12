package tg.defitech.tinytorrent.exceptions;

import tg.defitech.tinytorrent.HashValue;

/**
 * Définie une exception qui est lancée lorsqu'un client demande à un autre
 * client de lui renvoyer un morceau dont il ne dispose pas.
 */
public class ChunkNotAvailableException extends Exception {

    private static final long serialVersionUID = 1L;

    public ChunkNotAvailableException(HashValue resourceHashValue,
            int chunkIndex) {
        super("Chunk number " + chunkIndex
                + " associated to the resource identified by the hash value "
                + resourceHashValue + " is not available");
    }

}
