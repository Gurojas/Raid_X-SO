/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raidso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo Rojas
 */
public class FileOperations {
    
    
    
    public FileOperations(){
        
    }
    
    public File createFolder(String folderName){
        File fileFolder = new File(folderName);
        fileFolder.mkdir();
        return fileFolder;
    }
    
    public String getFileContent(File file){
        String fileContent = "";
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        while (scanner.hasNextLine()){
            fileContent = fileContent + scanner.nextLine();
        }
        
        return fileContent;
    }
    //createSubFolders(String fileName, String path, int numSubFolders)
    public void createSubFolders(ArrayList<String> subFolderNames, String path, int numSubFolders){
        
        File fileSubFolder = null;
        for (int i = 0; i < subFolderNames.size(); i++) {
            String subFolderName = subFolderNames.get(i);
            fileSubFolder = new File(path+"\\"+subFolderName); // creo la carpeta
            fileSubFolder.mkdir();
            fileSubFolder = new File(path+"\\"+subFolderName+"\\"+subFolderName+".txt"); // creo el archivo ttx dentro de la carpeta
            try {
                fileSubFolder.createNewFile();
            } catch (IOException ex) {
                Logger.getLogger(RaidGenerator.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    public String fileNameWithoutFormat(String filename){
        int index = filename.indexOf(".txt");
        String newFileName = filename.substring(0, index);
        return newFileName;
    }
    
    //writeFile(ArrayList<String> blocks, String[] subFoldersNames, String path, int numDisk)
    public void writeFile(ArrayList<String> blocks, ArrayList<String> subFoldersNames, String path, int numDisk){
        File file = null;
        FileWriter fw = null;
        
        int j = 0;
        int blockSize = blocks.size();

        for (int i = 0; i < blockSize; i++) {
            String nameSubFolder = subFoldersNames.get(j);
            
            String diskPath = path+"\\"+nameSubFolder+"\\"+nameSubFolder+".txt";
            file = new File(diskPath);
            try {
                fw = new FileWriter(file,true);
            } catch (IOException ex) {
                Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            String content = blocks.get(i);
            try {
                fw.write(content+"\n");
            } catch (IOException ex) {
                Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                fw.close(); // important line
            } catch (IOException ex) {
                Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            j = (j + 1) % numDisk;
        }
    }
    
    public void writeFileParity(String parityFolder, ArrayList<String> parities ,String path, int numDisk, int raidType){
        File file = null;
        FileWriter fw = null;
        
        String diskPath = path+"\\"+parityFolder+"\\"+parityFolder+".txt";
        file = new File(diskPath);

        String parity = "";
        if (raidType == 3){
            for (int i = 0; i < parities.size(); i++) {
                parity = parity + parities.get(i);
                if ((i + 1) % numDisk == 0){
                    try {
                        fw = new FileWriter(file,true);
                        fw.write(parity+"\n");

                    } catch (IOException ex) {
                        Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    parity = "";

                    try {
                        fw.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
            // cuando el numero de caractes de la paridad no coincide con el numero de discos
            if (!parity.equals("")){
                try {
                    fw = new FileWriter(file,true);
                    fw.write(parity+"\n");
                    fw.close();   
                } catch (IOException ex) {
                    Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            for (int i = 0; i < parities.size(); i++) {
                try {
                    fw = new FileWriter(file,true);
                    fw.write(parities.get(i)+"\n");
                    fw.close();
                } catch (IOException ex) {
                    Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
    
    public String readFile(String[] subFoldersNames, String path, int numDisk){
        File file = null;
        Scanner scanner = null;
       
        ArrayList<ArrayList<String>> blocks = new ArrayList<>();
        for (int i = 0; i < numDisk; i++) {
            ArrayList<String> array = new ArrayList<>();
            blocks.add(array);
        }
        
        for (int i = 0; i < numDisk; i++) {
            String subFolderName = subFoldersNames[i];
            String diskPath = path+"\\"+subFolderName+"\\"+subFolderName+".txt";
            file = new File(diskPath);
            try {
                scanner = new Scanner(file);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(FileOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            ArrayList<String> blockDisk = blocks.get(i);
            
            while (scanner.hasNextLine()){
                String contentDisk = scanner.nextLine();
                blockDisk.add(contentDisk);
            }
            scanner.close();
        }
        
        String binaryString = "";

        int maxSize = this.maxSize(blocks);
        
        String blockDisk[][] = new String[maxSize][numDisk];
        
        // zona critica
        for (int j = 0; j < numDisk; j++) {
            ArrayList<String> block = blocks.get(j);
            for (int i = 0; i < maxSize; i++) {
                //blockDisk[j][i] = block.get(j);
                if (i < block.size()){
                    blockDisk[i][j] = block.get(i);
                }
                else{
                    blockDisk[i][j] = "";
                }                
            }            
        }
        
        for (int i = 0; i < maxSize; i++) {
            for (int j = 0; j < numDisk; j++) {
                // blockDisk[i][j].length() != 1 caso cuando viene con bit de paridad (RAID 5 y 6)
                if (!blockDisk[i][j].equals("") && blockDisk[i][j].length() != 1){
                    binaryString = binaryString + blockDisk[i][j];
                }
            }
        }
        
        return binaryString;
    }
    
    private int maxSize(ArrayList<ArrayList<String>> blocks){
        int max = 0;
        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).size() >= max){
                max = blocks.get(i).size();
            }
        }
        return max;

    }
    
    
    

    
}
