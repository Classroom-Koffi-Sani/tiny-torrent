/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tg.defitech.tinytorrent;
import java.rmi.RemoteException;
import java.util.Set;
import tg.defitech.tinytorrent.api.RemoteClient;
import tg.defitech.tinytorrent.api.Tracker;
/**
 *
 * @author blaesus
 */
public class TrackerImpl implements Tracker{

    @Override
    public void register(HashValue resourceHashValue, RemoteClient client) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void unregister(HashValue resourceHashValue, RemoteClient client) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<HashValue> findResourceHashValues() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<HashValue> findResourceHashValues(RemoteClient client) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Set<RemoteClient> findClients(HashValue resourceHashValue) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
