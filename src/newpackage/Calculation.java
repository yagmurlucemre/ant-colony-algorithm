/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newpackage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Calculation {
     List<Koordinat> koordinat = new ArrayList<>();
      public  List<Koordinat>  cityKordinat(int city)
    { 
      for (int i=0; i<city;i++)
      {
         Random rn = new Random();
         int x = rn.nextInt(466);
         int y = rn.nextInt(470);
         koordinat.add(new Koordinat(i+1,x,y));
      }
      return koordinat;
    }
      

   public double[][] distanceMatrix(List<Koordinat> koordinatlar) {
     double[][] uzaklikMatrisi = new double[koordinatlar.size()][koordinatlar.size()];

        int satirIndisi = 0;
        for (Koordinat koordinat1 : koordinatlar) {
            int sutunIndisi = 0;
            for (Koordinat koordinat2 : koordinatlar) {
                uzaklikMatrisi[satirIndisi][sutunIndisi] = oklidDistanceHesapla(koordinat1.x, koordinat1.y, koordinat2.x, koordinat2.y);
                sutunIndisi++;
            }
            satirIndisi++;
        }
        return uzaklikMatrisi;
    }

    private double oklidDistanceHesapla(double x1, double y1, double x2, double y2) {
          final double xLerFarki = x2 - x1;
        final double yLerFarki = y2 - y1;
        return (Math.abs((Math.sqrt((xLerFarki * xLerFarki) + (yLerFarki * yLerFarki)))) + 1);
    }

   public  Town[] townKordinat(int city) {
     Town[]  towns = new Town[city]; 

 		for(int i=0; i<city; i++){
 			towns[i] = new Town (koordinat.get(i).id,koordinat.get(i).x,koordinat.get(i).y);
 		}
         return    towns;    
    }
}
