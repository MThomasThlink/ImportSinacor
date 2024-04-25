package com.thlink.NNPdf;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Util 
{
    public Collection<File> listFileTree(File dir, String pExt) 
    {
        Set<File> fileTree = new HashSet<>();
        if(dir==null||dir.listFiles()==null){
            return fileTree;
        }
        for (File entry : dir.listFiles()) 
        {
            if (entry.isFile() && entry.getName().endsWith(pExt)) 
                fileTree.add(entry);
            else 
                fileTree.addAll(listFileTree(entry, pExt));
        }
        return fileTree;
    }
    
    public List<String> verificaNovoArquivoA ( String pExt, String path, Integer pQtd, String pEmp ) 
    {
        DirectoryStream streamFolder;
        List<String> myList = new ArrayList<>();
        Path pathFolder = FileSystems.getDefault().getPath(path);
        try 
        {
            streamFolder = Files.newDirectoryStream(pathFolder);
            for (Object x : streamFolder )
            {
                String f = path.concat("/").concat(((Path)x).getFileName().toString());
                if (f.toLowerCase().endsWith(pExt))
                {
                    String[] parts = ((Path)x).getFileName().toString().split("-");
                    if (pEmp.equals("") || parts[2].startsWith(pEmp))
                    {
                        myList.add(f);
                        if (pQtd != null && myList.size() >= pQtd)
                            break;
                    }
                }
            }
            streamFolder.close();
            return myList;
        } catch (IOException ex) {
            System.out.println("verificaNovoArquivoA ERROR: " + ex.toString());
        }
        return new ArrayList<>();
    }
    private List<String> files;
    public List<String> listArquivoRecursivo ( String pExt, String path ) 
    {
        files = new ArrayList<>();
        verificaNovoArquivoRecursivo(pExt, path);
        return files;
    }
    
    public String verificaNovoArquivoRecursivo ( String pExt, String path ) 
    {
        //logger.info("verificaNovoArquivoRecursivo em " + path);
        
        File root = new File( path );
        File[] list = root.listFiles();

        if (list == null) 
        {
            //logger.info("1. Sem arquivos.");
            return null;
        }

        for ( File f : list ) 
        {
            if ( f.isDirectory() ) 
            {
                //logger.info("f.isDirectory()");
                String testFile = verificaNovoArquivoRecursivo( pExt, f.getAbsolutePath() );
                if (testFile != null)
                {
                    //logger.info( "2. File:" + f.getAbsoluteFile() );
                    files.add(testFile);
                    return testFile;
                }
                //else
                //    logger.info( "Dir:" + f.getAbsoluteFile() );
            }
            else 
            {
                //logger.info("!f.isDirectory()");
                if (f.getName().toLowerCase().endsWith(pExt))
                {
                    //if (isJPGReady(f.getAbsolutePath()))
                    {
                        //logger.info( "1. File:" + f.getAbsoluteFile() );
                        files.add(f.getAbsoluteFile().toString().replace("\\", "/"));
                        return f.getAbsoluteFile().toString().replace("\\", "/");
                    }
                }
                //logger.info( "1. IGN: " + f.getAbsoluteFile() );
            }
        }
        //logger.info("2. Sem arquivos.");
        return null;
    }
}


