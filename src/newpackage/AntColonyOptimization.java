package newpackage;

import java.awt.Graphics;
import java.util.Random;


public class AntColonyOptimization {

    public Ant[] ants = null;
    public static Graphics graphics;
    public double[][] distanceMatrix = null;
    public int antCount;
    public double c = 1.0;
    public double[][] pheromonTrail = null;
    public double[] posibilities = null;
    public int cityCount = 0;
    public int trailIndex = 0;
    private Random rnd = new Random();
    private double Q = 100;
    public int[] bestTour;
    public double bestTourLength;

    private class Ant { 

        public int tour[] = new int[distanceMatrix.length];
        public boolean visited[] = new boolean[distanceMatrix.length];

        public void travelToCity(int sehir) {
            tour[trailIndex + 1] = sehir;
            visited[sehir] = true;
        }

        public boolean visited(int sehir) {
            return visited[sehir];
        }

        public double tourLength() {
            double length = distanceMatrix[tour[cityCount - 1]][tour[0]]; 
            for (int i = 0; i < cityCount - 1; i++) {
                length = length + distanceMatrix[tour[i]][tour[i + 1]];
            }
            return length;
        }

        public void clear() {
            for (int i = 0; i < cityCount; i++) {
                visited[i] = false;
            }
        }

    }

    public void koloniolustur(int sehir) {
        Calculation cal = new Calculation();
        distanceMatrix = cal.distanceMatrix(MainScreen.coordinates);
        this.cityCount = sehir;
        double alfa = Double.parseDouble(MainScreen.alpha.getText());
        double beta = Double.parseDouble(MainScreen.beta.getText());
        double randomFactor = Double.parseDouble(MainScreen.rassal.getText());
        double dissolvingFactor = Double.parseDouble(MainScreen.buhar.getText());

        antCount = cityCount;
        pheromonTrail = new double[cityCount][cityCount];
        posibilities = new double[cityCount];
        ants = new Ant[antCount];
        for (int k = 0; k < antCount; k++) {
            ants[k] = new Ant();
        }
        int iterasyon = Integer.parseInt(MainScreen.deneysayi.getText());
        calculate(iterasyon, alfa, beta, randomFactor, dissolvingFactor);
    }

    public void calculate(int iteration, double alpha, double beta, double randomFactor, double dissolveFactor) {
        double lastBestTourLength = Double.MAX_VALUE;
        for (int i = 0; i < cityCount; i++) {
            for (int j = 0; j < cityCount; j++) {
                pheromonTrail[i][j] = c;
            }
        }
        for (int i = 0; i < iteration; i++) {

            setupAnt();
            antGoTo(alpha, beta, randomFactor);
            updatePheromon(dissolveFactor);
            setupBestTour();
            MainScreen.sonuc.append(String.valueOf(i + 1) + ". iterasyon :\n ");
            for (int j = 0; j < bestTour.length; j++) {
                MainScreen.sonuc.append(Integer.toString(bestTour[j] + 1) + "-");
            }
            MainScreen.sonuc.append("\n");
            if (bestTourLength < lastBestTourLength) {
                MainScreen.kenarCiz(graphics, bestTour);
                lastBestTourLength = bestTourLength;
            }
            MainScreen.sonuc.append("\nEn iyi tur uzunluğu : " + Double.toString(bestTourLength - cityCount));
            MainScreen.sonuc.append("\n");
            for (int l = 0; l < bestTour.length; l++) {
                MainScreen.jTextArea1.append(Integer.toString(bestTour[l] + 1) + "-");
            }
            MainScreen.jTextArea1.append("\n");
           // MainScreen.jTextField8.setText(Double.toString(enIyiTurUzunlugu - sehirSayisi) + " birim");
        }

    }

    private void setupBestTour() {
        if (bestTour == null) {
            bestTour = ants[0].tour;
            bestTourLength = ants[0].tourLength();
        }
        for (int i = 1; i < antCount; i++) {
            if (ants[i].tourLength() < bestTourLength) {
                bestTourLength = ants[i].tourLength();
                bestTour = ants[i].tour.clone();
            }
        }
    }

    private void setupAnt() {
        trailIndex = -1;
        for (int i = 0; i < antCount; i++) {
            ants[i].clear();
            ants[i].travelToCity(rnd.nextInt(cityCount));
        }
        trailIndex++;
    }

    private void antGoTo(double alfa, double beta, double randomFactor) {
        while (trailIndex < (cityCount - 1)) {
            for (int i = 0; i < antCount; i++) {
                ants[i].travelToCity(selectNextCiy(ants[i], alfa, beta, randomFactor));
            }
            trailIndex++;
        }
    }

    private int selectNextCiy(Ant karinca, double alfa, double beta, double rassallikFaktoru) {
        if (rnd.nextDouble() < rassallikFaktoru) {
            int r = rnd.nextInt(cityCount - trailIndex);
            int j = -1;
            for (int i = 0; i < cityCount; i++) {
                if (!karinca.visited(i)) {
                    j++;
                }
                if (j == r) {
                    return i;
                }
            }
        }
        return olasilikHesapla(karinca, alfa, beta);
    }

    private int olasilikHesapla(Ant karinca, double alfa, double beta) {
        int i = karinca.tour[trailIndex];
        int maksOlasilikIndisi = 0;
        double maksOlasilik = 0.0;
        double olasiliklarToplami = 0.0;
        for (int j = 0; j < cityCount; j++) {
            if (!karinca.visited(j)) {
                olasiliklarToplami += Math.pow(pheromonTrail[i][j], alfa) * Math.pow(1.0 / distanceMatrix[i][j], beta);
            }
        }

        for (int j = 0; j < cityCount; j++) {
            if (karinca.visited(j)) {
                posibilities[j] = 0.0;
            } else {
                double pay = Math.pow(pheromonTrail[i][j], alfa) * Math.pow(1.0 / distanceMatrix[i][j], beta);
                posibilities[j] = pay / olasiliklarToplami;
                if (posibilities[j] > maksOlasilik) {
                    maksOlasilik = posibilities[j];
                    maksOlasilikIndisi = j;
                }
            }
        }
        return maksOlasilikIndisi;
    }

    private void updatePheromon(double buharlasmaFaktoru) {
        for (int i = 0; i < cityCount; i++) {
            for (int j = 0; j < cityCount; j++) {
                pheromonTrail[i][j] = pheromonTrail[i][j] * buharlasmaFaktoru;
            }
        }

        for (int i = 0; i < antCount; i++) {
            double karincaKatkisi = Q / ants[i].tourLength();
            for (int j = 0; j < cityCount - 1; j++) {
                pheromonTrail[ants[i].tour[j]][ants[i].tour[j + 1]] += karincaKatkisi;
            }
            pheromonTrail[ants[i].tour[cityCount - 1]][ants[i].tour[0]] += karincaKatkisi; 
        }
    }
}
