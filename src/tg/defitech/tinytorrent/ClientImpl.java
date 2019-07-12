/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tg.defitech.tinytorrent;

import java.io.File;
import java.util.Scanner;
import tg.defitech.tinytorrent.api.Client;

/**
 *
 * @author blaesus
 */
public class ClientImpl implements Client{
    
   
    @Override
    public File share(File file, String trackerURL) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File download(TinyTorrent torrent) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public File getSharedFolder() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
    public static void partagerRessource(){
        System.out.println("---PARTAGE FICHIER---");
    }
    
    public static void telechargerRessource(){
        System.out.println("---TELECHARGEMENT FICHIER---");
    }
    
    public static void menu(){
        Scanner sc = new Scanner(System.in);
        System.out.println("---MENU---");
        int rep = 0;
        while(rep == 0){
             System.out.println("[1]->partager un fichier");
             System.out.println("[2]->Telecharger un fichier");
             rep = sc.nextInt();
             if(rep == 1){
                 partagerRessource();
             }
             else if(rep == 1){
                 telechargerRessource();
             }
             else{
                 rep = 0;
             }
        }
       
    }
    
    public static void main(String[] args){
        //String sharedFolderLink = args[0];
        menu();
        
    }
}
