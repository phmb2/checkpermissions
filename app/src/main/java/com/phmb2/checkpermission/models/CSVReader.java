package com.phmb2.checkpermission.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by phmb2 on 01/11/17.
 */

public class CSVReader {

    InputStream inputStream;

    public CSVReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    public List<String[]> read(){

        List<String[]> listaResultado = new ArrayList<String[]>();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        try{
            String csvLine;

            while ((csvLine = bufferedReader.readLine()) != null)
            {
                String[] row = csvLine.split(";");
                listaResultado.add(row);
            }

        }catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo CSV: " + e);
        }finally {

            try{
                inputStream.close();
            }catch(IOException e) {
                throw new RuntimeException("Erro ao fechar o fluxo de entrada: " + e);
            }
        }

        return listaResultado;
    }
}
