package com.example.elGamal;

import java.util.ArrayList;

public class KonversiChar {
 
    private ArrayList listChar  =  new ArrayList();
 
    public ArrayList getCharASCII(String pesan) {
 
        for (int i  =  0; i < pesan.length(); i++) {
            char chr  =  pesan.charAt(i);
            int in  =  chr;
 
            listChar.add(in);
        }
 
        return listChar;
    }
}