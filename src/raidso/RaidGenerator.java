/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package raidso;

import java.io.File;
import java.util.ArrayList;


/**
 *
 * @author Gustavo
 */
public class RaidGenerator {
    
    // separo en 8 bits (1 byte)
    private final int blockSize = 8; 
    // number of disk to use
    private final int numDisk = 3;
    
    
    
    
    public RaidGenerator(){
        
    }
    
    public void raid0(String fileContent, String fileName){
        
        char stringArray[] = fileContent.toCharArray();
        
        String binaryString = "";
        
        for (int i = 0; i < stringArray.length; i++) {
            char c = stringArray[i];
            binaryString = binaryString + this.charToBin(c);
        }
        
        //String binaryString = this.stringToBin(fileContent);
        ArrayList<String> blocks = this.separateBinaryStingInBlocks(binaryString);
        
        FileOperations fileOperations = new FileOperations();
        
        File fileFolder = fileOperations.createFolder("RAID_0");

        
        String fileNameWithoutFormat = fileOperations.fileNameWithoutFormat(fileName);
        
        // create name subfolders
        ArrayList<String> subFolderNames = new ArrayList<>();
        
        for (int i = 0; i < this.numDisk; i++) {
            subFolderNames.add(fileNameWithoutFormat+"-"+i);
        }
        
        fileOperations.createSubFolders(subFolderNames, fileFolder.getAbsolutePath(), this.numDisk);
        
        fileOperations.writeFile(blocks, subFolderNames,fileFolder.getAbsolutePath(),this.numDisk);
        
        
    }
    
    public void raid1(String fileContent,String fileName){
        char stringArray[] = fileContent.toCharArray();
        
        String binaryString = "";
        
        for (int i = 0; i < stringArray.length; i++) {
            char c = stringArray[i];
            binaryString = binaryString + this.charToBin(c);
        }
        
        ArrayList<String> blocks = this.separateBinaryStingInBlocks(binaryString);
        
        FileOperations fileOperations = new FileOperations();
        
        File fileFolder = fileOperations.createFolder("RAID_1");
        
        String fileNameWithoutFormat = fileOperations.fileNameWithoutFormat(fileName);
        
        // create name subfolders
        ArrayList<String> subFolderNames = new ArrayList<>();
        
        for (int i = 0; i < this.numDisk; i++) {
            subFolderNames.add(fileNameWithoutFormat+"-"+i);
        }
        
        // create name subfolders
        ArrayList<String> subFolderMirrorNames = new ArrayList<>();
        
        for (int i = 0; i < this.numDisk; i++) {
            subFolderMirrorNames.add(fileNameWithoutFormat+"(mirror)"+"-"+i);
        }
        
        
        fileOperations.createSubFolders(subFolderNames, fileFolder.getAbsolutePath(), this.numDisk);
        
        // mirror folders
        fileOperations.createSubFolders(subFolderMirrorNames, fileFolder.getAbsolutePath(), this.numDisk);
        

        fileOperations.writeFile(blocks, subFolderNames,fileFolder.getAbsolutePath(),this.numDisk);       
        fileOperations.writeFile(blocks, subFolderMirrorNames,fileFolder.getAbsolutePath(),this.numDisk);
        
        
        
    }
    
    public void raid2(String fileContent, String fileName){
        
    }
    
    public void raid3(String fileContent, String fileName){
        char stringArray[] = fileContent.toCharArray();
        
        String binaryString = "";
        
        for (int i = 0; i < stringArray.length; i++) {
            char c = stringArray[i];
            binaryString = binaryString + this.charToBin(c);
        }
        
        ArrayList<String> blocks = this.separateBinaryStingInBlocks(binaryString);
        
        FileOperations fileOperations = new FileOperations();
        
        File fileFolder = fileOperations.createFolder("RAID_3");
        
        String fileNameWithoutFormat = fileOperations.fileNameWithoutFormat(fileName);
        
        ArrayList<String> subFolderNames = new ArrayList<>();
        
        for (int i = 0; i < this.numDisk; i++) {
            subFolderNames.add(fileNameWithoutFormat+"-"+i);
        }
        
        // creo la carpeta con bit de paridad
        subFolderNames.add(fileNameWithoutFormat+"(parity)");
        
        fileOperations.createSubFolders(subFolderNames, fileFolder.getAbsolutePath(), this.numDisk);
        
        // ahora saco la parity
        subFolderNames.remove(fileNameWithoutFormat+"(parity)");
        
        // bit de paridad de cada bloque
        ArrayList<String> paritiesBlock = this.parityBlocks(blocks);
        
        fileOperations.writeFile(blocks, subFolderNames,fileFolder.getAbsolutePath(),this.numDisk);
        
        int raidType = 3;
        
        // escribo carpeta de paridad
        fileOperations.writeFileParity(fileNameWithoutFormat+"(parity)", paritiesBlock, fileFolder.getAbsolutePath(), this.numDisk, raidType);

    }
    
    public void raid4(String fileContent, String fileName){
        char stringArray[] = fileContent.toCharArray();
        
        String binaryString = "";
        
        for (int i = 0; i < stringArray.length; i++) {
            char c = stringArray[i];
            binaryString = binaryString + this.charToBin(c);
        }
        
        ArrayList<String> blocks = this.separateBinaryStingInBlocks(binaryString);
        
        FileOperations fileOperations = new FileOperations();
        
        File fileFolder = fileOperations.createFolder("RAID_4");
        
        String fileNameWithoutFormat = fileOperations.fileNameWithoutFormat(fileName);
        
        ArrayList<String> subFolderNames = new ArrayList<>();
        
        for (int i = 0; i < this.numDisk; i++) {
            subFolderNames.add(fileNameWithoutFormat+"-"+i);
        }
        
        // creo la carpeta con bit de paridad
        subFolderNames.add(fileNameWithoutFormat+"(parity)");
        
        fileOperations.createSubFolders(subFolderNames, fileFolder.getAbsolutePath(), this.numDisk);
        
        // ahora saco la parity
        subFolderNames.remove(fileNameWithoutFormat+"(parity)");
        
        // bit de paridad de cada bloque
        ArrayList<String> paritiesBlock = this.parityBlocks(blocks);
        
        ArrayList<String> paritiesSector = new ArrayList<>();
        
        String binarySector = "";
        for (int i = 0; i < paritiesBlock.size(); i++) {
            binarySector = binarySector + paritiesBlock.get(i);
            if ((i + 1) % this.numDisk == 0){
               String paritySector = this.getParity(binarySector);
               paritiesSector.add(paritySector);
               binarySector = "";
            }
        }
        
        if(!binarySector.equals("")){
            String paritySector = this.getParity(binarySector);
            paritiesSector.add(paritySector);
            binarySector = "";
        }
        
        fileOperations.writeFile(blocks, subFolderNames,fileFolder.getAbsolutePath(),this.numDisk);  
        
        int raidType = 4;
        
        // escribo carpeta de paridad
        fileOperations.writeFileParity(fileNameWithoutFormat+"(parity)", paritiesSector, fileFolder.getAbsolutePath(), this.numDisk, raidType);
        
        
        
    }
    
    public void raid5(String fileContent, String fileName){
        
    }
    
    public void raid6(String fileContent, String fileName){
        
       
    }
    
    public String readRaid0(String path){
        File file = new File(path);
        
        FileOperations fileOperations = new FileOperations();

        String binaryString = fileOperations.readFile(file.list(), file.getAbsolutePath(), this.numDisk);
        
        String originalString = "";
        
        // check
        if (binaryString.length() % 8 == 0){
            String bin = "";
            int n = binaryString.length() / 8;
            for (int i = 0; i < n; i++) {
                bin = binaryString.substring(0, 8);
                String stringChar = this.binToStringChar(bin);
                originalString = originalString + stringChar;
                binaryString = binaryString.substring(8);
            }
            
        }
        
        return originalString;
        
    }
    
    public String readRaid1(String path){
        
        File file = new File(path);
        
        FileOperations fileOperations = new FileOperations();
        
        ArrayList<String> nameFolders = new ArrayList<>();

        // selected not mirror folders
        for (String nameFolder : file.list()) {
            if (!nameFolder.contains("(mirror)")){
                nameFolders.add(nameFolder);
            }
        }
        
        String fileList[] = new String[nameFolders.size()];
        for (int i = 0; i < nameFolders.size(); i++) {
            fileList[i] = nameFolders.get(i);
        }
        
        
        String binaryString = fileOperations.readFile(fileList, file.getAbsolutePath(), this.numDisk);
        
        String originalString = "";
        
        // check
        if (binaryString.length() % 8 == 0){
            String bin = "";
            int n = binaryString.length() / 8;
            for (int i = 0; i < n; i++) {
                bin = binaryString.substring(0, 8);
                String stringChar = this.binToStringChar(bin);
                originalString = originalString + stringChar;
                binaryString = binaryString.substring(8);
            }
            
        }
        
        return originalString;
    }
    
    public String readRaid3(String path){
        File file = new File(path);
        
        FileOperations fileOperations = new FileOperations();
        
        ArrayList<String> nameFolders = new ArrayList<>();

        // selected not mirror folders
        for (String nameFolder : file.list()) {
            if (!nameFolder.contains("(parity)")){
                nameFolders.add(nameFolder);
            }
        }
        
        String fileList[] = new String[nameFolders.size()];
        for (int i = 0; i < nameFolders.size(); i++) {
            fileList[i] = nameFolders.get(i);
        }
        
        
        String binaryString = fileOperations.readFile(fileList, file.getAbsolutePath(), this.numDisk);
        
        String originalString = "";
        
        // check
        if (binaryString.length() % 8 == 0){
            String bin = "";
            int n = binaryString.length() / 8;
            for (int i = 0; i < n; i++) {
                bin = binaryString.substring(0, 8);
                String stringChar = this.binToStringChar(bin);
                originalString = originalString + stringChar;
                binaryString = binaryString.substring(8);
            }
            
        }
        
        return originalString;
    }
    
    public String readRaid4(String path){
        
        File file = new File(path);
        
        FileOperations fileOperations = new FileOperations();
        
        ArrayList<String> nameFolders = new ArrayList<>();

        // selected not mirror folders
        for (String nameFolder : file.list()) {
            if (!nameFolder.contains("(parity)")){
                nameFolders.add(nameFolder);
            }
        }
        
        String fileList[] = new String[nameFolders.size()];
        for (int i = 0; i < nameFolders.size(); i++) {
            fileList[i] = nameFolders.get(i);
        }
        
        
        String binaryString = fileOperations.readFile(fileList, file.getAbsolutePath(), this.numDisk);
        
        String originalString = "";
        
        // check
        if (binaryString.length() % 8 == 0){
            String bin = "";
            int n = binaryString.length() / 8;
            for (int i = 0; i < n; i++) {
                bin = binaryString.substring(0, 8);
                String stringChar = this.binToStringChar(bin);
                originalString = originalString + stringChar;
                binaryString = binaryString.substring(8);
            }
            
        }
        
        return originalString;
    }
    
    // convierte de caracter a un string binario
    public String charToBin(char c){
        int num = (int)c;
        String bin = "";
        // 2^8 (ascii 255)
        for (int i = 7; i >= 0; i--) {
            if (num >= Math.pow(2,i)){
                bin = bin + "1";
                num = num - (int)Math.pow(2,i);
            }
            else{
                bin = bin + "0";
            } 
        }
        return bin;
    }
    
    // convierte de un string binario a un caracter
    public String binToStringChar(String bin){
        
        // en caso de que el string binario no sea de 7 caracteres
        int binLength = bin.length();
        if (binLength < 8){
            int diff = 8 - binLength;
            for (int i = 0; i < diff; i++) {
                bin = "0" + bin;
            }
        }
                
        String string = "";
        char stringArray[] = bin.toCharArray();
        int n = stringArray.length;
        int c = 0;
        for (int i = 0; i < n; i++) {
            if (stringArray[i] == '1'){
                c = c + (int)Math.pow(2,7 - i);
            }
        }
        char character = (char)c;
        string = String.valueOf(character);
        
        return string;
    }
    
    // metodo que convierte 
    
    // separa el string binario en bloques de tamaño blocksize
    public ArrayList<String> separateBinaryStingInBlocks(String binaryString){
        ArrayList<String> blocks = new ArrayList<>();
        while (!binaryString.equals("")){
            // caso cuando ya el string es mas corto que 8 bits ( 8 caracteres)
            if (binaryString.length() < this.blockSize){
                String block = binaryString.substring(0,binaryString.length());
                blocks.add(block);
                binaryString = binaryString.substring(binaryString.length());
                
            }
            else{
                String block = binaryString.substring(0,this.blockSize);
                blocks.add(block);
                binaryString = binaryString.substring(this.blockSize);
            }            
        }
        
        return blocks;
    }
    
    // metodo que calcula la paridad de un bloque de string binarios
    public ArrayList<String> parityBlocks(ArrayList<String> blocks){
        ArrayList<String> parities = new ArrayList<>();
        for (String binBlock : blocks) {
            String parity = this.getParity(binBlock);
            parities.add(parity);
        }
        return parities;
    }
    
    // metodo que calcula la paridad para un string binario (8 bits)
    // Nums 1 % 2 == 0  --> paridad 0
    // Nums 1 % 2 != 0  --> paridad 1
    public String getParity(String binaryString){
        int conts1 = 0;
        
        char binArray[] = binaryString.toCharArray();
        for (int i = 0; i < binArray.length; i++) {
            char c = binArray[i];
            if (c == '1'){
                conts1++;
            }
        }
        // paridad 0
        if (conts1 % 2 == 0){
            return "0";
        }
        // paridad 1
        else{
            return "1";
        }
    }
    
}
